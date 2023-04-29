package com.mout.consumer;

import com.mout.helper.LocalDateHelper;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class KafkaConsumerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerDemo.class);
    private static final Duration POLLING_TIMEOUT = Duration.ofMillis(500);

    private final KafkaConsumerFactory factory = new KafkaConsumerFactory();
    private final LocalDateHelper localDateHelper = new LocalDateHelper();

    public static void main(String[] args) {
        var kafkaDemo = new KafkaConsumerDemo();
        kafkaDemo.consumeData();
    }

    private void consumeData() {
        try (var consumer = factory.create()) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::commitSync));
            consumeDailyQuotes(consumer);
        }
    }

    private void consumeDailyQuotes(KafkaConsumer<Void, Integer> consumer) {
        while (true) {
            var records = consumer.poll(POLLING_TIMEOUT);
            records.forEach(record -> {
                var localDate = localDateHelper.convertEpochInMillisToLocalDate(record.timestamp());
                LOGGER.info("Received record with quote {} of date {}", record.value(), localDate);
            });
            consumer.commitAsync();
        }
    }
}

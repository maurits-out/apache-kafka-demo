package com.mout.consumer;

import com.mout.helper.LocalDateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class KafkaConsumerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerDemo.class);

    private final KafkaConsumerFactory factory = new KafkaConsumerFactory();
    private final LocalDateHelper localDateHelper = new LocalDateHelper();

    public static void main(String[] args) {
        var kafkaDemo = new KafkaConsumerDemo();
        kafkaDemo.consumeData();
    }

    private void consumeData() {
        try (var consumer = factory.create()) {
            consumer.subscribe(List.of("daily-quotes"));
            while (true) {
                var records = consumer.poll(Duration.ofMillis(500));
                records.forEach(record -> {
                    var localDate = localDateHelper.convertEpochInMillisToLocalDate(record.timestamp());
                    LOGGER.info("Received record with quote {} of date {}", record.value(), localDate);
                });
                consumer.commitAsync();
            }
        }
    }
}

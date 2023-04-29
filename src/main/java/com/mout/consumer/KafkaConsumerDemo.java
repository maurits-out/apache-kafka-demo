package com.mout.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class KafkaConsumerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerDemo.class);
    private static final ZoneId ZONE_ID = ZoneId.of("GMT-4");

    private final KafkaConsumerFactory factory = new KafkaConsumerFactory();

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
                    var localDate = convertEpoch(record.timestamp());
                    LOGGER.info("Received record with quote {} of date {}", record.value(), localDate);
                });
                consumer.commitAsync();
            }
        }
    }

    private static LocalDate convertEpoch(long epoch) {
        var instant = Instant.ofEpochMilli(epoch);
        return instant.atZone(ZONE_ID).toLocalDate();
    }
}

package com.mout.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.util.List;

public class KafkaProducerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerDemo.class);
    private static final String TOPIC = "daily-quotes";
    private static final ZoneId ZONE_ID = ZoneId.of("GMT-4");

    private final KafkaProducerFactory kafkaProducerFactory = new KafkaProducerFactory();

    public static void main(String[] args) {
        var kafkaDemo = new KafkaProducerDemo();
        kafkaDemo.sendData();
    }

    private void sendData() {
        var dailyQuotes = readDailyQuotes();
        try (var producer = kafkaProducerFactory.create()) {
            sendDailyQuotes(dailyQuotes, producer);
        }
    }

    private void sendDailyQuotes(List<NasdaqDailyQuotes> dailyQuotes, KafkaProducer<Void, Integer> producer) {
        dailyQuotes.forEach(dq -> {
            var record = createProducerRecord(dq);
            var callback = createCallback(dq);
            producer.send(record, callback);
        });
    }

    private Callback createCallback(NasdaqDailyQuotes dq) {
        return (metadata, exception) -> {
            if (metadata != null) {
                LOGGER.info("Quote of {} sent to topic {} at offset {}", dq.date(), metadata.topic(), metadata.offset());
            } else {
                LOGGER.error("Failed to send quote of " + dq.date(), exception);
            }
        };
    }

    private ProducerRecord<Void, Integer> createProducerRecord(NasdaqDailyQuotes dailyQuotes) {
        var instant = dailyQuotes.date().atStartOfDay(ZONE_ID).toInstant();
        var timestamp = instant.toEpochMilli();
        return new ProducerRecord<>(TOPIC, null, timestamp, null, dailyQuotes.closePriceInCents());
    }

    private List<NasdaqDailyQuotes> readDailyQuotes() {
        var reader = new NasdaqDailyQuotesReader();
        return reader.read();
    }
}

package com.mout;

import java.util.List;

public class KafkaDemo {
    public static void main(String[] args) {
        NasdaqDailyQuotesReader reader = new NasdaqDailyQuotesReader();
        List<NasdaqDailyQuotes> dailyQuotes = reader.read();
        System.out.println(dailyQuotes.size());
    }
}
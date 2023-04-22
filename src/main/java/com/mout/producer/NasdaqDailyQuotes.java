package com.mout.producer;

import java.time.LocalDate;

record NasdaqDailyQuotes(LocalDate date, int closePriceInCents, int volume, int openPriceInCents, int highPriceInCents, int lowPriceInCents) implements Comparable<NasdaqDailyQuotes> {

    @Override
    public int compareTo(NasdaqDailyQuotes other) {
        return date.compareTo(other.date);
    }
}

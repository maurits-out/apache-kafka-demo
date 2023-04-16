package com.mout;

import java.time.LocalDate;

public record NasdaqDailyQuotes(LocalDate date, int closePriceInCents, int volume, int openPriceInCents, int highPriceInCents, int lowPriceInCents) {}



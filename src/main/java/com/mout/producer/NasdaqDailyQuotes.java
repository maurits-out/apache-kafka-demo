package com.mout.producer;

import java.time.LocalDate;

record NasdaqDailyQuotes(LocalDate date, int closePriceInCents, int volume, int openPriceInCents, int highPriceInCents, int lowPriceInCents) {}



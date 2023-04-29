package com.mout.helper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class LocalDateHelper {

    private static final ZoneId ZONE_ID = ZoneId.of("GMT-4");

    public long convertLocalDateToEpochInMillis(LocalDate localDate) {
        var instant = localDate.atStartOfDay(ZONE_ID).toInstant();
        return instant.toEpochMilli();
    }

    public LocalDate convertEpochInMillisToLocalDate(long epochInMillis) {
        var instant = Instant.ofEpochMilli(epochInMillis);
        return instant.atZone(ZONE_ID).toLocalDate();
    }
}

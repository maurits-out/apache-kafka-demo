package com.mout;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class NasdaqDailyQuotesReader {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final NumberFormat AMOUNT_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);
    private static final String NASDAQ_HISTORICAL_DATA_CSV = "/NasdaqHistoricalData.csv";

    static {
        ((DecimalFormat) AMOUNT_FORMAT).setParseBigDecimal(true);
    }

    public List<NasdaqDailyQuotes> read() {
        var resource = getClass().getResource(NASDAQ_HISTORICAL_DATA_CSV);
        if (resource == null) {
            throw new KafkaDemoException("Could not find file containing stock data");
        }
        try {
            var path = Paths.get(resource.toURI());
            return Files
                    .readAllLines(path)
                    .stream()
                    .skip(1)
                    .map(this::parseLine)
                    .toList();
        } catch (IOException | URISyntaxException ex) {
            throw new KafkaDemoException("Could not read stock data", ex);
        }
    }

    private NasdaqDailyQuotes parseLine(String line) {
        try {
            var parts = line.split(",");
            return new NasdaqDailyQuotes(
                    parseDate(parts[0]),
                    parseAmount(parts[1]),
                    Integer.parseInt(parts[2]),
                    parseAmount(parts[3]),
                    parseAmount(parts[4]),
                    parseAmount(parts[5])
            );
        } catch (ParseException ex) {
            throw new KafkaDemoException("Parse error in line '" + line + "'", ex);
        }
    }

    private static int parseAmount(String s) throws ParseException {
        var amount = (BigDecimal) AMOUNT_FORMAT.parse(s);
        return amount.movePointRight(2).intValue();
    }

    private static LocalDate parseDate(String s) {
        return LocalDate.parse(s, DATE_FORMATTER);
    }
}

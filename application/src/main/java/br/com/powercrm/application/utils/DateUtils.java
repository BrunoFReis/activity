package br.com.powercrm.application.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static Instant convertStringDateToInstant(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        Date date = sdf.parse(dateString);
        return date.toInstant();
    }

    public static String convertInstantToStringDate(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    public static String convertInstantToStringDateTime(Instant instant) {
        if (instant != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                    .withLocale(Locale.getDefault())
                    .withZone(ZoneId.systemDefault());
            return formatter.format(instant);
        }

        return null;
    }
}

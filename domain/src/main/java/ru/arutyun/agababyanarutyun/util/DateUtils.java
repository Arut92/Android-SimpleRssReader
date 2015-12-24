package ru.arutyun.agababyanarutyun.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.util.Comparator;
import java.util.Locale;

import ru.arutyun.agababyanarutyun.db.News;

public final class DateUtils {

    private static final String PUB_DATE_FORMAT  = "EEE, dd MMM yyyy HH:mm:ss Z"; //"Sat, 21 Nov 2015 16:56:04 +0300"
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormat.forPattern(PUB_DATE_FORMAT).withLocale(Locale.ENGLISH);

    private DateUtils() {}

    @Nullable
    public static LocalDateTime parseStringToDateTime(@NotNull final String date) {
        LocalDateTime dateTime = null;
        try {
            dateTime = FORMATTER.parseLocalDateTime(date);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

}


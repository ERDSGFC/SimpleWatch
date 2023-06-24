package com.qin.utils;

import io.micrometer.common.lang.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MyUtils {

    /**
     * 不需要实例化
     */
    private MyUtils() {}

    public static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final ZoneId zoneId = ZoneId.systemDefault();

    public static long getTimestamp() {
        return Instant.now().getEpochSecond();
    }

    public static long getTimestamp(final LocalDateTime localDateTime) {
        return localDateTime.atZone(MyUtils.zoneId).toEpochSecond();
    }

    public static long getTimestampMilli() {
        return Instant.now().toEpochMilli();
    }

    public static long getTimestampMilli(final LocalDateTime localDateTime) {
        return localDateTime.atZone(MyUtils.zoneId).toInstant().toEpochMilli();
    }

    @NonNull
    public static LocalDateTime getLocalDateTime(final long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), MyUtils.zoneId);
    }

    @NonNull
    public static LocalDateTime getLocalDateTimeOfMilli(final long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), MyUtils.zoneId);
    }

    public static String localDateTimeFormatter(final LocalDateTime localDateTime) {
        return localDateTime.format(MyUtils.dateTimeFormatter);
    }

    public static LocalDateTime getLocalDateTime(final String dataTime) {
        return LocalDateTime.parse(dataTime, MyUtils.dateTimeFormatter);
    }
}

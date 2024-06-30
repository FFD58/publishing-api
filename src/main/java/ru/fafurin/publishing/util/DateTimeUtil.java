package ru.fafurin.publishing.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DateTimeUtil {

    public static Long toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        } else return Timestamp.valueOf(localDateTime).getTime();
    }

}

package net.chinzer.seismicincidenttracker;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import androidx.room.TypeConverter;

public class DateTypeConverters {
    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @TypeConverter
    public static OffsetDateTime fromDateTimetoOffset(String text){
        return OffsetDateTime.parse(text, formatter);
    }

    @TypeConverter
    public static String fromOffsettoDatetime(OffsetDateTime date){
        return date.format(formatter);
    }
}

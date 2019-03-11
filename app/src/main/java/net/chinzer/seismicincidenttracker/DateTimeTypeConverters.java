package net.chinzer.seismicincidenttracker;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

import androidx.room.TypeConverter;

public class DateTimeTypeConverters {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_OFFSET_TIME;


    @TypeConverter
    public static OffsetDateTime fromDateTimetoOffset(String text){
        return OffsetDateTime.parse(text, dateFormatter);
    }

    @TypeConverter
    public static String fromOffsettoDatetime(OffsetDateTime date){
        return date.format(dateFormatter);
    }

    @TypeConverter
    public static OffsetTime fromTimetoOffset(String text){
        return OffsetTime.parse(text, timeFormatter);
    }

    @TypeConverter
    public static String fromOffsettoTime(OffsetTime time){
        return time.format(timeFormatter);
    }
}

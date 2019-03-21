package net.chinzer.seismicincidenttracker.Converters;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

import androidx.room.TypeConverter;

public class DateTimeTypeConverters {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_OFFSET_TIME;
    private static DateTimeFormatter userInputDateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static DateTimeFormatter userInputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");


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

    public static OffsetDateTime fromUserInputDateToOffsetDateTime(String text){
        return OffsetDateTime.parse(text, userInputDateTimeFormatter);
    }

    public static String fromOffsetDateTimeToUserInputDate(OffsetDateTime date){
        return date.format(userInputDateTimeFormatter);
    }

    public static OffsetTime fromUserInputTimeToOffsetTime(String text){
        return OffsetTime.parse(text, userInputTimeFormatter);
    }
    public static String fromOffsetTimeToUserInputTime(OffsetTime time){
        return time.format(userInputTimeFormatter);
    }

    public static String fromOffsetTimeToUserInputTime(OffsetDateTime time){
        return time.format(userInputTimeFormatter);
    }

}

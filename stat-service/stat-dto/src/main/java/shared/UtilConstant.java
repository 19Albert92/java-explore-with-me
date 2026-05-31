package shared;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class UtilConstant {

    public final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
}
package shared;

import lombok.experimental.UtilityClass;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class UtilConstant {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
}

package minhcreator.functional.location;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author MinhCreatorVN
 * @version 1.0 PreAlpha
 */
public class TimeManager {
    private static TimeManager instance;

    public TimeManager GetInstance() {
        if (instance == null) {
            instance = new TimeManager();
        }
        return instance;
    }

    public String TimeNow() {
        return LocalDateTime.now().toString();
    }

    /**
     * Returns the current time in the specified format.
     *
     * @param format the format of the time string (see {@link DateTimeFormatter} for details) make expression must have yyyy-MM-dd, dd/MM/yyyy, dd-MMM-yyyy || E, MMM dd yyyy
     *               Addition can be combined with HH:mm:ss
     * @return the current time in the specified format
     * @author MinhCreatorVN
     */
    public String TimeNowFormat(String format) {
        LocalDateTime times = LocalDateTime.now();

        DateTimeFormatter pattern_format = DateTimeFormatter.ofPattern(format);

        // return time after formated
        return pattern_format.format(times);
    }

}
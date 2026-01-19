package minhcreator.functional.location;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author MinhCreatorVN
 * @version 1.0 PreAlpha
 */
public class TimeManager {
    private static TimeManager instance;

    /**
     * Returns the singleton instance of TimeManager.
     *
     * @return the singleton instance of TimeManager
     */
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

    /**
     * Returns the current time in the specified format.
     *
     * @param dateTime the date time string make expression given like 2026-01-17 21:44:02 and then convert following to given format pattern
     * @param format   the format of the time string (see {@link DateTimeFormatter} for details) make expression must have yyyy-MM-dd, dd/MM/yyyy, dd-MMM-yyyy || E, MMM dd yyyy
     *                 Addition can be combined with HH:mm:ss
     * @return the current time in the specified format
     * @author MinhCreatorVN
     */
    public String TimeDateFormated(String dateTime, String format) {
        // First try to parse with space separator, if that fails try with ISO format
        try {
            // default format
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // transform string to standard date format
            LocalDate covert_string_to_date = LocalDate.parse(dateTime, inputFormatter);

            // format date to matched given format
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(format);
            return outputFormatter.format(covert_string_to_date);
        } catch (DateTimeParseException e) {
            // If space-separated format fails, try with default ISO format
            try {
                LocalDate covert_string_to_date = LocalDate.parse(dateTime);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(format);
                return outputFormatter.format(covert_string_to_date);
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("Invalid date-time format. Expected 'yyyy-MM-dd HH:mm:ss' or ISO format");
            }
        }
    }

    /**
     * Returns the date and time that is a specified number of days before the given date and time.
     *
     * @param currDay      the date and time to start from (in the format specified by the format parameter)
     * @param format       the format of the date and time string (see {@link DateTimeFormatter} for details)
     * @param backTraceDay the number of days to subtract from the current date and time
     * @return the date and time that is backTraceDay days before the current date and time
     * @author MinhCreatorVN
     */
    public String wayBackMachine(String currDay, String format, int backTraceDay) {
        // format current day
        LocalDate currDayParse = LocalDate.parse(currDay, DateTimeFormatter.ofPattern(format));
        LocalDate BackDate = currDayParse.minusDays(backTraceDay);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return BackDate.format(formatter);

    }
}
package minhcreator.debugPage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class functional_debug {

    public static void time() {
        LocalDateTime times = LocalDateTime.now();
        System.out.println(times);
        DateTimeFormatter pattern_format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String format = pattern_format.format(times);
        System.out.println(format + " after parser");
    }

    public static void main(String[] args) {
        time();
    }

}
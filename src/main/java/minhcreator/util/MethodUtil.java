package minhcreator.util;

import java.util.regex.Pattern;

public class MethodUtil {
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[A-Za-z0-9]*");

    public static int checkPasswordStrength(String password) {
        int score = 0;
        if (password.length() >= 8) {
            score++;
        }
        boolean hasUppercase = !password.equals(password.toLowerCase());
        if (hasUppercase) {
            score++;
        }
        boolean hasLowercase = !password.equals(password.toUpperCase());
        if (hasLowercase) {
            score++;
        }
        boolean hasDigit = DIGIT_PATTERN.matcher(password).matches();
        if (hasDigit) {
            score++;
        }
        boolean hasSpecialChar = !SPECIAL_CHAR_PATTERN.matcher(password).matches();
        if (hasSpecialChar) {
            score++;
        }
        if (score < 3) {
            return 1;
        } else if (score < 5) {
            return 2;
        } else {
            return 3;
        }
    }
}
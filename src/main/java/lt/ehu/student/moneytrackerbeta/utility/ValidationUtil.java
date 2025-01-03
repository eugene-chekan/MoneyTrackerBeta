package lt.ehu.student.moneytrackerbeta.utility;

public class ValidationUtil {
    public static boolean isValidInput(String input) {
        // Reject inputs containing SQL injection patterns
        return input != null && !input.contains("--")
                && !input.contains(";")
                && !input.toLowerCase().contains("union")
                && !input.toLowerCase().contains("drop");
    }

    public static String sanitizeInput(String input) {
        if (input == null)
            return null;
        // Remove potentially dangerous characters
        return input.replaceAll("[;'\"\\\\]", "");
    }
}

package thuvien.util;

public class StringUtils {

    public static String generateLoanCode() {
        return "LOAN-" + System.currentTimeMillis();
    }

    public static String generateCardNumber() {
        return "CARD-" + System.currentTimeMillis();
    }
}
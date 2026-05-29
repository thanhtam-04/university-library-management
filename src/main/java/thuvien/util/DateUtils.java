package thuvien.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    private static final long DEFAULT_LOAN_PERIOD_DAYS = 0;

	public static int calculateOverdueDays(LocalDate dueDate) {
        if (dueDate == null || dueDate.isAfter(LocalDate.now())) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    public static LocalDate calculateDueDate() {
        return LocalDate.now().plusDays(DEFAULT_LOAN_PERIOD_DAYS);
    }

    public static boolean isOverdue(LocalDate dueDate) {
        return dueDate != null && dueDate.isBefore(LocalDate.now());
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}
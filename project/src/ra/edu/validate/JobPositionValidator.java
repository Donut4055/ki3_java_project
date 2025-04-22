package ra.edu.validate;

import java.math.BigDecimal;
import java.time.LocalDate;

public class JobPositionValidator {

    private static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Tên vị trí không được để trống.");
            return false;
        }
        if (name.length() > 100) {
            System.out.println("Tên vị trí không được vượt quá 100 ký tự.");
            return false;
        }
        return true;
    }

    public static boolean isValidSalaryRange(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            System.out.println("Lương tối thiểu và tối đa không được để trống.");
            return false;
        }
        if (min.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Lương tối thiểu phải lớn hơn hoặc bằng 0.");
            return false;
        }
        if (max.compareTo(min) < 0) {
            System.out.println("Lương tối đa phải lớn hơn hoặc bằng lương tối thiểu.");
            return false;
        }
        return true;
    }

    public static boolean isValidExperience(int exp) {
        if (exp < 0) {
            System.out.println("Kinh nghiệm tối thiểu phải lớn hơn hoặc bằng 0.");
            return false;
        }
        return true;
    }

    public static boolean isValidDates(LocalDate createdStr, String expiredStr) {
        if (expiredStr == null || !expiredStr.matches(DATE_REGEX)) {
            System.out.println("Ngày hết hạn không đúng định dạng yyyy-MM-dd.");
            return false;
        }
        LocalDate created;
        LocalDate expired;
        try {
            created = createdStr;
            expired = LocalDate.parse(expiredStr);
        } catch (Exception e) {
            System.out.println("Ngày nhập không hợp lệ.");
            return false;
        }
        if (!expired.isAfter(created)) {
            System.out.println("Ngày hết hạn phải sau ngày đăng.");
            return false;
        }
        return true;
    }

}

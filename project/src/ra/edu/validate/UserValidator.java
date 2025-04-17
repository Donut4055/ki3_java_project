package ra.edu.validate;

public class UserValidator {

    public static boolean isValidUsername(String username) {
        return Validator.isNotEmpty(username) && username.length() >= 4;
    }

    public static boolean isValidPassword(String password) {
        return Validator.isNotEmpty(password) && password.length() >= 6;
    }

    public static boolean isValidUserInfo(String username, String password, String email, String phone) {
        return isValidUsername(username) &&
                isValidPassword(password) &&
                Validator.isValidEmail(email) &&
                Validator.isValidPhone(phone);
    }
}

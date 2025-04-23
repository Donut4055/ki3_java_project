package ra.edu.presentation;

import ra.edu.business.service.account.AccountServiceImpl;
import ra.edu.business.dao.account.AccountDAOImpl;
import ra.edu.validate.UserValidator;
import ra.edu.validate.Validator;
import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

public class RegisterUI {
    private static final AccountServiceImpl accountService = new AccountServiceImpl(new AccountDAOImpl());

    public static void displayRegisterMenu() {
        System.out.println("========== ĐĂNG KÝ TÀI KHOẢN ỨNG VIÊN ==========");
        String username = readNonEmptyString("Nhập tên đăng nhập: ");
        if (!UserValidator.isValidUsername(username)) {
            System.out.println(">>> Tên đăng nhập không hợp lệ (tối thiểu 4 ký tự).");
            return;
        }

        String password = readNonEmptyString("Nhập mật khẩu: ");
        if (!UserValidator.isValidPassword(password)) {
            System.out.println(">>> Mật khẩu phải có ít nhất 6 ký tự.");
            return;
        }

        String name = readNonEmptyString("Nhập tên ứng viên: ");

        String email = readNonEmptyString("Nhập email: ");
        if (!Validator.isValidEmail(email)) {
            System.out.println(">>> Email không đúng định dạng.");
            return;
        }

        String phone = readNonEmptyString("Nhập số điện thoại: ");
        if (!Validator.isValidPhone(phone)) {
            System.out.println(">>> Số điện thoại không hợp lệ.");
            return;
        }

        String gender = readNonEmptyString("Nhập giới tính (Nam/Nữ): ");
        if (!gender.equalsIgnoreCase("Nam") && !gender.equalsIgnoreCase("Nữ")) {
            System.out.println(">>> Giới tính phải là 'Nam' hoặc 'Nữ'.");
            return;
        }

        String dob = readNonEmptyString("Nhập ngày sinh (yyyy-MM-dd): ");
        if (!Validator.isValidDate(dob)) {
            System.out.println(">>> Ngày sinh không đúng định dạng yyyy-MM-dd.");
            return;
        }

        String description = readNonEmptyString("Nhập mô tả về ứng viên: ");
        if (description.trim().isEmpty()) {
            System.out.println(">>> Mô tả không được để trống.");
            return;
        }

        int experience = readInt("Nhập kinh nghiệm (số năm, >=0): ");
        if (experience < 0) {
            System.out.println(">>> Kinh nghiệm phải lớn hơn hoặc bằng 0.");
            return;
        }

        boolean isRegistered = accountService.registerUser(
                username, password, name, email, phone, gender, dob, description, experience
        );
        if (isRegistered) {
            System.out.println("Đăng ký tài khoản thành công!");
        } else {
            System.out.println("Đăng ký thất bại. Tên đăng nhập có thể đã tồn tại hoặc có lỗi xảy ra.");
        }
    }
}

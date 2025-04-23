package ra.edu.presentation.user;

import ra.edu.business.service.user.personalInfo.IPersonalIInfoService;
import ra.edu.business.service.user.personalInfo.IPersonalIInfoServiceImpl;
import ra.edu.validate.UserValidator;
import ra.edu.validate.Validator;
import ra.edu.MainApplication;

import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

public class PersonalInfoUI {
    private static final IPersonalIInfoService accountService = new IPersonalIInfoServiceImpl();
    private static int candidateId;

    public static void showMenu(int loggedInCandidateId) {
        candidateId = loggedInCandidateId;
        while (true) {
            System.out.println("\n===== THÔNG TIN CÁ NHÂN =====");
            System.out.println("1. Cập nhật thông tin");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("0. Quay lại");
            int choice = readInt("Chọn: ");
            switch (choice) {
                case 1:
                    updateProfile();
                    break;
                case 2:
                    changePassword();
                    break;
                case 0:
                    System.out.println(">>> Quay lại menu.");
                    return;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void updateProfile() {
        String name;
        do {
            name = readNonEmptyString("Tên mới: ");
            if (!UserValidator.isValidUsername(name)) {
                System.out.println(">>> Tên không hợp lệ (tối thiểu 4 ký tự).");
            }
        } while (!UserValidator.isValidUsername(name));

        String email;
        do {
            email = readNonEmptyString("Email mới: ");
            if (!Validator.isValidEmail(email)) {
                System.out.println(">>> Email không đúng định dạng.");
            }
        } while (!Validator.isValidEmail(email));

        String phone;
        do {
            phone = readNonEmptyString("SĐT mới: ");
            if (!Validator.isValidPhone(phone)) {
                System.out.println(">>> Số điện thoại không hợp lệ.");
            }
        } while (!Validator.isValidPhone(phone));

        String gender;
        do {
            gender = readNonEmptyString("Giới tính (Nam/Nữ): ");
            if (!gender.equalsIgnoreCase("Nam") && !gender.equalsIgnoreCase("Nữ")) {
                System.out.println(">>> Giới tính phải là 'Nam' hoặc 'Nữ'.");
            }
        } while (!gender.equalsIgnoreCase("Nam") && !gender.equalsIgnoreCase("Nữ"));

        String dob;
        do {
            dob = readNonEmptyString("Ngày sinh (yyyy-MM-dd): ");
            if (!Validator.isValidDate(dob)) {
                System.out.println(">>> Ngày sinh không đúng định dạng yyyy-MM-dd.");
            }
        } while (!Validator.isValidDate(dob));

        String desc;
        do {
            desc = readNonEmptyString("Giới thiệu bản thân: ");
            if (desc.trim().isEmpty()) {
                System.out.println(">>> Mô tả không được để trống.");
            }
        } while (desc.trim().isEmpty());

        boolean ok = accountService.updateUserProfile(
                candidateId,
                name, phone, gender, dob, desc, email
        );
        System.out.println(ok
                ? ">>> Cập nhật thông tin thành công."
                : ">>> Lỗi khi cập nhật thông tin.");
    }

    private static void changePassword() {
        while (true) {
            String currentPass = readNonEmptyString("Mật khẩu hiện tại: ");
            String newPass = readNonEmptyString("Mật khẩu mới: ");
            if (!UserValidator.isValidPassword(newPass)) {
                System.out.println(">>> Mật khẩu mới phải có ít nhất 6 ký tự.");
                continue;
            }

            boolean ok = accountService.changePassword(candidateId, currentPass, newPass);
            if (ok) {
                System.out.println(">>> Đổi mật khẩu thành công.");
                break;
            } else {
                System.out.println(">>> Mật khẩu hiện tại không đúng hoặc lỗi khi đổi. Vui lòng thử lại.");
            }
        }
    }
}

package ra.edu.presentation.user;

import ra.edu.business.service.user.personalInfo.IPersonalIInfoService;
import ra.edu.business.service.user.personalInfo.IPersonalIInfoServiceImpl;
import ra.edu.validate.UserValidator;
import ra.edu.validate.Validator;

import java.util.Scanner;

public class PersonalInfoUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final IPersonalIInfoService accountService = new IPersonalIInfoServiceImpl();
    private static int candidateId;

    public static void showMenu(int loggedInCandidateId) {
        candidateId = loggedInCandidateId;
        int choice;
        do {
            System.out.println("\n===== THÔNG TIN CÁ NHÂN =====");
            System.out.println("1. Cập nhật thông tin");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    updateProfile();
                    break;
                case 2:
                    changePassword();
                    break;
                case 0:
                    System.out.println(">>> Quay lại menu.");
                    break;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }

    private static void updateProfile() {
        System.out.print("Tên mới: ");
        String name = scanner.nextLine();
        if (!UserValidator.isValidUsername(name)) {
            System.out.println(">>> Tên không hợp lệ.");
            return;
        }

        System.out.print("Email mới: ");
        String email = scanner.nextLine();
        if (!Validator.isValidEmail(email)) {
            System.out.println(">>> Email không đúng định dạng.");
            return;
        }

        System.out.print("SĐT mới: ");
        String phone = scanner.nextLine();
        if (!Validator.isValidPhone(phone)) {
            System.out.println(">>> Số điện thoại không hợp lệ.");
            return;
        }

        System.out.print("Giới tính (Nam/Nữ): ");
        String gender = scanner.nextLine();
        if (!gender.equalsIgnoreCase("Nam") && !gender.equalsIgnoreCase("Nữ")) {
            System.out.println(">>> Giới tính phải là 'Nam' hoặc 'Nữ'.");
            return;
        }

        System.out.print("Ngày sinh (yyyy-MM-dd): ");
        String dob = scanner.nextLine();
        if (!Validator.isValidDate(dob)) {
            System.out.println(">>> Ngày sinh không đúng định dạng yyyy-MM-dd.");
            return;
        }

        System.out.print("Giới thiệu bản thân: ");
        String desc = scanner.nextLine();
        if (desc == null || desc.trim().isEmpty()) {
            System.out.println(">>> Mô tả không được để trống.");
            return;
        }

        boolean ok = accountService.updateUserProfile(candidateId, name, phone, gender, dob, desc, email);
        System.out.println(ok ? ">>> Cập nhật thành công." : ">>> Lỗi khi cập nhật.");
    }

    private static void changePassword() {
        System.out.print("Mật khẩu hiện tại: ");
        String currentPass = scanner.nextLine();

        System.out.print("Mật khẩu mới: ");
        String newPass = scanner.nextLine();
        if (!UserValidator.isValidPassword(newPass)) {
            System.out.println(">>> Mật khẩu mới phải ≥ 6 ký tự.");
            return;
        }

        boolean ok = accountService.changePassword(candidateId, currentPass, newPass);
        if (ok) {
            System.out.println(">>> Đổi mật khẩu thành công.");
        } else {
            System.out.println(">>> Mật khẩu hiện tại không đúng hoặc lỗi khi đổi mật khẩu.");
        }
    }
}
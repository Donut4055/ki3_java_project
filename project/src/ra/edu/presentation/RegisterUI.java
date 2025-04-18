package ra.edu.presentation;

import ra.edu.business.service.account.AccountServiceImpl;
import ra.edu.business.dao.account.AccountDAOImpl;

import java.util.Scanner;

import static java.lang.System.exit;

public class RegisterUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AccountServiceImpl accountService = new AccountServiceImpl(new AccountDAOImpl());

    public static void displayRegisterMenu() {
        System.out.println("========== ĐĂNG KÝ TÀI KHOẢN ỨNG VIÊN ==========");
        System.out.print("Nhập tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine();
        System.out.print("Nhập tên ứng viên: ");
        String name = scanner.nextLine();
        System.out.print("Nhập email: ");
        String email = scanner.nextLine();
        System.out.print("Nhập số điện thoại: ");
        String phone = scanner.nextLine();
        System.out.print("Nhập giới tính: ");
        String gender = scanner.nextLine();
        System.out.print("Nhập ngày sinh (yyyy-mm-dd): ");
        String dob = scanner.nextLine();
        System.out.print("Nhập mô tả về ứng viên: ");
        String description = scanner.nextLine();

        System.out.print("Nhập kinh nghiệm: ");
        int experience = Integer.parseInt(scanner.nextLine());

        boolean isRegistered = accountService.registerUser(
                username, password, name, email, phone, gender, dob, description, experience
        );
        if (isRegistered) {
            System.out.println("Đăng ký tài khoản thành công!");

        } else {
            System.out.println("Đăng ký thất bại. Tên đăng nhập đã tồn tại hoặc có lỗi xảy ra.");
        }
    }
}

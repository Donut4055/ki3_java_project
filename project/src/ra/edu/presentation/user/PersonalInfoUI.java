package ra.edu.presentation.user;

import java.util.Scanner;

public class PersonalInfoUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU QUẢN LÝ THÔNG TIN CÁ NHÂN =====");
            System.out.println("1. Cập nhật thông tin cá nhân");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("0. Quay về menu chính");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    updatePersonalInfo();
                    break;
                case 2:
                    changePassword();
                    break;
                case 0:
                    System.out.println(">>> Quay về menu chính.");
                    break;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }

    private static void updatePersonalInfo() {
        System.out.println("Cập nhật thông tin cá nhân...");
    }

    private static void changePassword() {
        System.out.println("Đổi mật khẩu: ");
        System.out.print("Nhập mật khẩu mới: ");
        String newPassword = scanner.nextLine();
        System.out.println("Xác thực qua email/số điện thoại...");
    }
}

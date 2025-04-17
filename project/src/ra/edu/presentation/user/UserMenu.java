package ra.edu.presentation.user;

import java.util.Scanner;

public class UserMenu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU ỨNG VIÊN =====");
            System.out.println("1. Quản lý Thông tin cá nhân");
            System.out.println("2. Xem và Nộp đơn tuyển dụng");
            System.out.println("3. Quản lý đơn ứng tuyển");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    PersonalInfoUI.showMenu();
                    break;
                case 2:
                    RecruitmentApplicationUI.showMenu();
                    break;
                case 3:
                    ApplicationManagementUI.showMenu();
                    break;
                case 0:
                    System.out.println(">>> Đăng xuất thành công.");
                    break;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }
}

package ra.edu.presentation;

import java.util.Scanner;

public class UserMenu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU ỨNG VIÊN =====");
            System.out.println("1. Xem các vị trí tuyển dụng");
            System.out.println("2. Xem hồ sơ cá nhân");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
//                    RecruitmentPositionUI.viewAll(); // hiển thị danh sách vị trí đang tuyển
                    break;
                case 2:
//                    CandidateUI.viewProfile(); // hiển thị profile của ứng viên
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

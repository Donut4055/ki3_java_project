package ra.edu.presentation.user;

import java.util.Scanner;

public class RecruitmentApplicationUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU XEM VÀ NỘP ĐƠN TUYỂN DỤNG =====");
            System.out.println("1. Xem danh sách vị trí đang hoạt động");
            System.out.println("2. Xem chi tiết vị trí và nộp đơn");
            System.out.println("0. Quay về menu chính");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewActivePositions();
                    break;
                case 2:
                    applyForPosition();
                    break;
                case 0:
                    System.out.println(">>> Quay về menu chính.");
                    break;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }

    private static void viewActivePositions() {
        System.out.println("Danh sách các vị trí tuyển dụng đang hoạt động...");
    }

    private static void applyForPosition() {
        System.out.print("Nhập URL CV của bạn: ");
        String cvUrl = scanner.nextLine();
        System.out.println("Đã nộp đơn với CV: " + cvUrl);
    }
}

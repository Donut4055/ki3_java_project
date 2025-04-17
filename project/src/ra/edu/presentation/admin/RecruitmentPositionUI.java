package ra.edu.presentation.admin;

import java.util.Scanner;

public class RecruitmentPositionUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU QUẢN LÝ VỊ TRÍ TUYỂN DỤNG =====");
            System.out.println("1. Thêm vị trí tuyển dụng mới");
            System.out.println("2. Cập nhật vị trí tuyển dụng");
            System.out.println("3. Xóa vị trí tuyển dụng");
            System.out.println("4. Xem danh sách vị trí tuyển dụng đang hoạt động");
            System.out.println("0. Quay về menu chính");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Thêm vị trí tuyển dụng mới...");
                    break;
                case 2:
                    System.out.println("Cập nhật vị trí tuyển dụng...");
                    break;
                case 3:
                    System.out.println("Xóa vị trí tuyển dụng...");
                    break;
                case 4:
                    System.out.println("Danh sách vị trí tuyển dụng đang hoạt động...");
                    break;
                case 0:
                    System.out.println(">>> Quay về menu chính.");
                    break;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }
}

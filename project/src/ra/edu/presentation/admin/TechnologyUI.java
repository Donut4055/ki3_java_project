package ra.edu.presentation.admin;

import java.util.Scanner;

public class TechnologyUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU QUẢN LÝ CÔNG NGHỆ =====");
            System.out.println("1. Xem danh sách công nghệ");
            System.out.println("2. Thêm công nghệ mới");
            System.out.println("3. Sửa công nghệ");
            System.out.println("4. Xóa công nghệ");
            System.out.println("0. Quay về menu chính");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Danh sách công nghệ: (ví dụ)");
                    break;
                case 2:
                    System.out.println("Thêm công nghệ mới...");
                    break;
                case 3:
                    System.out.println("Sửa công nghệ...");
                    break;
                case 4:
                    System.out.println("Xóa công nghệ...");
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

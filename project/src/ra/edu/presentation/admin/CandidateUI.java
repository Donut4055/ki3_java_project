package ra.edu.presentation.admin;

import java.util.Scanner;

public class CandidateUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU QUẢN LÝ ỨNG VIÊN =====");
            System.out.println("1. Hiển thị danh sách ứng viên");
            System.out.println("2. Khóa/Mở khóa tài khoản ứng viên");
            System.out.println("3. Reset mật khẩu ứng viên");
            System.out.println("4. Tìm kiếm ứng viên theo tên");
            System.out.println("5. Lọc theo kinh nghiệm, tuổi, giới tính, công nghệ");
            System.out.println("0. Quay về menu chính");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Danh sách ứng viên...");
                    break;
                case 2:
                    System.out.println("Khóa/Mở khóa tài khoản ứng viên...");
                    break;
                case 3:
                    System.out.println("Reset mật khẩu ứng viên...");
                    break;
                case 4:
                    System.out.println("Tìm kiếm ứng viên theo tên...");
                    break;
                case 5:
                    System.out.println("Lọc ứng viên theo các tiêu chí...");
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

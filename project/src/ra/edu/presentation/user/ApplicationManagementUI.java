package ra.edu.presentation.user;

import java.util.Scanner;

public class ApplicationManagementUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU QUẢN LÝ ĐƠN ỨNG TUYỂN =====");
            System.out.println("1. Xem danh sách đơn đã nộp");
            System.out.println("2. Xem chi tiết đơn");
            System.out.println("0. Quay về menu chính");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewSubmittedApplications();
                    break;
                case 2:
                    viewApplicationDetails();
                    break;
                case 0:
                    System.out.println(">>> Quay về menu chính.");
                    break;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }

    private static void viewSubmittedApplications() {
        System.out.println("Danh sách đơn ứng tuyển đã nộp...");
        // Hiển thị danh sách các đơn ứng tuyển
    }

    private static void viewApplicationDetails() {
        System.out.print("Nhập ID đơn ứng tuyển muốn xem: ");
        String applicationId = scanner.nextLine();
        System.out.println("Chi tiết đơn ứng tuyển với ID: " + applicationId);
        // Hiển thị chi tiết đơn ứng tuyển
        // Nếu đơn ở trạng thái interviewing, hiển thị thông tin tham gia phòng và thời gian
        if (isInterviewing(applicationId)) {
            System.out.println("Thông tin phỏng vấn: Phòng XYZ, Thời gian: 10:00 AM");
        }
    }

    private static boolean isInterviewing(String applicationId) {
        // Kiểm tra xem đơn có trạng thái interviewing hay không
        return true; // Giả sử đây là đơn ứng tuyển đang ở trạng thái interviewing
    }
}

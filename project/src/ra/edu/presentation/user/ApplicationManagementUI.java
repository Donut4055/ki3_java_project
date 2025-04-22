package ra.edu.presentation.user;

import ra.edu.MainApplication;
import ra.edu.business.model.Application;
import ra.edu.business.service.user.application.ApplicationServiceImpl;
import ra.edu.business.service.user.application.IApplicationService;

import java.util.List;
import java.util.Scanner;

public class ApplicationManagementUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final IApplicationService service = new ApplicationServiceImpl();

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== QUẢN LÝ ĐƠN ỨNG TUYỂN =====");
            System.out.println("1. Danh sách đơn đã nộp");
            System.out.println("2. Xem chi tiết đơn");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.print("Nhập trang: ");
                    int page = Integer.parseInt(scanner.nextLine());
                    System.out.print("Số đơn/trang: ");
                    int size = Integer.parseInt(scanner.nextLine());
                    int cid = MainApplication.currentUser.getId();
                    List<Application> list = service.getSubmittedApplications(cid, page, size);
                    System.out.println("---- DANH SÁCH ĐƠN ĐÃ NỘP ----");
                    for (Application app : list) {
                        System.out.printf("ID:%d | Vị trí:%d | Tiến độ:%s | Ngày tạo:%s\n",
                                app.getId(), app.getRecruitmentPositionId(), app.getProgress(), app.getCreateAt());
                    }
                    break;
                case 2:
                    System.out.print("Nhập ID đơn muốn xem: ");
                    int aid = Integer.parseInt(scanner.nextLine());
                    Application detail = service.getApplicationDetails(aid);
                    if (detail != null) {
                        System.out.println(detail);
                    } else {
                        System.out.println(">>> Không tìm thấy đơn.");
                    }
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
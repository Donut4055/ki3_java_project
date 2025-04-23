package ra.edu.presentation.user;


import ra.edu.business.model.Application;
import ra.edu.business.service.user.application.IApplicationService;
import ra.edu.business.service.user.application.ApplicationServiceImpl;
import ra.edu.MainApplication;
import static ra.edu.utils.InputUtils.readInt;

import java.util.List;

public class ApplicationManagementUI {
    private static final IApplicationService service = new ApplicationServiceImpl();

    public static void showMenu() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ ĐƠN ĐÃ NỘP =====");
            System.out.println("1. Danh sách đơn đã nộp");
            System.out.println("2. Xem chi tiết đơn");
            System.out.println("0. Quay lại");
            int choice = readInt("Chọn: ");
            switch (choice) {
                case 1:
                    listSubmitted();
                    break;
                case 2:
                    viewSubmittedDetails();
                    break;
                case 0:
                    return;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void listSubmitted() {
        int page = readInt("Nhập trang (>=1): ");
        int size = readInt("Số đơn/trang (>=1): ");
        int cid = MainApplication.currentUser.getId();
        List<Application> list = service.getSubmittedApplications(cid, page, size);
        System.out.println("---- DANH SÁCH ĐƠN ĐÃ NỘP ----");
        list.forEach(app -> System.out.printf(
                "ID:%d | Vị trí:%d | Trạng thái:%s | Ngày tạo:%s%n",
                app.getId(), app.getRecruitmentPositionId(), app.getProgress(), app.getCreateAt()));
    }

    private static void viewSubmittedDetails() {
        int aid = readInt("Nhập ID đơn muốn xem: ");
        Application app = service.getApplicationDetails(aid);
        if (app == null) {
            System.out.println(">>> Không tìm thấy đơn.");
            return;
        }
        System.out.println(app);
        if ("interviewing".equalsIgnoreCase(app.getProgress()) && app.getInterviewRequestDate() != null) {
            System.out.println("1. Xác nhận tham gia phỏng vấn");
            System.out.println("2. Từ chối phỏng vấn");
            int opt = readInt("Chọn: ");
            switch (opt) {
                case 1:
                    app.setInterviewRequestResult("Confirmed");
                    System.out.println(">>> Bạn đã xác nhận tham gia phỏng vấn.");
                    break;
                case 2:
                    app.setInterviewRequestResult("Declined");
                    System.out.println(">>> Bạn đã từ chối phỏng vấn.");
                    break;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }
}
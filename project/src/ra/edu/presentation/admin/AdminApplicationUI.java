package ra.edu.presentation.admin;


import ra.edu.business.model.Application;
import ra.edu.business.service.admin.AdminApplication.AdminApplicationServiceImpl;
import ra.edu.business.service.admin.AdminApplication.IAdminApplicationService;

import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class AdminApplicationUI {
    private static final IAdminApplicationService service = new AdminApplicationServiceImpl();

    public static void showMenu() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ ĐƠN ỨNG TUYỂN (ADMIN) =====");
            System.out.println("1. Danh sách đơn (chưa huỷ)");
            System.out.println("2. Lọc theo trạng thái");
            System.out.println("3. Lọc theo kết quả");
            System.out.println("4. Xem chi tiết đơn");
            System.out.println("5. Huỷ đơn");
            System.out.println("6. Chuyển sang interviewing");
            System.out.println("7. Cập nhật kết quả");
            System.out.println("0. Quay về menu chính");
            int choice = readInt("Chọn: ");
            switch (choice) {
                case 1: listApplications();       break;
                case 2: filterByProgress();      break;
                case 3: filterByResult();        break;
                case 4: viewDetails();           break;
                case 5: cancelApplication();     break;
                case 6: scheduleInterview();     break;
                case 7: updateInterviewResult(); break;
                case 0: return;
                default: System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void listApplications() {
        int page;
        do {
            page = readInt("Trang (>=1): ");
            if (page < 1) System.out.println(">>> Trang phải ≥ 1!");
        } while (page < 1);

        int size;
        do {
            size = readInt("Số bản ghi/trang (>=1): ");
            if (size < 1) System.out.println(">>> Phải ≥ 1!");
        } while (size < 1);

        List<Application> apps = service.listApplications(page, size);
        apps.forEach(System.out::println);
    }

    private static void filterByProgress() {
        String[] valid = {"pending", "handling", "interviewing", "done"};
        String progress;
        do {
            progress = readNonEmptyString("Trạng thái (pending/handling/interviewing/done): ");
        } while (!Arrays.stream(valid).anyMatch(progress::equalsIgnoreCase));

        int page = readInt("Trang: ");
        int size = readInt("Size: ");
        service.filterByProgress(progress, page, size)
                .forEach(System.out::println);
    }

    private static void filterByResult() {
        String[] valid = {"pass", "fail"};
        String result;
        do {
            result = readNonEmptyString("Kết quả (pass/fail): ");
        } while (!Arrays.stream(valid).anyMatch(result::equalsIgnoreCase));

        int page = readInt("Trang: ");
        int size = readInt("Size: ");
        service.filterByResult(result, page, size)
                .forEach(System.out::println);
    }

    private static void viewDetails() {
        int id = readInt("ID đơn muốn xem: ");
        Application app = service.viewApplication(id);
        System.out.println(app != null ? app : ">>> Không tìm thấy đơn.");
    }

    private static void cancelApplication() {
        int id = readInt("ID đơn huỷ: ");
        String reason = readNonEmptyString("Lý do huỷ: ");
        boolean ok = service.cancelApplication(id, reason);
        System.out.println(ok ? ">>> Huỷ thành công." : ">>> Lỗi khi huỷ đơn.");
    }

    private static void scheduleInterview() {
        int id = readInt("ID đơn chuyển interviewing: ");
        String link = readNonEmptyString("Link phỏng vấn: ");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dt;
        while (true) {
            String input = readNonEmptyString("Thời gian (YYYY-MM-DDTHH:MM): ");
            try {
                dt = LocalDateTime.parse(input, fmt);
                if (dt.isBefore(LocalDateTime.now())) {
                    System.out.println(">>> Thời gian phải sau thời điểm hiện tại.");
                    continue;
                }
                break;
            } catch (DateTimeParseException e) {
                System.out.println(">>> Sai định dạng, phải là YYYY-MM-DDTHH:MM, ví dụ: 2025-04-23T15:30");
            }
        }
        Timestamp time = Timestamp.valueOf(dt);
        boolean ok = service.setInterview(id, link, time);
        System.out.println(ok
                ? ">>> Thiết lập phỏng vấn thành công."
                : ">>> Lỗi khi cập nhật.");
    }


    private static void updateInterviewResult() {
        int id = readInt("ID đơn cập nhật kết quả: ");
        String[] valid = {"pass", "fail"};
        String result;
        do {
            result = readNonEmptyString("Kết quả (pass/fail): ");
        } while (!Arrays.stream(valid).anyMatch(result::equalsIgnoreCase));

        String note = readNonEmptyString("Ghi chú kết quả: ");
        boolean ok = service.updateResult(id, result, note);
        System.out.println(ok ? ">>> Cập nhật kết quả thành công." : ">>> Lỗi khi cập nhật.");
    }
}


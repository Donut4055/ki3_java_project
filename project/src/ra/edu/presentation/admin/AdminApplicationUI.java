package ra.edu.presentation.admin;

import ra.edu.business.model.Application;
import ra.edu.business.service.admin.AdminApplication.IAdminApplicationService;
import ra.edu.business.service.admin.AdminApplication.AdminApplicationServiceImpl;
import ra.edu.utils.DataFormatter;

import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class AdminApplicationUI {
    private static final IAdminApplicationService service = new AdminApplicationServiceImpl();
    private static final int PAGE_SIZE = 10;

    public static void showMenu() {
        while (true) {
            try {
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
            } catch (Exception e) {
                System.out.println(">>> Đã xảy ra lỗi: " + e.getMessage());
            }
        }
    }

    private static void listApplications() {
        try {
            String[] headers = {
                    "ID","CandidateID","PositionID","CV URL","Progress",
                    "ReqDate","ReqResult","Link","InterviewTime",
                    "Result","ResultNote","DestroyedAt","CreatedAt","UpdatedAt","DestroyReason"
            };
            BiFunction<Integer,Integer,List<Application>> fetchPage =
                    (page, size) -> service.listApplications(page, size);
            IntSupplier totalCount = service::countApplications;
            Function<Application,String[]> mapper = a -> new String[]{
                    String.valueOf(a.getId()),
                    String.valueOf(a.getCandidateId()),
                    String.valueOf(a.getRecruitmentPositionId()),
                    a.getCvUrl(),
                    a.getProgress(),
                    String.valueOf(a.getInterviewRequestDate()),
                    a.getInterviewRequestResult(),
                    a.getInterviewLink(),
                    String.valueOf(a.getInterviewTime()),
                    a.getInterviewResult(),
                    a.getInterviewResultNote(),
                    String.valueOf(a.getDestroyAt()),
                    String.valueOf(a.getCreateAt()),
                    String.valueOf(a.getUpdateAt()),
                    a.getDestroyReason()
            };

            DataFormatter.printInteractiveTable(
                    headers,
                    fetchPage,
                    totalCount,
                    mapper,
                    PAGE_SIZE
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi liệt kê đơn: " + e.getMessage());
        }
    }

    private static void filterByProgress() {
        try {
            String[] valid = {"pending","handling","interviewing","done"};
            String progress;
            do {
                progress = readNonEmptyString("Trạng thái (pending/handling/interviewing/done): ");
            } while (!Arrays.stream(valid).anyMatch(progress::equalsIgnoreCase));

            String[] headers = {"ID","CandidateID","PositionID","Progress","CreatedAt"};
            List<Application> apps = service.filterByProgress(progress, 1, service.countApplications());
            DataFormatter.printTable(
                    headers,
                    apps,
                    a -> new String[]{
                            String.valueOf(a.getId()),
                            String.valueOf(a.getCandidateId()),
                            String.valueOf(a.getRecruitmentPositionId()),
                            a.getProgress(),
                            String.valueOf(a.getCreateAt())
                    }
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi lọc theo trạng thái: " + e.getMessage());
        }
    }

    private static void filterByResult() {
        try {
            String[] valid = {"pass","fail"};
            String result;
            do {
                result = readNonEmptyString("Kết quả (pass/fail): ");
            } while (!Arrays.stream(valid).anyMatch(result::equalsIgnoreCase));

            String[] headers = {"ID","CandidateID","PositionID","InterviewResult","UpdatedAt"};
            List<Application> apps = service.filterByResult(result, 1, service.countApplications());
            DataFormatter.printTable(
                    headers,
                    apps,
                    a -> new String[]{
                            String.valueOf(a.getId()),
                            String.valueOf(a.getCandidateId()),
                            String.valueOf(a.getRecruitmentPositionId()),
                            a.getInterviewResult(),
                            String.valueOf(a.getUpdateAt())
                    }
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi lọc theo kết quả: " + e.getMessage());
        }
    }

    private static void viewDetails() {
        try {
            int id = readInt("ID đơn muốn xem: ");
            Application app = service.viewApplication(id);
            if (app == null) {
                System.out.println(">>> Không tìm thấy đơn.");
                return;
            }

            String[] headers = {
                    "ID","CandidateID","PositionID","CV URL","Progress",
                    "ReqDate","ReqResult","Link","InterviewTime",
                    "Result","ResultNote","DestroyedAt","CreatedAt","UpdatedAt","DestroyReason"
            };
            DataFormatter.printTable(
                    headers,
                    List.of(app),
                    a -> new String[]{
                            String.valueOf(a.getId()),
                            String.valueOf(a.getCandidateId()),
                            String.valueOf(a.getRecruitmentPositionId()),
                            a.getCvUrl(),
                            a.getProgress(),
                            String.valueOf(a.getInterviewRequestDate()),
                            a.getInterviewRequestResult(),
                            a.getInterviewLink(),
                            String.valueOf(a.getInterviewTime()),
                            a.getInterviewResult(),
                            a.getInterviewResultNote(),
                            String.valueOf(a.getDestroyAt()),
                            String.valueOf(a.getCreateAt()),
                            String.valueOf(a.getUpdateAt()),
                            a.getDestroyReason()
                    }
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi xem chi tiết đơn: " + e.getMessage());
        }
    }

    private static void cancelApplication() {
        try {
            int id = readInt("ID đơn huỷ: ");
            String reason = readNonEmptyString("Lý do huỷ: ");
            boolean ok = service.cancelApplication(id, reason);
            System.out.println(ok ? ">>> Huỷ thành công." : ">>> Lỗi khi huỷ đơn.");
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi huỷ đơn: " + e.getMessage());
        }
    }

    private static void scheduleInterview() {
        try {
            int id = readInt("ID đơn chuyển interviewing: ");
            String link = readNonEmptyString("Link phỏng vấn: ");

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dt;
            while (true) {
                try {
                    String input = readNonEmptyString("Thời gian (YYYY-MM-DDTHH:MM): ");
                    dt = LocalDateTime.parse(input, fmt);
                    if (dt.isBefore(LocalDateTime.now())) {
                        System.out.println(">>> Thời gian phải sau thời điểm hiện tại.");
                        continue;
                    }
                    break;
                } catch (DateTimeParseException ex) {
                    System.out.println(">>> Sai định dạng, phải là YYYY-MM-DDTHH:MM");
                }
            }
            Timestamp time = Timestamp.valueOf(dt);
            boolean ok = service.setInterview(id, link, time);
            System.out.println(ok
                    ? ">>> Thiết lập phỏng vấn thành công."
                    : ">>> Lỗi khi cập nhật.");
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi lên lịch phỏng vấn: " + e.getMessage());
        }
    }

    private static void updateInterviewResult() {
        try {
            int id = readInt("ID đơn cập nhật kết quả: ");
            String[] valid = {"pass","fail"};
            String result;
            do {
                result = readNonEmptyString("Kết quả (pass/fail): ");
            } while (!Arrays.stream(valid).anyMatch(result::equalsIgnoreCase));

            String note = readNonEmptyString("Ghi chú kết quả: ");
            boolean ok = service.updateResult(id, result, note);
            System.out.println(ok
                    ? ">>> Cập nhật kết quả thành công."
                    : ">>> Lỗi khi cập nhật.");
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi cập nhật kết quả: " + e.getMessage());
        }
    }
}

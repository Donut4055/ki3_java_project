package ra.edu.presentation.user;

import ra.edu.business.model.Application;
import ra.edu.business.service.user.application.IApplicationService;
import ra.edu.business.service.user.application.ApplicationServiceImpl;
import ra.edu.MainApplication;
import ra.edu.utils.DataFormatter;

import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class ApplicationManagementUI {
    private static final IApplicationService service = new ApplicationServiceImpl();
    private static final int PAGE_SIZE = 10;

    public static void showMenu() {
        while (true) {
            try {
                System.out.println("\n===== QUẢN LÝ ĐƠN ĐÃ NỘP =====");
                System.out.println("1. Danh sách đơn đã nộp");
                System.out.println("2. Xem chi tiết đơn");
                System.out.println("0. Quay lại");
                int choice = readInt("Chọn: ");
                switch (choice) {
                    case 1: listSubmitted();        break;
                    case 2: viewSubmittedDetails(); break;
                    case 0: return;
                    default: System.out.println(">>> Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) {
                System.out.println(">>> Đã xảy ra lỗi: " + e.getMessage());
            }
        }
    }

    private static void listSubmitted() {
        try {
            int candidateId = MainApplication.currentUser.getId();
            BiFunction<Integer,Integer,List<Application>> fetchPage =
                    (page, size) -> service.getSubmittedApplications(candidateId, page, size);
            IntSupplier totalCount = () -> service.countSubmittedApplications(candidateId);

            String[] headers = { "ID", "PositionID", "Progress", "CreateAt" };
            Function<Application,String[]> mapper = app -> new String[]{
                    String.valueOf(app.getId()),
                    String.valueOf(app.getRecruitmentPositionId()),
                    app.getProgress(),
                    app.getCreateAt().toString()
            };

            DataFormatter.printInteractiveTable(
                    headers, fetchPage, totalCount, mapper, PAGE_SIZE
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi liệt kê đơn đã nộp: " + e.getMessage());
        }
    }

    private static void viewSubmittedDetails() {
        try {
            int appId = readInt("Nhập ID đơn muốn xem: ");
            Application app = service.getApplicationDetails(appId);
            if (app == null) {
                System.out.println(">>> Không tìm thấy đơn.");
                return;
            }

            // In chi tiết theo cột Field / Value
            String[] hdr = { "Field", "Value" };
            List<String[]> rows = List.of(
                    new String[]{ "ID",            String.valueOf(app.getId()) },
                    new String[]{ "CandidateID",   String.valueOf(app.getCandidateId()) },
                    new String[]{ "PositionID",    String.valueOf(app.getRecruitmentPositionId()) },
                    new String[]{ "CV URL",        app.getCvUrl() },
                    new String[]{ "Progress",      app.getProgress() },
                    new String[]{ "CreateAt",      app.getCreateAt().toString() },
                    new String[]{ "ReqDate",       app.getInterviewRequestDate()   == null ? "" : app.getInterviewRequestDate().toString() },
                    new String[]{ "ReqResult",     app.getInterviewRequestResult()== null ? "" : app.getInterviewRequestResult() },
                    new String[]{ "InterviewLink", app.getInterviewLink()          == null ? "" : app.getInterviewLink() },
                    new String[]{ "InterviewTime", app.getInterviewTime()          == null ? "" : app.getInterviewTime().toString() },
                    new String[]{ "Result",        app.getInterviewResult()        == null ? "" : app.getInterviewResult() },
                    new String[]{ "ResultNote",    app.getInterviewResultNote()    == null ? "" : app.getInterviewResultNote() }
            );
            DataFormatter.printTable(hdr, rows, row -> row);

            // Nếu đang interviewing và đã có requestDate, cho xác nhận phỏng vấn
            if ("interviewing".equalsIgnoreCase(app.getProgress())
                    && app.getInterviewRequestDate() != null) {
                System.out.println("\n1. Xác nhận tham gia phỏng vấn");
                System.out.println("2. Từ chối phỏng vấn");
                int opt = readInt("Chọn: ");
                if (opt == 1 || opt == 2) {
                    boolean confirm = (opt == 1);
                    boolean ok = service.respondToInterview(appId, confirm);
                    if (ok) {
                        System.out.println(confirm
                                ? ">>> Bạn đã xác nhận tham gia phỏng vấn."
                                : ">>> Bạn đã từ chối phỏng vấn.");
                    } else {
                        System.out.println(">>> Lỗi khi phản hồi phỏng vấn.");
                    }
                } else {
                    System.out.println(">>> Lựa chọn không hợp lệ.");
                }
            }
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi xem chi tiết đơn: " + e.getMessage());
        }
    }
}

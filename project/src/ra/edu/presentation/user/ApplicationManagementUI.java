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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ApplicationManagementUI {
    private static final IApplicationService service = new ApplicationServiceImpl();
    private static final int PAGE_SIZE = 10;

    public static void showMenu() {
        while (true) {
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
        }
    }

    private static void listSubmitted() {
        int candidateId = MainApplication.currentUser.getId();
        // BiFunction(page, size) -> List<Application>
        BiFunction<Integer,Integer,List<Application>> fetchPage =
                (page, size) -> service.getSubmittedApplications(candidateId, page, size);
        // IntSupplier tổng số đơn để tính trang
        IntSupplier totalCount = () -> service.countSubmittedApplications(candidateId);

        String[] headers = { "ID", "PositionID", "Progress", "CreatedAt" };
        Function<Application,String[]> mapper = app -> new String[]{
                String.valueOf(app.getId()),
                String.valueOf(app.getRecruitmentPositionId()),
                app.getProgress(),
                app.getCreateAt().toString()
        };

        DataFormatter.printInteractiveTable(
                headers,
                fetchPage,
                totalCount,
                mapper,
                PAGE_SIZE
        );
    }

    private static void viewSubmittedDetails() {
        int appId = readInt("Nhập ID đơn muốn xem: ");
        Application app = service.getApplicationDetails(appId);
        if (app == null) {
            System.out.println(">>> Không tìm thấy đơn.");
            return;
        }

        // In chi tiết theo dạng cột Field/Value
        String[] hdr = { "Field", "Value" };
        List<String[]> rows = List.of(
                new String[]{ "ID", String.valueOf(app.getId()) },
                new String[]{ "CandidateID", String.valueOf(app.getCandidateId()) },
                new String[]{ "PositionID", String.valueOf(app.getRecruitmentPositionId()) },
                new String[]{ "CV URL", app.getCvUrl() },
                new String[]{ "Progress", app.getProgress() },
                new String[]{ "CreateAt", app.getCreateAt().toString() },
                new String[]{ "ReqDate", app.getInterviewRequestDate()   == null ? "" : app.getInterviewRequestDate().toString() },
                new String[]{ "ReqResult",    app.getInterviewRequestResult() == null ? "" : app.getInterviewRequestResult() },
                new String[]{ "InterviewLink",app.getInterviewLink()     == null ? "" : app.getInterviewLink() },
                new String[]{ "InterviewTime",app.getInterviewTime()     == null ? "" : app.getInterviewTime().toString() },
                new String[]{ "Result",       app.getInterviewResult()   == null ? "" : app.getInterviewResult() },
                new String[]{ "ResultNote",   app.getInterviewResultNote()== null ? "" : app.getInterviewResultNote() }
        );
        // Tận dụng DataFormatter để in
        DataFormatter.printTable(hdr, rows, row -> row);

        // Nếu đang interviewing và đã có yêu cầu, cho xác nhận
        if ("interviewing".equalsIgnoreCase(app.getProgress())
                && app.getInterviewRequestDate() != null) {
            System.out.println("\n1. Xác nhận tham gia phỏng vấn");
            System.out.println("2. Từ chối phỏng vấn");
            int opt = readInt("Chọn: ");
            switch (opt) {
                case 1:
                    service.respondToInterview(appId, true);
                    System.out.println(">>> Bạn đã xác nhận tham gia phỏng vấn.");
                    break;
                case 2:
                    service.respondToInterview(appId, false);
                    System.out.println(">>> Bạn đã từ chối phỏng vấn.");
                    break;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }
}

package ra.edu.presentation.user;

import ra.edu.business.model.Application;
import ra.edu.business.model.RecruitmentPosition;
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

public class RecruitmentApplicationUI {
    private static final IApplicationService service = new ApplicationServiceImpl();
    private static final int PAGE_SIZE = 10;

    public static void showMenu() {
        while (true) {
            System.out.println("\n===== ỨNG TUYỂN VỊ TRÍ =====");
            System.out.println("1. Danh sách vị trí đang hoạt động");
            System.out.println("2. Xem chi tiết và ứng tuyển");
            System.out.println("0. Quay lại");
            int choice = readInt("Chọn: ");
            switch (choice) {
                case 1: listActivePositions(); break;
                case 2: viewAndApply();        break;
                case 0: return;
                default: System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void listActivePositions() {
        BiFunction<Integer,Integer,List<RecruitmentPosition>> fetchPage =
                (page, size) -> service.getActivePositions(page, size);
        IntSupplier totalCount = () -> service.countActivePositions();

        String[] headers = {
                "ID", "Tên", "Mô tả",
                "LươngMin", "LươngMax",
                "KinhNghiemMin",
                "NgayTao", "NgayHetHan"
        };
        Function<RecruitmentPosition,String[]> mapper = rp -> new String[]{
                String.valueOf(rp.getId()),
                rp.getName(),
                rp.getDescription(),
                rp.getMinSalary().toPlainString(),
                rp.getMaxSalary().toPlainString(),
                String.valueOf(rp.getMinExperience()),
                rp.getCreatedDate().toString(),
                rp.getExpiredDate().toString()
        };

        DataFormatter.printInteractiveTable(
                headers,
                fetchPage,
                totalCount,
                mapper,
                PAGE_SIZE
        );
    }

    private static void viewAndApply() {
        int pid = readInt("Nhập ID vị trí muốn xem: ");
        RecruitmentPosition rp = service.getPositionDetails(pid);
        if (rp == null) {
            System.out.println(">>> Không tìm thấy vị trí.");
            return;
        }

        String[] hdr = { "Thuộc tính", "Giá trị" };
        List<String[]> rows = List.of(
                new String[]{ "ID",             String.valueOf(rp.getId()) },
                new String[]{ "Tên",            rp.getName() },
                new String[]{ "Mô tả",          rp.getDescription() },
                new String[]{ "Lương tối thiểu", rp.getMinSalary().toPlainString() },
                new String[]{ "Lương tối đa",   rp.getMaxSalary().toPlainString() },
                new String[]{ "Kinh nghiệm tối thiểu", String.valueOf(rp.getMinExperience()) },
                new String[]{ "Ngày tạo",       rp.getCreatedDate().toString() },
                new String[]{ "Ngày hết hạn",    rp.getExpiredDate().toString() }
        );
        DataFormatter.printTable(hdr, rows, row -> row);

        int cid = MainApplication.currentUser.getId();
        boolean already = service.getSubmittedApplications(cid, 1, Integer.MAX_VALUE).stream()
                .anyMatch(a -> a.getRecruitmentPositionId() == pid
                        && !"rejected".equalsIgnoreCase(a.getProgress()));
        if (already) {
            System.out.println(">>> Bạn đã ứng tuyển vị trí này và chưa bị từ chối.");
            return;
        }

        String confirm;
        do {
            confirm = readNonEmptyString("Bạn muốn ứng tuyển vị trí này? (y/n): ");
        } while (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("n"));

        if (confirm.equalsIgnoreCase("y")) {
            String cvUrl;
            do {
                cvUrl = readNonEmptyString("Nhập URL CV: ");
            } while (cvUrl.trim().isEmpty());

            boolean ok = service.submitApplication(cid, pid, cvUrl);
            System.out.println(ok
                    ? ">>> Ứng tuyển thành công."
                    : ">>> Lỗi khi ứng tuyển."
            );
        } else {
            System.out.println(">>> Đã hủy ứng tuyển.");
        }
    }
}

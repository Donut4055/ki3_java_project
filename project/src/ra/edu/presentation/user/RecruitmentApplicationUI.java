package ra.edu.presentation.user;

import ra.edu.business.model.Application;
import ra.edu.business.model.RecruitmentPosition;
import ra.edu.business.service.user.application.IApplicationService;
import ra.edu.business.service.user.application.ApplicationServiceImpl;
import ra.edu.MainApplication;
import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

import java.util.List;
import java.util.Scanner;

public class RecruitmentApplicationUI {
    private static final IApplicationService service = new ApplicationServiceImpl();
    public static void showMenu() {
        while (true) {
            System.out.println("\n===== ỨNG TUYỂN VỊ TRÍ =====");
            System.out.println("1. Danh sách vị trí đang hoạt động");
            System.out.println("2. Xem chi tiết và ứng tuyển");
            System.out.println("0. Quay lại");
            int choice = readInt("Chọn: ");
            switch (choice) {
                case 1:
                    listActivePositions();
                    break;
                case 2:
                    viewAndApply();
                    break;
                case 0:
                    return;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void listActivePositions() {
        int page = readInt("Nhập trang (>=1): ");
        int size = readInt("Số vị trí/trang (>=1): ");
        List<RecruitmentPosition> list = service.getActivePositions(page, size);
        System.out.println("---- VỊ TRÍ ĐANG HOẠT ĐỘNG ----");
        list.forEach(rp -> System.out.printf(
                "ID:%d | %s | Hạn nộp:%s%n",
                rp.getId(), rp.getName(), rp.getExpiredDate()));
    }

    private static void viewAndApply() {
        int pid = readInt("Nhập ID vị trí muốn xem: ");
        RecruitmentPosition rp = service.getPositionDetails(pid);
        if (rp == null) {
            System.out.println(">>> Không tìm thấy vị trí.");
            return;
        }
        System.out.println(rp);
        int cid = MainApplication.currentUser.getId();
        List<Application> prev = service.getSubmittedApplications(cid, 1, Integer.MAX_VALUE);
        boolean already = prev.stream()
                .anyMatch(a -> a.getRecruitmentPositionId() == pid && !"rejected".equalsIgnoreCase(a.getProgress()));
        if (already) {
            System.out.println(">>> Bạn đã ứng tuyển vị trí này và chưa bị từ chối.");
            return;
        }
        System.out.print("Bạn muốn ứng tuyển vị trí này? (y/n): ");
        String confirm = readInt("Bạn chọn (1-Yes, 2-No): ") == 1 ? "y" : "n";
        if ("y".equalsIgnoreCase(confirm)) {
            String cv = readNonEmptyString("Nhập URL CV: ");
            boolean ok = service.submitApplication(cid, pid, cv);
            System.out.println(ok ? ">>> Ứng tuyển thành công." : ">>> Lỗi khi ứng tuyển.");
        } else {
            System.out.println(">>> Hủy ứng tuyển.");
        }
    }
}



package ra.edu.presentation.user;

import ra.edu.MainApplication;
import ra.edu.business.model.RecruitmentPosition;
import ra.edu.business.service.user.application.ApplicationServiceImpl;
import ra.edu.business.service.user.application.IApplicationService;

import java.util.List;
import java.util.Scanner;

public class RecruitmentApplicationUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final IApplicationService service = new ApplicationServiceImpl();

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== ỨNG TUYỂN VỊ TRÍ =====");
            System.out.println("1. Danh sách vị trí đang hoạt động");
            System.out.println("2. Xem chi tiết và ứng tuyển");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.print("Nhập trang: ");
                    int page = Integer.parseInt(scanner.nextLine());
                    System.out.print("Số vị trí/trang: ");
                    int size = Integer.parseInt(scanner.nextLine());
                    List<RecruitmentPosition> list = service.getActivePositions(page, size);
                    System.out.println("---- VỊ TRÍ ĐANG HOẠT ĐỘNG ----");
                    for (RecruitmentPosition rp : list) {
                        System.out.printf("ID:%d | %s | Hết hạn:%s\n",
                                rp.getId(), rp.getName(), rp.getExpiredDate());
                    }
                    break;
                case 2:
                    System.out.print("Nhập ID vị trí muốn ứng tuyển: ");
                    int pid = Integer.parseInt(scanner.nextLine());
                    RecruitmentPosition rp = service.getPositionDetails(pid);
                    if (rp != null) {
                        System.out.println(rp);
                        System.out.print("Nhập URL CV của bạn: ");
                        String cv = scanner.nextLine();
                        int cid = MainApplication.currentUser.getId();
                        boolean ok = service.submitApplication(cid, pid, cv);
                        System.out.println(ok ? ">>> Ứng tuyển thành công." : ">>> Lỗi khi ứng tuyển.");
                    } else {
                        System.out.println(">>> Không tìm thấy vị trí.");
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


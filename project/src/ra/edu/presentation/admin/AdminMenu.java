package ra.edu.presentation.admin;

import ra.edu.presentation.LoginUI;
import static ra.edu.presentation.LoginUI.clearLoginFile;
import static ra.edu.utils.InputUtils.readInt;

public class AdminMenu {
    public static void showMenu() {
        while (true) {
            System.out.println("\n===== MENU QUẢN TRỊ VIÊN =====");
            System.out.println("1. Quản lý công nghệ");
            System.out.println("2. Quản lý ứng viên");
            System.out.println("3. Quản lý vị trí tuyển dụng");
            System.out.println("4. Quản lý đơn ứng tuyển");  // ← thêm dòng này
            System.out.println("0. Đăng xuất");

            int choice = readInt("Chọn: ");
            switch (choice) {
                case 1:
                    TechnologyUI.showMenu();
                    break;
                case 2:
                    CandidateUI.showMenu();
                    break;
                case 3:
                    RecruitmentPositionUI.showMenu();
                    break;
                case 4:
                    AdminApplicationUI.showMenu();
                    break;
                case 0:
                    System.out.println(">>> Đăng xuất thành công.");
                    clearLoginFile();
                    LoginUI.displayLoginMenu();
                    return;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }
}

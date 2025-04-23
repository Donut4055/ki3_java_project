package ra.edu.presentation.user;

import static ra.edu.utils.InputUtils.readInt;
import ra.edu.presentation.LoginUI;
import static ra.edu.presentation.LoginUI.clearLoginFile;
import static ra.edu.MainApplication.currentUser;

public class UserMenu {

    public static void showMenu() {
        while (true) {
            System.out.println("\n===== MENU ỨNG VIÊN =====");
            System.out.println("1. Quản lý Thông tin cá nhân");
            System.out.println("2. Xem và Nộp đơn tuyển dụng");
            System.out.println("3. Quản lý đơn ứng tuyển");
            System.out.println("0. Đăng xuất");
            int choice = readInt("Chọn: ");

            switch (choice) {
                case 1:
                    PersonalInfoUI.showMenu(currentUser.getId());
                    break;
                case 2:
                    RecruitmentApplicationUI.showMenu();
                    break;
                case 3:
                    ApplicationManagementUI.showMenu();
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
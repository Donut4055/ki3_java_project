    package ra.edu.presentation.admin;

    import ra.edu.presentation.LoginUI;

    import java.util.Scanner;

    import static ra.edu.presentation.LoginUI.clearLoginFile;

    public class AdminMenu {
        private static final Scanner scanner = new Scanner(System.in);

        public static void showMenu() {
            int choice;
            do {
                System.out.println("\n===== MENU QUẢN TRỊ VIÊN =====");
                System.out.println("1. Quản lý công nghệ");
                System.out.println("2. Quản lý ứng viên");
                System.out.println("3. Quản lý vị trí tuyển dụng");
                System.out.println("0. Đăng xuất");
                System.out.print("Chọn: ");
                choice = Integer.parseInt(scanner.nextLine());

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
                    case 0:
                        System.out.println(">>> Đăng xuất thành công.");
                        clearLoginFile();
                        LoginUI.displayLoginMenu();
                        break;
                    default:
                        System.out.println(">>> Lựa chọn không hợp lệ.");
                }
            } while (choice != 0);
        }
    }

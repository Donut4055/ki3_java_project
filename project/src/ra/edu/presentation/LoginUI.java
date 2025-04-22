package ra.edu.presentation;

import ra.edu.business.dao.account.AccountDAOImpl;
import ra.edu.business.model.Account;
import ra.edu.business.service.account.AccountServiceImpl;
import ra.edu.presentation.admin.AdminMenu;
import ra.edu.presentation.user.UserMenu;
import ra.edu.MainApplication;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LoginUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AccountServiceImpl loginAccount = new AccountServiceImpl(new AccountDAOImpl());
    private static final String LOGIN_FILE = "isLoggedIn.txt";

    public static void displayLoginMenu() {
        while (true) {
            System.out.println("========== HỆ THỐNG TUYỂN DỤNG ==========");
            System.out.println("1. Đăng nhập Admin");
            System.out.println("2. Đăng nhập Ứng viên");
            System.out.println("3. Đăng ký tài khoản Ứng viên");
            System.out.println("4. Thoát");
            System.out.println("==========================================");
            System.out.print("Nhập lựa chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    login("ADMIN");
                    break;
                case "2":
                    login("USER");
                    break;
                case "3":
                    RegisterUI.displayRegisterMenu();
                    break;
                case "4":
                    clearLoginFile();
                    MainApplication.currentUser = null;
                    System.out.println("Thoát chương trình. Tạm biệt!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    private static void login(String expectedRole) {
        System.out.print("Nhập tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine();

        Account account = loginAccount.login(username, password, expectedRole);
        if (account == null) {
            System.out.println("Đăng nhập thất bại. Sai thông tin đăng nhập.");
            return;
        }

        if (!account.getRole().equalsIgnoreCase(expectedRole)) {
            System.out.println("Bạn không có quyền truy cập vào khu vực này.");
            return;
        }

        MainApplication.currentUser = account;
        writeLoginFile(account);

        System.out.println("Đăng nhập thành công!");
        if (account.getRole().equalsIgnoreCase("ADMIN")) {
            AdminMenu.showMenu();
        } else {
            UserMenu.showMenu();
        }
    }

    private static void writeLoginFile(Account account) {
        try (FileWriter writer = new FileWriter(LOGIN_FILE)) {
            writer.write("ID:" + account.getId() + ",Username:" + account.getUsername() + ",Role:" + account.getRole());
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file đăng nhập: " + e.getMessage());
        }
    }

    public static void clearLoginFile() {
        try (FileWriter writer = new FileWriter(LOGIN_FILE)) {
        } catch (IOException e) {
            System.err.println("Lỗi khi xóa file đăng nhập: " + e.getMessage());
        }
    }
}

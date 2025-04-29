package ra.edu.presentation;

import ra.edu.business.dao.account.AccountDAOImpl;
import ra.edu.business.model.Account;
import ra.edu.business.service.account.AccountServiceImpl;
import ra.edu.presentation.admin.AdminMenu;
import ra.edu.presentation.user.UserMenu;
import ra.edu.MainApplication;

import java.io.FileWriter;
import java.io.IOException;

import static java.lang.System.exit;
import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

public class LoginUI {
    private static final AccountServiceImpl accountService =
            new AccountServiceImpl(new AccountDAOImpl());
    private static final String LOGIN_FILE = "isLoggedIn.txt";

    public static void displayLoginMenu() {
        while (true) {
            System.out.println("\n========== HỆ THỐNG TUYỂN DỤNG ==========");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký tài khoản ứng viên");
            System.out.println("0. Thoát");
            int choice = readInt("Chọn: ");
            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    RegisterUI.displayRegisterMenu();
                    return;
                case 0:
                    System.out.println(">>> Cảm ơn bạn đã sử dụng hệ thống. Tạm biệt!");
                    exit(0);
                    return;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    private static void handleLogin() {
        String username = readNonEmptyString("Nhập tên đăng nhập: ");
        String password = readNonEmptyString("Nhập mật khẩu: ");

        // Thử đăng nhập ADMIN
        Account account = accountService.login(username, password, "ADMIN");
        if (account == null) {
            // Nếu không phải ADMIN, thử đăng nhập USER
            account = accountService.login(username, password, "USER");
        }

        if (account == null) {
            System.out.println(">>> Đăng nhập thất bại. Sai thông tin hoặc không có quyền.");
            return;
        }
        if ("locked".equalsIgnoreCase(account.getStatus())) {
            System.out.println(">>> Tài khoản của bạn đã bị khoá. Vui lòng liên hệ Admin.");
            return;
        }

        // Lưu vào currentUser và file
        MainApplication.currentUser = account;
        writeLoginFile(account);

        System.out.println(">>> Đăng nhập thành công! Chào, " + account.getUsername());
        if ("ADMIN".equalsIgnoreCase(account.getRole())) {
            AdminMenu.showMenu();
        } else {
            UserMenu.showMenu();
        }
    }

    private static void writeLoginFile(Account account) {
        try (FileWriter writer = new FileWriter(LOGIN_FILE)) {
            writer.write(
                    "ID:" + account.getId()
                            + ",Username:" + account.getUsername()
                            + ",Role:" + account.getRole()
                            + ",Status:" + account.getStatus()
            );
        } catch (IOException e) {
            System.err.println(">>> Lỗi khi ghi file đăng nhập: " + e.getMessage());
        }
    }

    public static void clearLoginFile() {
        try (FileWriter writer = new FileWriter(LOGIN_FILE)) {
        } catch (IOException e) {
            System.err.println(">>> Lỗi khi xóa file đăng nhập: " + e.getMessage());
        }
    }
}

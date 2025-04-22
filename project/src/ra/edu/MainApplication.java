package ra.edu;

import ra.edu.business.model.Account;
import ra.edu.presentation.MainUI;
import ra.edu.presentation.admin.AdminMenu;
import ra.edu.presentation.user.UserMenu;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainApplication {
    public static Account currentUser;
    private static final String LOGIN_FILE = "isLoggedIn.txt";

    public static void main(String[] args) {
        readLoginFile();
        if (currentUser != null) {
            System.out.println("Chào mừng trở lại, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                AdminMenu.showMenu();
            } else {
                UserMenu.showMenu();
            }
        } else {
            MainUI.main(args);
        }
    }

    private static void readLoginFile() {
        File file = new File(LOGIN_FILE);
        if (!file.exists()) {
            return;
        }
        try {
            String content = new String(Files.readAllBytes(file.toPath())).trim();
            if (content.isEmpty()) {
                return;
            }
            String[] parts = content.split(",");
            int id = 0;
            String username = "";
            String role = "";
            for (String part : parts) {
                String[] kv = part.split(":");
                if (kv.length == 2) {
                    switch (kv[0]) {
                        case "ID":
                            id = Integer.parseInt(kv[1]);
                            break;
                        case "Username":
                            username = kv[1];
                            break;
                        case "Role":
                            role = kv[1];
                            break;
                    }
                }
            }
            currentUser = new Account(id, username, null, role);
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file đăng nhập: " + e.getMessage());
        }
    }
}

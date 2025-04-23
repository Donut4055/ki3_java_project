package ra.edu;

import ra.edu.business.model.Account;
import ra.edu.presentation.MainUI;
import ra.edu.presentation.admin.AdminMenu;
import ra.edu.presentation.user.UserMenu;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApplication {
    public static Account currentUser;
    private static final String LOGIN_FILE = "isLoggedIn.txt";

    public static void main(String[] args) {
        readLoginFile();
        if (currentUser != null) {
            System.out.println("Chào mừng trở lại, " + currentUser.getUsername()
                    + " (" + currentUser.getRole() + ")");
            if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                AdminMenu.showMenu();
            } else {
                UserMenu.showMenu();
            }
        } else {
            MainUI.DisplayMenu();
        }
    }

    private static void readLoginFile() {
        Path path = Paths.get(LOGIN_FILE);
        if (!Files.exists(path)) return;

        try {
            String content = Files.readString(path).trim();
            if (content.isEmpty()) return;

            String[] parts = content.split(",");
            int id = -1;
            String username = null, role = null, status = null;
            for (String part : parts) {
                String[] kv = part.split(":", 2);
                if (kv.length != 2) continue;
                String key = kv[0].trim(), value = kv[1].trim();
                switch (key) {
                    case "ID":       id = Integer.parseInt(value); break;
                    case "Username": username = value; break;
                    case "Role":     role = value; break;
                    case "Status":   status = value; break;
                }
            }
            if (id >= 0 && username != null && role != null && status != null) {
                currentUser = new Account(id, username, null, role, status);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file đăng nhập: " + e.getMessage());
        }
    }

};
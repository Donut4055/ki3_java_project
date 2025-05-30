package ra.edu.business.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/recruitment_system";
    private static final String USER = "root";
    private static final String PASSWORD = "donut_12321";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Không thể kết nối CSDL: " + e.getMessage());
            return null;
        }
    }

    public static void close(AutoCloseable... resources) {
        for (AutoCloseable res : resources) {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    System.err.println("Lỗi đóng resource: " + e.getMessage());
                }
            }
        }
    }
}

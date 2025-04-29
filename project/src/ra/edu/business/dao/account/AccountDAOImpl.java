package ra.edu.business.dao.account;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Account;

import java.sql.*;

public class AccountDAOImpl implements IAccountDAO {

    @Override
    public Account login(String username, String password, String role) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể mở kết nối CSDL trong login.");
            return null;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL sp_login_account(?, ?, ?)}");
            cs.setString(1, username);
            cs.setString(2, password);
            cs.setString(3, role);

            rs = cs.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi login: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi bất thường trong login: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return null;
    }

    @Override
    public boolean createUserAccount(String username, String password,
                                     String name, String email, String phone,
                                     String gender, String dob, String description,
                                     int experience) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể mở kết nối CSDL khi tạo tài khoản.");
            return false;
        }
        CallableStatement ps = null;
        try {
            ps = conn.prepareCall("{CALL register_user(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setInt(6, experience);
            ps.setString(7, gender);
            ps.setString(8, description);
            ps.setString(9, dob);
            ps.registerOutParameter(10, Types.INTEGER);

            ps.executeUpdate();
            int newCandidateId = ps.getInt(10);
            System.out.println("New Candidate ID: " + newCandidateId);
            return newCandidateId > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo user account: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Lỗi bất thường khi tạo user account: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(ps, conn);
        }
    }

    @Override
    public boolean saveUserInfo(int candidateId, String name, String email,
                                String phone, String gender, int experience,
                                String description, String dob) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể mở kết nối CSDL khi lưu thông tin user.");
            return false;
        }
        CallableStatement ps = null;
        try {
            ps = conn.prepareCall("{CALL update_candidate_info(?, ?, ?, ?, ?, ?, ?, ?)}");
            ps.setInt(1, candidateId);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, gender);
            ps.setInt(6, experience);
            ps.setString(7, description);
            ps.setString(8, dob);

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi lưu thông tin user: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Lỗi bất thường khi lưu thông tin user: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(ps, conn);
        }
    }
    public boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM _account WHERE username = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra username: " + e.getMessage());
        }
        return false;
    }

    public boolean isEmailTaken(String email) {
        String sql = "SELECT COUNT(*) FROM candidate WHERE email = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra email: " + e.getMessage());
        }
        return false;
    }
}

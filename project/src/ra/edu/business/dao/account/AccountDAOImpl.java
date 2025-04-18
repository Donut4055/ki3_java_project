package ra.edu.business.dao.account;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Account;
import java.sql.*;
import java.util.Date;

public class AccountDAOImpl implements IAccountDAO {

    @Override
    public Account login(String username, String password, String role) {
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.getConnection();
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
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi login: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, cs, rs);
        }

        return null;
    }

    @Override
    public boolean createUserAccount(String username, String password, String name, String email, String phone, String gender, String dob, String description, int experience) {
        String sql = "{CALL register_user(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement ps = conn.prepareCall(sql)) {

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
            e.printStackTrace();
            return false;
        }
    }




    @Override
    public boolean saveUserInfo(int candidateId, String name, String email, String phone, String gender, int experience, String description, String dob) {
        String sql = "{CALL update_candidate_info(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement ps = conn.prepareCall(sql)) {

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
            e.printStackTrace();
            return false;
        }
    }


}
package ra.edu.business.dao.account;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Account;
import java.sql.*;

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
}
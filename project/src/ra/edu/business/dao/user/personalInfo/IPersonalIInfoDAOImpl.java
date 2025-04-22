package ra.edu.business.dao.user.personalInfo;

import ra.edu.business.config.ConnectionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class IPersonalIInfoDAOImpl implements IPersonalIInfoDAO {
    @Override
    public boolean updateCandidateProfile(int candidateId, String name, String email, String phone, String gender, String dob, String description) {
        String sql = "{CALL sp_update_candidate_profile(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, candidateId);
            cs.setString(2, name);
            cs.setString(3, email);
            cs.setString(4, phone);
            cs.setString(5, gender);
            cs.setString(6, dob);
            cs.setString(7, description);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật profile: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean changeCandidatePassword(int candidateId, String oldPassword, String newPassword) {
        String sql = "{CALL sp_change_candidate_password(?, ?, ?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, candidateId);
            cs.setString(2, oldPassword);
            cs.setString(3, newPassword);
            cs.registerOutParameter(4, Types.TINYINT);

            cs.executeUpdate();
            int success = cs.getInt(4);
            return success == 1;
        } catch (SQLException e) {
            System.err.println("Lỗi đổi mật khẩu: " + e.getMessage());
            return false;
        }
    }
}

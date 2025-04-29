package ra.edu.business.dao.user.personalInfo;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Candidate;

import java.sql.*;

public class IPersonalIInfoDAOImpl implements IPersonalIInfoDAO {

    @Override
    public boolean updateCandidateProfile(int candidateId,
                                          String name,
                                          String email,
                                          String phone,
                                          String gender,
                                          String dob,
                                          String description) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong updateCandidateProfile.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL sp_update_candidate_profile(?, ?, ?, ?, ?, ?, ?)}");
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
            System.err.println("Lỗi updateCandidateProfile: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public boolean changeCandidatePassword(int candidateId,
                                           String oldPassword,
                                           String newPassword) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong changeCandidatePassword.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL sp_change_candidate_password(?, ?, ?, ?)}");
            cs.setInt(1, candidateId);
            cs.setString(2, oldPassword);
            cs.setString(3, newPassword);
            cs.registerOutParameter(4, Types.TINYINT);
            cs.executeUpdate();
            return cs.getInt(4) == 1;
        } catch (SQLException e) {
            System.err.println("Lỗi changeCandidatePassword: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public Candidate getCandidateProfile(int candidateId) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong getCandidateProfile.");
            return null;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL sp_get_candidate_profile(?)}");
            cs.setInt(1, candidateId);
            rs = cs.executeQuery();
            if (rs.next()) {
                return new Candidate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("experience"),
                        rs.getString("gender"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getDate("dob")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getCandidateProfile: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return null;
    }
}

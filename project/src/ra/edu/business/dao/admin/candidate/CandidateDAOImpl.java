package ra.edu.business.dao.admin.candidate;

import ra.edu.business.model.Candidate;
import ra.edu.business.config.ConnectionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDAOImpl implements ICandidateDAO {

    @Override
    public List<Candidate> getCandidates(int pageNumber, int pageSize) {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "{CALL get_candidates(?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, (pageNumber - 1) * pageSize);
            cs.setInt(2, pageSize);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                candidates.add(new Candidate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("experience"),
                        rs.getString("gender"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getDate("dob")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public boolean lockUnlockAccount(int candidateId, String status) {
        String sql = "{CALL lock_unlock_account(?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, candidateId);
            cs.setString(2, status);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String resetPassword(int candidateId) {
        String sql = "{CALL reset_password(?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, candidateId);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.executeUpdate();
            return cs.getString(2);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Candidate> searchCandidateByName(String name) {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "{CALL search_candidate_by_name(?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, name);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                candidates.add(new Candidate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("experience"),
                        rs.getString("gender"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getDate("dob")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public List<Candidate> filterCandidatesByExperience(int experience) {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "{CALL filter_candidates_by_experience(?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, experience);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                candidates.add(new Candidate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("experience"),
                        rs.getString("gender"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getDate("dob")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public List<Candidate> filterCandidatesByAge(int age) {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "{CALL filter_candidates_by_age(?)}";  // Gọi stored procedure
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, age);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                candidates.add(new Candidate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("experience"),
                        rs.getString("gender"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getDate("dob")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lọc ứng viên theo tuổi: " + e.getMessage());
        }
        return candidates;
    }


    @Override
    public List<Candidate> filterCandidatesByGender(String gender) {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "{CALL filter_candidates_by_gender(?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(gender, 1);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                candidates.add(new Candidate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("experience"),
                        rs.getString("gender"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getDate("dob")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public List<Candidate> filterCandidatesByTechnology(int technologyId) {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "{CALL filter_candidates_by_technology(?)}";  // Gọi stored procedure lọc theo công nghệ
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, technologyId);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                candidates.add(new Candidate(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("experience"),
                        rs.getString("gender"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getDate("dob")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lọc ứng viên theo công nghệ: " + e.getMessage());
        }
        return candidates;
    }


}

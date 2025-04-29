package ra.edu.business.dao.admin.candidate;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDAOImpl implements ICandidateDAO {

    @Override
    public List<Candidate> getCandidates(int pageNumber, int pageSize) {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong getCandidates.");
            return candidates;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL get_candidates(?, ?)}");
            cs.setInt(1, (pageNumber - 1) * pageSize);
            cs.setInt(2, pageSize);
            rs = cs.executeQuery();
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
            System.err.println("Lỗi getCandidates: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ getCandidates: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return candidates;
    }

    @Override
    public boolean lockUnlockAccount(int candidateId, String status) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong lockUnlockAccount.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL lock_unlock_account(?, ?)}");
            cs.setInt(1, candidateId);
            cs.setString(2, status);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi lockUnlockAccount: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ lockUnlockAccount: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public String resetPassword(int candidateId) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong resetPassword.");
            return null;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL reset_password(?, ?)}");
            cs.setInt(1, candidateId);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.executeUpdate();
            return cs.getString(2);
        } catch (SQLException e) {
            System.err.println("Lỗi resetPassword: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ resetPassword: " + e.getMessage());
            return null;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public List<Candidate> searchCandidateByName(String name) {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong searchCandidateByName.");
            return candidates;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL search_candidate_by_name(?)}");
            cs.setString(1, name);
            rs = cs.executeQuery();
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
            System.err.println("Lỗi searchCandidateByName: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ searchCandidateByName: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return candidates;
    }

    @Override
    public List<Candidate> filterCandidatesByExperience(int experience) {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong filterCandidatesByExperience.");
            return candidates;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL filter_candidates_by_experience(?)}");
            cs.setInt(1, experience);
            rs = cs.executeQuery();
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
            System.err.println("Lỗi filterCandidatesByExperience: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ filterCandidatesByExperience: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return candidates;
    }

    @Override
    public List<Candidate> filterCandidatesByAge(int age) {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong filterCandidatesByAge.");
            return candidates;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL filter_candidates_by_age(?)}");
            cs.setInt(1, age);
            rs = cs.executeQuery();
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
            System.err.println("Lỗi filterCandidatesByAge: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ filterCandidatesByAge: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return candidates;
    }

    @Override
    public List<Candidate> filterCandidatesByGender(String gender) {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong filterCandidatesByGender.");
            return candidates;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL filter_candidates_by_gender(?)}");
            cs.setString(1, gender);
            rs = cs.executeQuery();
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
            System.err.println("Lỗi filterCandidatesByGender: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ filterCandidatesByGender: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return candidates;
    }

    @Override
    public List<Candidate> filterCandidatesByTechnology(int technologyId) {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong filterCandidatesByTechnology.");
            return candidates;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL filter_candidates_by_technology(?)}");
            cs.setInt(1, technologyId);
            rs = cs.executeQuery();
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
            System.err.println("Lỗi filterCandidatesByTechnology: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ filterCandidatesByTechnology: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return candidates;
    }

    @Override
    public int countCandidates() {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong countCandidates.");
            return 0;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL sp_count_candidates()}");
            rs = cs.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi countCandidates: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ countCandidates: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return 0;
    }

    @Override
    public boolean cancelPendingApplications(int candidateId) {
        String sql = "{CALL sp_cancel_pending_applications(?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, candidateId);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi huỷ pending applications: " + e.getMessage());
            return false;
        }
    }
}

package ra.edu.business.dao.user.application;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Application;
import ra.edu.business.model.RecruitmentPosition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAOImpl implements IApplicationDAO {

    @Override
    public List<Application> getSubmittedApplications(int candidateId, int page, int size) {
        List<Application> list = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong getSubmittedApplications.");
            return list;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL sp_get_submitted_applications(?, ?, ?)}");
            cs.setInt(1, candidateId);
            cs.setInt(2, (page - 1) * size);
            cs.setInt(3, size);
            rs = cs.executeQuery();
            while (rs.next()) {
                list.add(new Application(
                        rs.getInt("id"),
                        rs.getInt("candidateId"),
                        rs.getInt("recruitmentPositionId"),
                        rs.getString("cvUrl"),
                        rs.getString("progress"),
                        rs.getTimestamp("interviewRequestDate"),
                        rs.getString("interviewRequestResult"),
                        rs.getString("interviewLink"),
                        rs.getTimestamp("interviewTime"),
                        rs.getString("interviewResult"),
                        rs.getString("interviewResultNote"),
                        rs.getTimestamp("destroyAt"),
                        rs.getTimestamp("createAt"),
                        rs.getTimestamp("updateAt"),
                        rs.getString("destroyReason")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getSubmittedApplications: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ getSubmittedApplications: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return list;
    }

    @Override
    public Application getApplicationDetails(int applicationId) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong getApplicationDetails.");
            return null;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL sp_get_application_details(?)}");
            cs.setInt(1, applicationId);
            rs = cs.executeQuery();
            if (rs.next()) {
                return new Application(
                        rs.getInt("id"),
                        rs.getInt("candidateId"),
                        rs.getInt("recruitmentPositionId"),
                        rs.getString("cvUrl"),
                        rs.getString("progress"),
                        rs.getTimestamp("interviewRequestDate"),
                        rs.getString("interviewRequestResult"),
                        rs.getString("interviewLink"),
                        rs.getTimestamp("interviewTime"),
                        rs.getString("interviewResult"),
                        rs.getString("interviewResultNote"),
                        rs.getTimestamp("destroyAt"),
                        rs.getTimestamp("createAt"),
                        rs.getTimestamp("updateAt"),
                        rs.getString("destroyReason")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getApplicationDetails: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ getApplicationDetails: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return null;
    }

    @Override
    public List<RecruitmentPosition> getActivePositions(int page, int size) {
        List<RecruitmentPosition> list = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong getActivePositions.");
            return list;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL sp_get_active_positions(?, ?)}");
            cs.setInt(1, (page - 1) * size);
            cs.setInt(2, size);
            rs = cs.executeQuery();
            while (rs.next()) {
                list.add(new RecruitmentPosition(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("minSalary"),
                        rs.getBigDecimal("maxSalary"),
                        rs.getInt("minExperience"),
                        rs.getDate("createdDate"),
                        rs.getDate("expiredDate")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getActivePositions: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ getActivePositions: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return list;
    }

    @Override
    public RecruitmentPosition getPositionDetails(int positionId) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong getPositionDetails.");
            return null;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL sp_get_position_details(?)}");
            cs.setInt(1, positionId);
            rs = cs.executeQuery();
            if (rs.next()) {
                return new RecruitmentPosition(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("minSalary"),
                        rs.getBigDecimal("maxSalary"),
                        rs.getInt("minExperience"),
                        rs.getDate("createdDate"),
                        rs.getDate("expiredDate")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getPositionDetails: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ getPositionDetails: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return null;
    }

    @Override
    public boolean submitApplication(int candidateId, int positionId, String cvUrl) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong submitApplication.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL sp_submit_application(?, ?, ?)}");
            cs.setInt(1, candidateId);
            cs.setInt(2, positionId);
            cs.setString(3, cvUrl);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi submitApplication: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ submitApplication: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public int countSubmittedApplications(int candidateId) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong countSubmittedApplications.");
            return 0;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL sp_count_submitted_applications(?)}");
            cs.setInt(1, candidateId);
            rs = cs.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi countSubmittedApplications: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ countSubmittedApplications: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return 0;
    }

    @Override
    public boolean respondToInterview(int appId, boolean confirm) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong respondToInterview.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL sp_candidate_respond_interview(?, ?)}");
            cs.setInt(1, appId);
            cs.setString(2, confirm ? "Confirmed" : "Declined");
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi respondToInterview: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ respondToInterview: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public int countActivePositions() {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong countActivePositions.");
            return 0;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL sp_count_active_positions()}");
            rs = cs.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi countActivePositions: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ countActivePositions: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return 0;
    }
}

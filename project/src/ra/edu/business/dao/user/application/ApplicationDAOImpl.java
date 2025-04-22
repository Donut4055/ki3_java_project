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
        String sql = "{CALL sp_get_submitted_applications(?, ?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, candidateId);
            cs.setInt(2, (page-1)*size);
            cs.setInt(3, size);
            ResultSet rs = cs.executeQuery();
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
            System.err.println("Lỗi lấy đơn đã apply: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Application getApplicationDetails(int applicationId) {
        String sql = "{CALL sp_get_application_details(?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, applicationId);
            ResultSet rs = cs.executeQuery();
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
            System.err.println("Lỗi xem chi tiết đơn: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<RecruitmentPosition> getActivePositions(int page, int size) {
        List<RecruitmentPosition> list = new ArrayList<>();
        String sql = "{CALL sp_get_active_positions(?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, (page-1)*size);
            cs.setInt(2, size);
            ResultSet rs = cs.executeQuery();
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
            System.err.println("Lỗi lấy vị trí active: " + e.getMessage());
        }
        return list;
    }

    @Override
    public RecruitmentPosition getPositionDetails(int positionId) {
        String sql = "{CALL sp_get_position_details(?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, positionId);
            ResultSet rs = cs.executeQuery();
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
            System.err.println("Lỗi xem chi tiết vị trí: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean submitApplication(int candidateId, int positionId, String cvUrl) {
        String sql = "{CALL sp_submit_application(?, ?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, candidateId);
            cs.setInt(2, positionId);
            cs.setString(3, cvUrl);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi ứng tuyển: " + e.getMessage());
            return false;
        }
    }
}
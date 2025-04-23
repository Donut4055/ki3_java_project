package ra.edu.business.dao.admin.AdminApplication;


import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Application;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminApplicationDAOImpl implements IAdminApplicationDAO {
    private Application mapApp(ResultSet rs) throws SQLException {
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

    @Override
    public List<Application> listApplications(int page, int size) {
        String sql = "{CALL sp_admin_list_applications(?, ?)}";
        List<Application> list = new ArrayList<>();
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, (page-1)*size);
            cs.setInt(2, size);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) list.add(mapApp(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Application> filterByProgress(String progress, int page, int size) {
        String sql = "{CALL sp_admin_filter_by_progress(?, ?, ?)}";
        List<Application> list = new ArrayList<>();
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, progress);
            cs.setInt(2, (page-1)*size);
            cs.setInt(3, size);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) list.add(mapApp(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Application> filterByResult(String result, int page, int size) {
        String sql = "{CALL sp_admin_filter_by_result(?, ?, ?)}";
        List<Application> list = new ArrayList<>();
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, result);
            cs.setInt(2, (page-1)*size);
            cs.setInt(3, size);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) list.add(mapApp(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean cancelApplication(int appId, String reason) {
        String sql = "{CALL sp_admin_cancel_application(?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, appId);
            cs.setString(2, reason);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Application viewApplication(int appId) {
        String sql = "{CALL sp_admin_view_application(?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, appId);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) return mapApp(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean setInterview(int appId, String link, Timestamp time) {
        String sql = "{CALL sp_admin_set_interview(?, ?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, appId);
            cs.setString(2, link);
            cs.setTimestamp(3, time);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thiết lập phỏng vấn: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateResult(int appId, String result, String note) {
        String sql = "{CALL sp_admin_update_result(?, ?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, appId);
            cs.setString(2, result);
            cs.setString(3, note);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật kết quả: " + e.getMessage());
            return false;
        }
    }

}


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
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong listApplications.");
            return list;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, (page - 1) * size);
            cs.setInt(2, size);
            rs = cs.executeQuery();
            while (rs.next()) {
                list.add(mapApp(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi listApplications: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ listApplications: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return list;
    }

    @Override
    public List<Application> filterByProgress(String progress, int page, int size) {
        String sql = "{CALL sp_admin_filter_by_progress(?, ?, ?)}";
        List<Application> list = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong filterByProgress.");
            return list;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall(sql);
            cs.setString(1, progress);
            cs.setInt(2, (page - 1) * size);
            cs.setInt(3, size);
            rs = cs.executeQuery();
            while (rs.next()) {
                list.add(mapApp(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi filterByProgress: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ filterByProgress: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return list;
    }

    @Override
    public List<Application> filterByResult(String result, int page, int size) {
        String sql = "{CALL sp_admin_filter_by_result(?, ?, ?)}";
        List<Application> list = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong filterByResult.");
            return list;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall(sql);
            cs.setString(1, result);
            cs.setInt(2, (page - 1) * size);
            cs.setInt(3, size);
            rs = cs.executeQuery();
            while (rs.next()) {
                list.add(mapApp(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi filterByResult: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ filterByResult: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return list;
    }

    @Override
    public boolean cancelApplication(int appId, String reason) {
        String sql = "{CALL sp_admin_cancel_application(?, ?)}";
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong cancelApplication.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, appId);
            cs.setString(2, reason);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi cancelApplication: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ cancelApplication: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public Application viewApplication(int appId) {
        String sql = "{CALL sp_admin_view_application(?)}";
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong viewApplication.");
            return null;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, appId);
            rs = cs.executeQuery();
            if (rs.next()) {
                return mapApp(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi viewApplication: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ viewApplication: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return null;
    }

    @Override
    public boolean setInterview(int appId, String link, Timestamp time) {
        String sql = "{CALL sp_admin_set_interview(?, ?, ?)}";
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong setInterview.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, appId);
            cs.setString(2, link);
            cs.setTimestamp(3, time);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi setInterview: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ setInterview: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public boolean updateResult(int appId, String result, String note) {
        String sql = "{CALL sp_admin_update_result(?, ?, ?)}";
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong updateResult.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, appId);
            cs.setString(2, result);
            cs.setString(3, note);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi updateResult: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ updateResult: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }
    @Override
    public int countApplications() {
        String sql = "{CALL sp_count_applications()}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đếm đơn ứng tuyển: " + e.getMessage());
        }
        return 0;
    }
}

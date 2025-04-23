package ra.edu.business.dao.admin.technology;


import ra.edu.business.model.Technology;
import ra.edu.business.config.ConnectionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechnologyDAOImpl implements ITechnologyDAO {

    @Override
    public List<Technology> getTechnologies(int pageNumber, int pageSize) {
        List<Technology> technologies = new ArrayList<>();
        String sql = "{CALL get_technologies(?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, (pageNumber - 1) * pageSize);
            cs.setInt(2, pageSize);

            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                technologies.add(new Technology(
                        rs.getInt("technology_id"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return technologies;
    }

    public boolean isTechnologyNameExist(String technologyName) {
        String sql = "SELECT COUNT(*) FROM technology WHERE name = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, technologyName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra tên công nghệ: " + e.getMessage());
        }
        return false;
    }

    // Thêm công nghệ mới vào cơ sở dữ liệu
    public boolean addTechnology(Technology technology) {
        String sql = "{CALL add_technology(?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, technology.getName());
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm công nghệ: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật công nghệ
    public boolean updateTechnology(int id, String newName) {
        String sql = "{CALL update_technology(?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            cs.setString(2, newName);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật công nghệ: " + e.getMessage());
            return false;
        }
    }

    // Xóa công nghệ
    public boolean deleteTechnology(int id) {
        String sql = "{CALL delete_technology(?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa công nghệ: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int countTechnologies() {
        String sql = "{CALL sp_count_technologies()}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đếm công nghệ: " + e.getMessage());
        }
        return 0;
    }
}

package ra.edu.business.dao.admin.technology;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Technology;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechnologyDAOImpl implements ITechnologyDAO {

    @Override
    public List<Technology> getTechnologies(int pageNumber, int pageSize) {
        List<Technology> technologies = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong getTechnologies.");
            return technologies;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL get_technologies(?, ?)}");
            cs.setInt(1, (pageNumber - 1) * pageSize);
            cs.setInt(2, pageSize);
            rs = cs.executeQuery();
            while (rs.next()) {
                technologies.add(new Technology(
                        rs.getInt("technology_id"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getTechnologies: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return technologies;
    }

    @Override
    public boolean addTechnology(Technology technology) {
        String name = technology.getName();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong addTechnology.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL add_technology(?)}");
            cs.setString(1, name);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi addTechnology: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public boolean updateTechnology(int id, String newName) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong updateTechnology.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL update_technology(?, ?)}");
            cs.setInt(1, id);
            cs.setString(2, newName);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi updateTechnology: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public boolean deleteTechnology(int id) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong deleteTechnology.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL delete_technology(?)}");
            cs.setInt(1, id);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi deleteTechnology: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public boolean isTechnologyNameExist(String technologyName) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong isTechnologyNameExist.");
            return false;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT COUNT(*) FROM technology WHERE name = ?");
            ps.setString(1, technologyName);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi isTechnologyNameExist: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, ps, conn);
        }
        return false;
    }

    @Override
    public int countTechnologies() {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong countTechnologies.");
            return 0;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL sp_count_technologies()}");
            rs = cs.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi countTechnologies: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return 0;
    }
}

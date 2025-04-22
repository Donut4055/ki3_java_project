package ra.edu.business.dao.admin.recruitment;
import ra.edu.business.model.RecruitmentPosition;
import ra.edu.business.config.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class RecruitmentPositionDAOImpl implements IRecruitmentPositionDAO {

    @Override
    public List<RecruitmentPosition> getPositions(int page, int size) {
        List<RecruitmentPosition> list = new ArrayList<>();
        String sql = "{CALL get_recruitment_positions(?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, (page - 1) * size);
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
            System.err.println("Lỗi khi lấy vị trí: " + e.getMessage());
        }
        return list;
    }

    @Override
    public int addPosition(RecruitmentPosition rp, List<Integer> techIds) {
        String sql = "{CALL add_recruitment_position(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, rp.getName());
            cs.setString(2, rp.getDescription());
            cs.setBigDecimal(3, rp.getMinSalary());
            cs.setBigDecimal(4, rp.getMaxSalary());
            cs.setInt(5, rp.getMinExperience());
            cs.setDate(6, new java.sql.Date(rp.getExpiredDate().getTime()));
            cs.registerOutParameter(7, Types.INTEGER);
            cs.executeUpdate();
            int newId = cs.getInt(7);
            // link technologies
            try (CallableStatement link = conn.prepareCall("{CALL link_position_technology(?, ?)}")) {
                for (Integer techId : techIds) {
                    link.setInt(1, newId);
                    link.setInt(2, techId);
                    link.executeUpdate();
                }
            }
            return newId;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm vị trí: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public boolean updatePosition(RecruitmentPosition rp, List<Integer> techIds) {
        String sql = "{CALL update_recruitment_position(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, rp.getId());
            cs.setString(2, rp.getName());
            cs.setString(3, rp.getDescription());
            cs.setBigDecimal(4, rp.getMinSalary());
            cs.setBigDecimal(5, rp.getMaxSalary());
            cs.setInt(6, rp.getMinExperience());
            cs.setDate(7, new java.sql.Date(rp.getExpiredDate().getTime()));
            cs.executeUpdate();
            // link new techs
            try (CallableStatement link = conn.prepareCall("{CALL link_position_technology(?, ?)}")) {
                for (Integer techId : techIds) {
                    link.setInt(1, rp.getId());
                    link.setInt(2, techId);
                    link.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật vị trí: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletePosition(int id) {
        String sql = "{CALL delete_recruitment_position(?)}";
        try (Connection conn = ConnectionDB.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa vị trí: " + e.getMessage());
            return false;
        }
    }
}

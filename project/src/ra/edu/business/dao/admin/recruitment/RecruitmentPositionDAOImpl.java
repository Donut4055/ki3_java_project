package ra.edu.business.dao.admin.recruitment;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.RecruitmentPosition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecruitmentPositionDAOImpl implements IRecruitmentPositionDAO {

    @Override
    public List<RecruitmentPosition> getPositions(int page, int size) {
        List<RecruitmentPosition> list = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong getPositions.");
            return list;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL get_recruitment_positions(?, ?)}");
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
            System.err.println("Lỗi getPositions: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ getPositions: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return list;
    }

    @Override
    public int addPosition(RecruitmentPosition rp, List<Integer> techIds) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong addPosition.");
            return -1;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL add_recruitment_position(?, ?, ?, ?, ?, ?, ?)}");
            cs.setString(1, rp.getName());
            cs.setString(2, rp.getDescription());
            cs.setBigDecimal(3, rp.getMinSalary());
            cs.setBigDecimal(4, rp.getMaxSalary());
            cs.setInt(5, rp.getMinExperience());
            cs.setDate(6, new Date(rp.getExpiredDate().getTime()));
            cs.registerOutParameter(7, Types.INTEGER);
            cs.executeUpdate();
            int newId = cs.getInt(7);

            // Liên kết công nghệ
            try (CallableStatement link = conn.prepareCall("{CALL link_position_technology(?, ?)}")) {
                for (Integer techId : techIds) {
                    link.setInt(1, newId);
                    link.setInt(2, techId);
                    link.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Lỗi link công nghệ trong addPosition: " + e.getMessage());
            }

            return newId;
        } catch (SQLException e) {
            System.err.println("Lỗi addPosition: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ addPosition: " + e.getMessage());
            return -1;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public boolean updatePosition(RecruitmentPosition rp, List<Integer> techIds) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong updatePosition.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL update_recruitment_position(?, ?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, rp.getId());
            cs.setString(2, rp.getName());
            cs.setString(3, rp.getDescription());
            cs.setBigDecimal(4, rp.getMinSalary());
            cs.setBigDecimal(5, rp.getMaxSalary());
            cs.setInt(6, rp.getMinExperience());
            cs.setDate(7, new Date(rp.getExpiredDate().getTime()));
            cs.executeUpdate();

            // Liên kết lại công nghệ
            try (CallableStatement link = conn.prepareCall("{CALL link_position_technology(?, ?)}")) {
                for (Integer techId : techIds) {
                    link.setInt(1, rp.getId());
                    link.setInt(2, techId);
                    link.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Lỗi link công nghệ trong updatePosition: " + e.getMessage());
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi updatePosition: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ updatePosition: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public boolean deletePosition(int id) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong deletePosition.");
            return false;
        }
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL delete_recruitment_position(?)}");
            cs.setInt(1, id);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi deletePosition: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ deletePosition: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.close(cs, conn);
        }
    }

    @Override
    public int countPositions() {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.err.println(">>> Không thể kết nối CSDL trong countPositions.");
            return 0;
        }
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{CALL sp_count_recruitment_positions()}");
            rs = cs.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi countPositions: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ngoại lệ bất ngờ countPositions: " + e.getMessage());
        } finally {
            ConnectionDB.close(rs, cs, conn);
        }
        return 0;
    }
}

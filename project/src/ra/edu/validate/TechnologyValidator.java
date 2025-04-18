package ra.edu.validate;

import ra.edu.business.dao.admin.technology.ITechnologyDAO;

public class TechnologyValidator {

    public static boolean isValidTechnologyName(String technologyName) {
        if (isNotEmpty(technologyName) && !isDuplicateTechnologyName(technologyName)) {
            return true;
        } else {
            System.out.println("Tên công nghệ không hợp lệ hoặc đã tồn tại.");
            return false;
        }
    }

    public static boolean isNotEmpty(String technologyName) {
        if (technologyName == null || technologyName.trim().isEmpty()) {
            System.out.println("Tên công nghệ không được để trống.");
            return false;
        }
        return true;
    }

    public static boolean isDuplicateTechnologyName(String technologyName) {
        return ITechnologyDAO.isTechnologyNameExist(technologyName);
    }
}

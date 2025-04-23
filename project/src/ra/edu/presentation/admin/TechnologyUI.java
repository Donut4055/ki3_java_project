package ra.edu.presentation.admin;

import ra.edu.business.model.Technology;
import ra.edu.business.service.admin.technology.ITechnologyService;
import ra.edu.business.service.admin.technology.TechnologyServiceImpl;
import ra.edu.validate.TechnologyValidator;
import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

import java.util.List;

public class TechnologyUI {
    private static final ITechnologyService technologyService = new TechnologyServiceImpl();

    public static void showMenu() {
        while (true) {
            System.out.println("\n===== MENU QUẢN LÝ CÔNG NGHỆ =====");
            System.out.println("1. Xem danh sách công nghệ");
            System.out.println("2. Thêm công nghệ mới");
            System.out.println("3. Sửa công nghệ");
            System.out.println("4. Xóa công nghệ");
            System.out.println("0. Quay về menu chính");
            int choice = readInt("Chọn: ");
            switch (choice) {
                case 1:
                    viewTechnologies();
                    break;
                case 2:
                    addTechnology();
                    break;
                case 3:
                    updateTechnology();
                    break;
                case 4:
                    deleteTechnology();
                    break;
                case 0:
                    System.out.println(">>> Quay về menu chính.");
                    return;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void viewTechnologies() {
        List<Technology> technologies = technologyService.getTechnologies(1, 10);
        technologies.forEach(tech -> System.out.println(tech.getId() + ": " + tech.getName()));
    }

    private static void addTechnology() {
        String technologyName = readNonEmptyString("Nhập tên công nghệ mới: ");
        try {
            if (TechnologyValidator.isValidTechnologyName(technologyName)) {
                if (technologyService.addTechnology(technologyName)) {
                    System.out.println(">>> Công nghệ đã được thêm thành công.");
                } else {
                    System.out.println(">>> Lỗi khi thêm công nghệ.");
                }
            } else {
                System.out.println(">>> Tên công nghệ không hợp lệ hoặc đã tồn tại.");
            }
        } catch (Exception e) {
            System.out.println(">>> Đã xảy ra lỗi khi thêm công nghệ: " + e.getMessage());
        }
    }

    private static void updateTechnology() {
        int id = readInt("Nhập ID công nghệ muốn sửa: ");
        String newName = readNonEmptyString("Nhập tên công nghệ mới: ");
        try {
            if (TechnologyValidator.isValidTechnologyName(newName) && !TechnologyValidator.isDuplicateTechnologyName(newName)) {
                if (technologyService.updateTechnology(id, newName)) {
                    System.out.println(">>> Công nghệ đã được cập nhật thành công.");
                } else {
                    System.out.println(">>> Lỗi khi cập nhật công nghệ.");
                }
            } else {
                System.out.println(">>> Tên công nghệ không hợp lệ hoặc đã tồn tại.");
            }
        } catch (Exception e) {
            System.out.println(">>> Đã xảy ra lỗi khi cập nhật công nghệ: " + e.getMessage());
        }
    }

    private static void deleteTechnology() {
        int id = readInt("Nhập ID công nghệ muốn xóa: ");
        try {
            if (technologyService.deleteTechnology(id)) {
                System.out.println(">>> Công nghệ đã được xóa.");
            } else {
                System.out.println(">>> Lỗi khi xóa công nghệ.");
            }
        } catch (Exception e) {
            System.out.println(">>> Đã xảy ra lỗi khi xóa công nghệ: " + e.getMessage());
        }
    }
}
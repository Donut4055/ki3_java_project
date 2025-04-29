package ra.edu.presentation.admin;

import ra.edu.business.model.Technology;
import ra.edu.business.service.admin.technology.ITechnologyService;
import ra.edu.business.service.admin.technology.TechnologyServiceImpl;
import ra.edu.validate.TechnologyValidator;
import ra.edu.utils.DataFormatter;

import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class TechnologyUI {
    private static final ITechnologyService technologyService = new TechnologyServiceImpl();
    private static final int PAGE_SIZE = 10;

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
        try {
            String[] headers = { "ID", "Tên" };
            BiFunction<Integer, Integer, List<Technology>> fetchPage =
                    (page, size) -> technologyService.getTechnologies(page, size);
            IntSupplier totalCount = technologyService::countTechnologies;
            Function<Technology, String[]> mapper = t -> new String[]{
                    String.valueOf(t.getId()), t.getName()
            };

            DataFormatter.printInteractiveTable(
                    headers, fetchPage, totalCount, mapper, PAGE_SIZE
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi hiển thị danh sách: " + e.getMessage());
        }
    }

    private static void addTechnology() {
        try {
            String name;
            do {
                name = readNonEmptyString("Nhập tên công nghệ mới: ");
            } while (!TechnologyValidator.isValidTechnologyName(name));

            if (technologyService.addTechnology(name)) {
                System.out.println(">>> Công nghệ đã được thêm thành công.");
            } else {
                System.out.println(">>> Lỗi khi thêm công nghệ.");
            }
        } catch (Exception e) {
            System.out.println(">>> Đã xảy ra lỗi khi thêm công nghệ: " + e.getMessage());
        }
    }

    private static void updateTechnology() {
        try {
            int id = readInt("Nhập ID công nghệ muốn sửa: ");
            String name;
            do {
                name = readNonEmptyString("Nhập tên công nghệ mới: ");
            } while (!TechnologyValidator.isValidTechnologyName(name));

            if (technologyService.updateTechnology(id, name)) {
                System.out.println(">>> Công nghệ đã được cập nhật thành công.");
            } else {
                System.out.println(">>> Lỗi khi cập nhật công nghệ.");
            }
        } catch (Exception e) {
            System.out.println(">>> Đã xảy ra lỗi khi cập nhật công nghệ: " + e.getMessage());
        }
    }

    private static void deleteTechnology() {
        try {
            int id = readInt("Nhập ID công nghệ muốn xóa: ");
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

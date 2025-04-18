package ra.edu.presentation.admin;

import ra.edu.business.model.Technology;
import ra.edu.business.service.admin.technology.ITechnologyService;
import ra.edu.business.service.admin.technology.TechnologyServiceImpl;
import ra.edu.validate.TechnologyValidator;

import java.util.List;
import java.util.Scanner;

public class TechnologyUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ITechnologyService technologyService = new TechnologyServiceImpl();

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== MENU QUẢN LÝ CÔNG NGHỆ =====");
            System.out.println("1. Xem danh sách công nghệ");
            System.out.println("2. Thêm công nghệ mới");
            System.out.println("3. Sửa công nghệ");
            System.out.println("4. Xóa công nghệ");
            System.out.println("0. Quay về menu chính");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

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
                    break;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }

    private static void viewTechnologies() {
        List<Technology> technologies = technologyService.getTechnologies(1, 10);
        technologies.forEach(tech -> System.out.println(tech.getId() + ": " + tech.getName()));
    }

    private static void addTechnology() {
        System.out.print("Nhập tên công nghệ mới: ");
        String technologyName = scanner.nextLine();

        try {
            if (TechnologyValidator.isValidTechnologyName(technologyName)) {
                if (technologyService.addTechnology(technologyName)) {
                    System.out.println("Công nghệ đã được thêm thành công.");
                } else {
                    System.out.println("Lỗi khi thêm công nghệ.");
                }
            } else {
                System.out.println("Tên công nghệ không hợp lệ hoặc đã tồn tại.");
            }
        } catch (Exception e) {
            System.out.println("Đã xảy ra lỗi khi thêm công nghệ: " + e.getMessage());
        }
    }

    private static void updateTechnology() {
        System.out.print("Nhập ID công nghệ muốn sửa: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Nhập tên công nghệ mới: ");
        String newName = scanner.nextLine();

        try {
            if (TechnologyValidator.isValidTechnologyName(newName) && !TechnologyValidator.isDuplicateTechnologyName(newName)) {
                if (technologyService.updateTechnology(id, newName)) {
                    System.out.println("Công nghệ đã được cập nhật thành công.");
                } else {
                    System.out.println("Lỗi khi cập nhật công nghệ.");
                }
            } else {
                System.out.println("Tên công nghệ không hợp lệ hoặc đã tồn tại.");
            }
        } catch (Exception e) {
            System.out.println("Đã xảy ra lỗi khi cập nhật công nghệ: " + e.getMessage());
        }
    }

    private static void deleteTechnology() {
        System.out.print("Nhập ID công nghệ muốn xóa: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            if (technologyService.deleteTechnology(id)) {
                System.out.println("Công nghệ đã được xóa.");
            } else {
                System.out.println("Lỗi khi xóa công nghệ.");
            }
        } catch (Exception e) {
            System.out.println("Đã xảy ra lỗi khi xóa công nghệ: " + e.getMessage());
        }
    }


}

package ra.edu.presentation.admin;

import ra.edu.business.model.RecruitmentPosition;
import ra.edu.business.service.admin.recruitment.IRecruitmentPositionService;
import ra.edu.business.service.admin.recruitment.RecruitmentPositionServiceImpl;
import ra.edu.validate.JobPositionValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RecruitmentPositionUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final IRecruitmentPositionService service = new RecruitmentPositionServiceImpl();

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== QUẢN LÝ VỊ TRÍ TUYỂN DỤNG =====");
            System.out.println("1. Danh sách vị trí tuyển dụng");
            System.out.println("2. Thêm vị trí mới");
            System.out.println("3. Cập nhật vị trí");
            System.out.println("4. Xóa vị trí");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1: list(); break;
                case 2: add(); break;
                case 3: update(); break;
                case 4: delete(); break;
                case 0: System.out.println(">>> Quay lại menu chính."); break;
                default: System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }

    private static void list() {
        int page;
        do {
            System.out.print("Trang (>=1): ");
            page = Integer.parseInt(scanner.nextLine());
        } while (page < 1);

        int size;
        do {
            System.out.print("Số bản ghi mỗi trang (>=1): ");
            size = Integer.parseInt(scanner.nextLine());
        } while (size < 1);

        List<RecruitmentPosition> list = service.getPositions(page, size);
        System.out.println("---- DANH SÁCH ----");
        for (RecruitmentPosition rp : list) {
            System.out.printf("ID:%d | %s | Lương:%s-%s | Exp:%d năm | Hết hạn:%s%n",
                    rp.getId(), rp.getName(), rp.getMinSalary(), rp.getMaxSalary(),
                    rp.getMinExperience(), rp.getExpiredDate());
        }
    }

    private static void add() {
        String name;
        do {
            System.out.print("Tên vị trí: ");
            name = scanner.nextLine();
        } while (!JobPositionValidator.isValidName(name));

        System.out.print("Mô tả: ");
        String desc = scanner.nextLine();

        BigDecimal min, max;
        do {
            System.out.print("Lương tối thiểu: ");
            min = new BigDecimal(scanner.nextLine());
            System.out.print("Lương tối đa: ");
            max = new BigDecimal(scanner.nextLine());
        } while (!JobPositionValidator.isValidSalaryRange(min, max));

        int exp;
        do {
            System.out.print("Kinh nghiệm tối thiểu (năm): ");
            exp = Integer.parseInt(scanner.nextLine());
        } while (!JobPositionValidator.isValidExperience(exp));

        LocalDate created = LocalDate.now();
        String expired;
        do {
            System.out.print("Ngày hết hạn (yyyy-MM-dd): ");
            expired = scanner.nextLine();
        } while (!JobPositionValidator.isValidDates(created, expired));

        List<Integer> techIds;
        do {
            System.out.print("IDs công nghệ (phân tách dấu phẩy, ít nhất 1): ");
            String[] parts = scanner.nextLine().split(",");
            techIds = new ArrayList<>();
            for (String p : parts) {
                try {
                    techIds.add(Integer.parseInt(p.trim()));
                } catch (NumberFormatException ignored) {}
            }
            if (techIds.isEmpty()) {
                System.out.println(">>> Bạn phải chọn ít nhất một công nghệ.");
            }
        } while (techIds.isEmpty());

        RecruitmentPosition rp = new RecruitmentPosition(0, name, desc, min, max, exp,
                java.sql.Date.valueOf(created), java.sql.Date.valueOf(expired));
        int newId = service.addPosition(rp, techIds);
        System.out.println(newId > 0 ? ">>> Thêm thành công! ID mới=" + newId : ">>> Lỗi khi thêm.");
    }

    private static void update() {
        System.out.print("ID cần sửa: ");
        int id = Integer.parseInt(scanner.nextLine());

        String name;
        do {
            System.out.print("Tên mới: ");
            name = scanner.nextLine();
        } while (!JobPositionValidator.isValidName(name));

        System.out.print("Mô tả mới: ");
        String desc = scanner.nextLine();

        BigDecimal min, max;
        do {
            System.out.print("Lương tối thiểu mới: ");
            min = new BigDecimal(scanner.nextLine());
            System.out.print("Lương tối đa mới: ");
            max = new BigDecimal(scanner.nextLine());
        } while (!JobPositionValidator.isValidSalaryRange(min, max));

        int exp;
        do {
            System.out.print("Kinh nghiệm mới (năm): ");
            exp = Integer.parseInt(scanner.nextLine());
        } while (!JobPositionValidator.isValidExperience(exp));

        LocalDate created = LocalDate.now();
        String expired;
        do {
            System.out.print("Ngày hết hạn mới (yyyy-MM-dd): ");
            expired = scanner.nextLine();
        } while (!JobPositionValidator.isValidDates(created, expired));


        List<Integer> techIds;
        do {
            System.out.print("IDs công nghệ mới (phân tách dấu phẩy, ít nhất 1): ");
            String[] parts = scanner.nextLine().split(",");
            techIds = new ArrayList<>();
            for (String p : parts) {
                try {
                    techIds.add(Integer.parseInt(p.trim()));
                } catch (NumberFormatException ignored) {}
            }
            if (techIds.isEmpty()) {
                System.out.println(">>> Bạn phải chọn ít nhất một công nghệ.");
            }
        } while (techIds.isEmpty());

        RecruitmentPosition rp = new RecruitmentPosition(id, name, desc, min, max, exp,
                java.sql.Date.valueOf(created), java.sql.Date.valueOf(expired));
        System.out.println(service.updatePosition(rp, techIds)
                ? ">>> Cập nhật thành công!"
                : ">>> Lỗi khi cập nhật.");
    }

    private static void delete() {
        System.out.print("ID cần xóa: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println(service.deletePosition(id)
                ? ">>> Xóa thành công!"
                : ">>> Lỗi khi xóa.");
    }
}
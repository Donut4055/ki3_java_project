package ra.edu.presentation.admin;

import ra.edu.business.model.RecruitmentPosition;
import ra.edu.business.service.admin.recruitment.IRecruitmentPositionService;
import ra.edu.business.service.admin.recruitment.RecruitmentPositionServiceImpl;
import ra.edu.validate.JobPositionValidator;
import ra.edu.utils.DataFormatter;

import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class RecruitmentPositionUI {
    private static final IRecruitmentPositionService service = new RecruitmentPositionServiceImpl();
    private static final int PAGE_SIZE = 10;

    public static void showMenu() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ VỊ TRÍ TUYỂN DỤNG =====");
            System.out.println("1. Danh sách vị trí tuyển dụng");
            System.out.println("2. Thêm vị trí mới");
            System.out.println("3. Cập nhật vị trí");
            System.out.println("4. Xóa vị trí");
            System.out.println("0. Quay lại");
            int choice = readInt("Chọn: ");
            switch (choice) {
                case 1: list();   break;
                case 2: add();    break;
                case 3: update(); break;
                case 4: delete(); break;
                case 0:
                    System.out.println(">>> Quay lại menu chính.");
                    return;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void list() {
        String[] headers = {
                "ID",
                "Name",
                "Description",
                "MinSalary",
                "MaxSalary",
                "MinExp",
                "CreatedDate",
                "ExpiredDate"
        };

        BiFunction<Integer,Integer,List<RecruitmentPosition>> fetchPage = service::getPositions;
        IntSupplier totalCount = () -> service.countPositions();
        Function<RecruitmentPosition,String[]> mapper = rp -> new String[]{
                String.valueOf(rp.getId()),
                rp.getName(),
                rp.getDescription(),
                rp.getMinSalary().toPlainString(),
                rp.getMaxSalary().toPlainString(),
                String.valueOf(rp.getMinExperience()),
                rp.getCreatedDate().toString(),
                rp.getExpiredDate().toString()
        };

        DataFormatter.printInteractiveTable(
                headers,
                fetchPage,
                totalCount,
                mapper,
                PAGE_SIZE
        );
    }

    private static void add() {
        String name;
        do {
            name = readNonEmptyString("Tên vị trí: ");
        } while (!JobPositionValidator.isValidName(name));

        String desc = readNonEmptyString("Mô tả (có thể để trống): ");

        BigDecimal min, max;
        do {
            min = new BigDecimal(readNonEmptyString("Lương tối thiểu: "));
            max = new BigDecimal(readNonEmptyString("Lương tối đa: "));
        } while (!JobPositionValidator.isValidSalaryRange(min, max));

        int exp;
        do {
            exp = readInt("Kinh nghiệm tối thiểu (năm): ");
        } while (!JobPositionValidator.isValidExperience(exp));

        LocalDate created = LocalDate.now();
        String expiredStr;
        do {
            expiredStr = readNonEmptyString("Ngày hết hạn (yyyy-MM-dd): ");
        } while (!JobPositionValidator.isValidDates(created, expiredStr));
        LocalDate expired = LocalDate.parse(expiredStr);

        List<Integer> techIds;
        do {
            String input = readNonEmptyString("IDs công nghệ (phân tách dấu phẩy, ít nhất 1): ");
            String[] parts = input.split(",");
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

        RecruitmentPosition rp = new RecruitmentPosition(
                0,
                name,
                desc,
                min,
                max,
                exp,
                Date.valueOf(created),
                Date.valueOf(expired)
        );
        int newId = service.addPosition(rp, techIds);
        System.out.println(newId > 0
                ? ">>> Thêm thành công! ID mới = " + newId
                : ">>> Lỗi khi thêm.");
    }

    private static void update() {
        int id = readInt("ID cần sửa: ");

        String name;
        do {
            name = readNonEmptyString("Tên mới: ");
        } while (!JobPositionValidator.isValidName(name));

        String desc = readNonEmptyString("Mô tả mới (có thể để trống): ");

        BigDecimal min, max;
        do {
            min = new BigDecimal(readNonEmptyString("Lương tối thiểu mới: "));
            max = new BigDecimal(readNonEmptyString("Lương tối đa mới: "));
        } while (!JobPositionValidator.isValidSalaryRange(min, max));

        int exp;
        do {
            exp = readInt("Kinh nghiệm mới (năm): ");
        } while (!JobPositionValidator.isValidExperience(exp));

        LocalDate created = LocalDate.now();
        String expiredStr;
        do {
            expiredStr = readNonEmptyString("Ngày hết hạn mới (yyyy-MM-dd): ");
        } while (!JobPositionValidator.isValidDates(created, expiredStr));
        LocalDate expired = LocalDate.parse(expiredStr);

        List<Integer> techIds;
        do {
            String input = readNonEmptyString("IDs công nghệ mới (phân tách dấu phẩy): ");
            String[] parts = input.split(",");
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

        RecruitmentPosition rp = new RecruitmentPosition(
                id,
                name,
                desc,
                min,
                max,
                exp,
                Date.valueOf(created),
                Date.valueOf(expired)
        );
        boolean success = service.updatePosition(rp, techIds);
        System.out.println(success
                ? ">>> Cập nhật thành công!"
                : ">>> Lỗi khi cập nhật.");
    }

    private static void delete() {
        int id = readInt("ID cần xóa: ");
        boolean success = service.deletePosition(id);
        System.out.println(success
                ? ">>> Xóa thành công!"
                : ">>> Lỗi khi xóa.");
    }
}

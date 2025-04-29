package ra.edu.presentation.admin;

import ra.edu.business.model.Candidate;
import ra.edu.business.service.admin.candidate.CandidateServiceImpl;
import ra.edu.utils.DataFormatter;

import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class CandidateUI {

    private static final CandidateServiceImpl candidateService = new CandidateServiceImpl();
    private static final int PAGE_SIZE = 10;

    public static void showMenu() {
        while (true) {
            try {
                System.out.println("\n===== MENU QUẢN LÝ ỨNG VIÊN =====");
                System.out.println("1. Hiển thị danh sách ứng viên");
                System.out.println("2. Khoá/Mở khoá tài khoản ứng viên");
                System.out.println("3. Reset mật khẩu ứng viên");
                System.out.println("4. Tìm kiếm ứng viên theo tên");
                System.out.println("5. Lọc ứng viên theo kinh nghiệm");
                System.out.println("6. Lọc ứng viên theo tuổi");
                System.out.println("7. Lọc ứng viên theo giới tính");
                System.out.println("8. Lọc ứng viên theo công nghệ");
                System.out.println("0. Quay về menu chính");
                int choice = readInt("Chọn: ");
                switch (choice) {
                    case 1: displayCandidates();             break;
                    case 2: lockUnlockAccount();            break;
                    case 3: resetPassword();                break;
                    case 4: searchCandidateByName();        break;
                    case 5: filterCandidatesByExperience(); break;
                    case 6: filterCandidatesByAge();        break;
                    case 7: filterCandidatesByGender();     break;
                    case 8: filterCandidatesByTechnology(); break;
                    case 0: return;
                    default: System.out.println(">>> Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) {
                System.out.println(">>> Đã xảy ra lỗi: " + e.getMessage());
            }
        }
    }

    private static void displayCandidates() {
        try {
            String[] headers = { "ID", "Tên", "Email", "SĐT", "Kinh nghiệm", "Giới tính", "Ngày sinh" };
            BiFunction<Integer,Integer,List<Candidate>> fetchPage =
                    (page, size) -> candidateService.getCandidates(page, size);
            IntSupplier totalCount = candidateService::countCandidates;
            Function<Candidate,String[]> mapper = c -> new String[]{
                    String.valueOf(c.getId()),
                    c.getName(),
                    c.getEmail(),
                    c.getPhone(),
                    String.valueOf(c.getExperience()),
                    c.getGender(),
                    c.getDob().toString()
            };

            DataFormatter.printInteractiveTable(
                    headers,
                    fetchPage,
                    totalCount,
                    mapper,
                    PAGE_SIZE
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi hiển thị danh sách ứng viên: " + e.getMessage());
        }
    }

    private static void lockUnlockAccount() {
        try {
            int candidateId = readInt("Nhập ID ứng viên: ");
            String status = readNonEmptyString("Nhập trạng thái (active/locked): ");
            boolean ok = candidateService.lockUnlockAccount(candidateId, status);
            System.out.println(ok
                    ? ">>> Thay đổi trạng thái thành công."
                    : ">>> Lỗi khi thay đổi trạng thái.");
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi khoá/mở khoá tài khoản: " + e.getMessage());
        }
    }

    private static void resetPassword() {
        try {
            int candidateId = readInt("Nhập ID ứng viên: ");
            String newPwd = candidateService.resetPassword(candidateId);
            if (newPwd != null) {
                System.out.println(">>> Mật khẩu mới: " + newPwd);
            } else {
                System.out.println(">>> Lỗi khi reset mật khẩu.");
            }
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi reset mật khẩu: " + e.getMessage());
        }
    }

    private static void searchCandidateByName() {
        try {
            String name = readNonEmptyString("Nhập tên ứng viên cần tìm: ");
            String[] headers = { "ID", "Tên", "Email" };
            List<Candidate> list = candidateService.searchCandidateByName(name);
            DataFormatter.printTable(
                    headers,
                    list,
                    c -> new String[]{
                            String.valueOf(c.getId()),
                            c.getName(),
                            c.getEmail()
                    }
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi tìm kiếm ứng viên: " + e.getMessage());
        }
    }

    private static void filterCandidatesByExperience() {
        try {
            int exp = readInt("Nhập mức kinh nghiệm (>=): ");
            String[] headers = { "ID", "Tên", "Kinh nghiệm" };
            List<Candidate> list = candidateService.filterCandidatesByExperience(exp);
            DataFormatter.printTable(
                    headers,
                    list,
                    c -> new String[]{
                            String.valueOf(c.getId()),
                            c.getName(),
                            String.valueOf(c.getExperience())
                    }
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi lọc theo kinh nghiệm: " + e.getMessage());
        }
    }

    private static void filterCandidatesByAge() {
        try {
            int age = readInt("Nhập độ tuổi tối thiểu: ");
            String[] headers = { "ID", "Tên", "Tuổi" };
            List<Candidate> list = candidateService.filterCandidatesByAge(age);
            DataFormatter.printTable(
                    headers,
                    list,
                    c -> new String[]{
                            String.valueOf(c.getId()),
                            c.getName(),
                            String.valueOf(Period.between(c.getDob().toLocalDate(), LocalDate.now()).getYears())
                    }
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi lọc theo tuổi: " + e.getMessage());
        }
    }

    private static void filterCandidatesByGender() {
        try {
            String gender = readNonEmptyString("Nhập giới tính (Nam/Nữ): ");
            String[] headers = { "ID", "Tên", "Giới tính" };
            List<Candidate> list = candidateService.filterCandidatesByGender(gender);
            DataFormatter.printTable(
                    headers,
                    list,
                    c -> new String[]{
                            String.valueOf(c.getId()),
                            c.getName(),
                            c.getGender()
                    }
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi lọc theo giới tính: " + e.getMessage());
        }
    }

    private static void filterCandidatesByTechnology() {
        try {
            int techId = readInt("Nhập ID công nghệ: ");
            String[] headers = { "ID", "Tên" };
            List<Candidate> list = candidateService.filterCandidatesByTechnology(techId);
            DataFormatter.printTable(
                    headers,
                    list,
                    c -> new String[]{
                            String.valueOf(c.getId()),
                            c.getName()
                    }
            );
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi lọc theo công nghệ: " + e.getMessage());
        }
    }
}

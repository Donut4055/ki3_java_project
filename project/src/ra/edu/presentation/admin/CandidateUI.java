package ra.edu.presentation.admin;

import ra.edu.business.model.Candidate;
import ra.edu.business.service.admin.candidate.CandidateServiceImpl;
import ra.edu.utils.DataFormatter;
import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class CandidateUI {

    private static final CandidateServiceImpl candidateService = new CandidateServiceImpl();
    private static final int PAGE_SIZE = 10;

    public static void showMenu() {
        while (true) {
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
                case 1: displayCandidates();          break;
                case 2: lockUnlockAccount();         break;
                case 3: resetPassword();             break;
                case 4: searchCandidateByName();     break;
                case 5: filterCandidatesByExperience(); break;
                case 6: filterCandidatesByAge();     break;
                case 7: filterCandidatesByGender();  break;
                case 8: filterCandidatesByTechnology(); break;
                case 0: return;
                default: System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void displayCandidates() {
        String[] headers = { "ID", "Tên", "Email", "SĐT", "Kinh nghiệm", "Giới tính", "Ngày sinh" };

        BiFunction<Integer,Integer,List<Candidate>> fetchPage =
                (page, size) -> candidateService.getCandidates(page, size);
        IntSupplier totalCount = () -> candidateService.countCandidates();
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
    }

    private static void lockUnlockAccount() {
        int candidateId = readInt("Nhập ID ứng viên: ");
        String status = readNonEmptyString("Nhập trạng thái (active/locked): ");
        boolean ok = candidateService.lockUnlockAccount(candidateId, status);
        System.out.println(ok
                ? ">>> Thay đổi trạng thái thành công."
                : ">>> Lỗi khi thay đổi trạng thái.");
    }

    private static void resetPassword() {
        int candidateId = readInt("Nhập ID ứng viên: ");
        String newPwd = candidateService.resetPassword(candidateId);
        if (newPwd != null) {
            System.out.println(">>> Mật khẩu mới: " + newPwd);
        } else {
            System.out.println(">>> Lỗi khi reset mật khẩu.");
        }
    }

    private static void searchCandidateByName() {
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
    }

    private static void filterCandidatesByExperience() {
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
    }

    private static void filterCandidatesByAge() {
        int age = readInt("Nhập độ tuổi tối thiểu: ");
        String[] headers = { "ID", "Tên", "Tuổi" };

        List<Candidate> list = candidateService.filterCandidatesByAge(age);
        DataFormatter.printTable(
                headers,
                list,
                c -> new String[]{
                        String.valueOf(c.getId()),
                        c.getName(),
                }
        );
    }

    private static void filterCandidatesByGender() {
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
    }

    private static void filterCandidatesByTechnology() {
        int techId = readInt("Nhập ID công nghệ: ");
        String[] headers = { "ID", "Tên", "Công nghệ" };

        List<Candidate> list = candidateService.filterCandidatesByTechnology(techId);
        DataFormatter.printTable(
                headers,
                list,
                c -> new String[]{
                        String.valueOf(c.getId()),
                        c.getName(),
                }
        );
    }
}

package ra.edu.presentation.admin;

import ra.edu.business.model.Candidate;
import ra.edu.business.service.admin.candidate.CandidateServiceImpl;
import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

import java.util.List;

public class CandidateUI {

    private static final CandidateServiceImpl candidateService = new CandidateServiceImpl();

    // Hiển thị menu quản lý ứng viên
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
                case 1:
                    displayCandidates();
                    break;
                case 2:
                    lockUnlockAccount();
                    break;
                case 3:
                    resetPassword();
                    break;
                case 4:
                    searchCandidateByName();
                    break;
                case 5:
                    filterCandidatesByExperience();
                    break;
                case 6:
                    filterCandidatesByAge();
                    break;
                case 7:
                    filterCandidatesByGender();
                    break;
                case 8:
                    filterCandidatesByTechnology();
                    break;
                case 0:
                    System.out.println(">>> Quay về menu chính.");
                    return;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }

    // Hiển thị danh sách ứng viên
    private static void displayCandidates() {
        System.out.println("===== DANH SÁCH ỨNG VIÊN =====");
        List<Candidate> candidates = candidateService.getCandidates(1, 10);
        candidates.forEach(c -> System.out.println(
                "ID: " + c.getId() + ", Tên: " + c.getName() + ", Email: " + c.getEmail()));
    }

    // Khoá/Mở khoá tài khoản ứng viên
    private static void lockUnlockAccount() {
        int candidateId = readInt("Nhập ID ứng viên: ");
        String status = readNonEmptyString("Nhập trạng thái (active/locked): ");

        if (candidateService.lockUnlockAccount(candidateId, status)) {
            System.out.println("Trạng thái tài khoản đã được thay đổi.");
        } else {
            System.out.println("Lỗi khi thay đổi trạng thái tài khoản.");
        }
    }

    // Reset mật khẩu ứng viên
    private static void resetPassword() {
        int candidateId = readInt("Nhập ID ứng viên: ");
        String newPassword = candidateService.resetPassword(candidateId);
        if (newPassword != null) {
            System.out.println("Mật khẩu mới của ứng viên là: " + newPassword);
        } else {
            System.out.println("Lỗi khi reset mật khẩu.");
        }
    }

    // Tìm kiếm ứng viên theo tên
    private static void searchCandidateByName() {
        String name = readNonEmptyString("Nhập tên ứng viên cần tìm: ");
        List<Candidate> candidates = candidateService.searchCandidateByName(name);
        if (!candidates.isEmpty()) {
            System.out.println("Tìm thấy " + candidates.size() + " ứng viên:");
            candidates.forEach(c -> System.out.println(
                    "ID: " + c.getId() + ", Tên: " + c.getName() + ", Email: " + c.getEmail()));
        } else {
            System.out.println("Không tìm thấy ứng viên.");
        }
    }

    // Lọc ứng viên theo kinh nghiệm
    private static void filterCandidatesByExperience() {
        int experience = readInt("Nhập mức kinh nghiệm (>=): ");
        List<Candidate> candidates = candidateService.filterCandidatesByExperience(experience);
        if (!candidates.isEmpty()) {
            System.out.println("Tìm thấy " + candidates.size() + " ứng viên:");
            candidates.forEach(c -> System.out.println(
                    "ID: " + c.getId() + ", Tên: " + c.getName() + ", Kinh nghiệm: " + c.getExperience()));
        } else {
            System.out.println("Không tìm thấy ứng viên với mức kinh nghiệm yêu cầu.");
        }
    }

    // Lọc ứng viên theo tuổi
    private static void filterCandidatesByAge() {
        int age = readInt("Nhập độ tuổi tối thiểu: ");
        List<Candidate> candidates = candidateService.filterCandidatesByAge(age);
        if (!candidates.isEmpty()) {
            System.out.println("Tìm thấy " + candidates.size() + " ứng viên:");
            candidates.forEach(c -> System.out.println(
                    "ID: " + c.getId() + ", Tên: " + c.getName()));
        } else {
            System.out.println("Không tìm thấy ứng viên với độ tuổi yêu cầu.");
        }
    }

    // Lọc ứng viên theo giới tính
    private static void filterCandidatesByGender() {
        String gender = readNonEmptyString("Nhập giới tính (Nam/Nữ): ");
        List<Candidate> candidates = candidateService.filterCandidatesByGender(gender);
        if (!candidates.isEmpty()) {
            System.out.println("Tìm thấy " + candidates.size() + " ứng viên:");
            candidates.forEach(c -> System.out.println(
                    "ID: " + c.getId() + ", Tên: " + c.getName() + ", Giới tính: " + c.getGender()));
        } else {
            System.out.println("Không tìm thấy ứng viên với giới tính yêu cầu.");
        }
    }

    // Lọc ứng viên theo công nghệ
    private static void filterCandidatesByTechnology() {
        int technologyId = readInt("Nhập ID công nghệ: ");
        List<Candidate> candidates = candidateService.filterCandidatesByTechnology(technologyId);
        if (!candidates.isEmpty()) {
            System.out.println("Tìm thấy " + candidates.size() + " ứng viên:");
            candidates.forEach(c -> System.out.println(
                    "ID: " + c.getId() + ", Tên: " + c.getName()));
        } else {
            System.out.println("Không tìm thấy ứng viên với công nghệ yêu cầu.");
        }
    }
}
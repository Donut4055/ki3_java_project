package ra.edu.presentation.admin;

import ra.edu.business.model.Candidate;
import ra.edu.business.service.admin.candidate.CandidateServiceImpl;

import java.util.List;
import java.util.Scanner;

public class CandidateUI {

    private static final Scanner scanner = new Scanner(System.in);
    private static final CandidateServiceImpl candidateService = new CandidateServiceImpl();

    // Hiển thị menu quản lý ứng viên
    public static void showMenu() {
        int choice;
        do {
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
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

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
//                    filterCandidatesByAge();
                    break;
                case 7:
                    filterCandidatesByGender();
                    break;
                case 8:
//                    filterCandidatesByTechnology();
                    break;
                case 0:
                    System.out.println(">>> Quay về menu chính.");
                    break;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }

    // Hiển thị danh sách ứng viên
    private static void displayCandidates() {
        System.out.println("===== DANH SÁCH ỨNG VIÊN =====");
        List<Candidate> candidates = candidateService.getCandidates(1, 10);  // Lấy danh sách ứng viên với phân trang (page 1, page size 10)
        for (Candidate candidate : candidates) {
            System.out.println("ID: " + candidate.getId() + ", Tên: " + candidate.getName() + ", Email: " + candidate.getEmail());
        }
    }

    // Khoá/Mở khoá tài khoản ứng viên
    private static void lockUnlockAccount() {
        System.out.print("Nhập ID ứng viên: ");
        int candidateId = Integer.parseInt(scanner.nextLine());
        System.out.print("Nhập trạng thái (active/locked): ");
        String status = scanner.nextLine();

        if (candidateService.lockUnlockAccount(candidateId, status)) {
            System.out.println("Trạng thái tài khoản đã được thay đổi.");
        } else {
            System.out.println("Lỗi khi thay đổi trạng thái tài khoản.");
        }
    }

    // Reset mật khẩu ứng viên
    private static void resetPassword() {
        System.out.print("Nhập ID ứng viên: ");
        int candidateId = Integer.parseInt(scanner.nextLine());

        String newPassword = candidateService.resetPassword(candidateId);
        if (newPassword != null) {
            System.out.println("Mật khẩu mới của ứng viên là: " + newPassword);
        } else {
            System.out.println("Lỗi khi reset mật khẩu.");
        }
    }

    // Tìm kiếm ứng viên theo tên
    private static void searchCandidateByName() {
        System.out.print("Nhập tên ứng viên cần tìm: ");
        String name = scanner.nextLine();

        List<Candidate> candidates = candidateService.searchCandidateByName(name);
        if (!candidates.isEmpty()) {
            System.out.println("Tìm thấy " + candidates.size() + " ứng viên:");
            for (Candidate candidate : candidates) {
                System.out.println("ID: " + candidate.getId() + ", Tên: " + candidate.getName() + ", Email: " + candidate.getEmail());
            }
        } else {
            System.out.println("Không tìm thấy ứng viên.");
        }
    }

    // Lọc ứng viên theo kinh nghiệm
    private static void filterCandidatesByExperience() {
        System.out.print("Nhập mức kinh nghiệm (>=): ");
        int experience = Integer.parseInt(scanner.nextLine());

        List<Candidate> candidates = candidateService.filterCandidatesByExperience(experience);
        if (!candidates.isEmpty()) {
            System.out.println("Tìm thấy " + candidates.size() + " ứng viên:");
            for (Candidate candidate : candidates) {
                System.out.println("ID: " + candidate.getId() + ", Tên: " + candidate.getName() + ", Kinh nghiệm: " + candidate.getExperience());
            }
        } else {
            System.out.println("Không tìm thấy ứng viên với mức kinh nghiệm yêu cầu.");
        }
    }

    // Lọc ứng viên theo tuổi
//    private static void filterCandidatesByAge() {
//        System.out.print("Nhập độ tuổi tối thiểu: ");
//        int age = Integer.parseInt(scanner.nextLine());
//
//        List<Candidate> candidates = candidateService.filterCandidatesByAge(age);
//        if (!candidates.isEmpty()) {
//            System.out.println("Tìm thấy " + candidates.size() + " ứng viên:");
//            for (Candidate candidate : candidates) {
//                System.out.println("ID: " + candidate.getId() + ", Tên: " + candidate.getName() );
//            }
//        } else {
//            System.out.println("Không tìm thấy ứng viên với độ tuổi yêu cầu.");
//        }
//    }

    // Lọc ứng viên theo giới tính
    private static void filterCandidatesByGender() {
        System.out.print("Nhập giới tính (Nam/Nữ): ");
        String gender = scanner.nextLine();

        List<Candidate> candidates = candidateService.filterCandidatesByGender(gender);
        if (!candidates.isEmpty()) {
            System.out.println("Tìm thấy " + candidates.size() + " ứng viên:");
            for (Candidate candidate : candidates) {
                System.out.println("ID: " + candidate.getId() + ", Tên: " + candidate.getName() + ", Giới tính: " + candidate.getGender());
            }
        } else {
            System.out.println("Không tìm thấy ứng viên với giới tính yêu cầu.");
        }
    }

    // Lọc ứng viên theo công nghệ
//    private static void filterCandidatesByTechnology() {
//        System.out.print("Nhập ID công nghệ: ");
//        int technologyId = Integer.parseInt(scanner.nextLine());
//
//        List<Candidate> candidates = candidateService.filterCandidatesByTechnology(technologyId);
//        if (!candidates.isEmpty()) {
//            System.out.println("Tìm thấy " + candidates.size() + " ứng viên:");
//            for (Candidate candidate : candidates) {
//                System.out.println("ID: " + candidate.getId() + ", Tên: " + candidate.getName() + ", Công nghệ: " + candidate.getTechnology());
//            }
//        } else {
//            System.out.println("Không tìm thấy ứng viên với công nghệ yêu cầu.");
//        }
    }


package ra.edu.presentation.user;

import ra.edu.business.model.Candidate;
import ra.edu.business.service.user.personalInfo.IPersonalIInfoService;
import ra.edu.business.service.user.personalInfo.IPersonalIInfoServiceImpl;
import ra.edu.validate.UserValidator;
import ra.edu.validate.Validator;

import static ra.edu.utils.InputUtils.readInt;
import static ra.edu.utils.InputUtils.readNonEmptyString;

public class PersonalInfoUI {
    private static final IPersonalIInfoService service = new IPersonalIInfoServiceImpl();
    private static int candidateId;

    public static void showMenu(int loggedInCandidateId) {
        candidateId = loggedInCandidateId;
        while (true) {
            System.out.println("\n===== THÔNG TIN CÁ NHÂN =====");
            System.out.println("1. Xem thông tin cá nhân");
            System.out.println("2. Cập nhật thông tin");
            System.out.println("3. Đổi mật khẩu");
            System.out.println("0. Quay lại");
            int choice = readInt("Chọn: ");
            switch (choice) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    updateProfile();
                    break;
                case 3:
                    changePassword();
                    break;
                case 0:
                    System.out.println(">>> Quay lại menu.");
                    return;
                default:
                    System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void viewProfile() {
        try {
            Candidate c = service.getCandidateProfile(candidateId);
            if (c == null) {
                System.out.println(">>> Không tìm thấy thông tin cá nhân.");
                return;
            }
            System.out.println("----- THÔNG TIN ỨNG VIÊN -----");
            System.out.printf("ID           : %d%n", c.getId());
            System.out.printf("Họ tên       : %s%n", c.getName());
            System.out.printf("Email        : %s%n", c.getEmail());
            System.out.printf("Số điện thoại: %s%n", c.getPhone());
            System.out.printf("Giới tính    : %s%n", c.getGender());
            System.out.printf("Ngày sinh    : %s%n", c.getDob());
            System.out.printf("Kinh nghiệm  : %d năm%n", c.getExperience());
            System.out.printf("Trạng thái   : %s%n", c.getStatus());
            System.out.printf("Mô tả        : %s%n", c.getDescription());
        } catch (Exception e) {
            System.out.println(">>> Lỗi khi lấy thông tin cá nhân: " + e.getMessage());
        }
    }

    private static void updateProfile() {
        try {
            String name;
            do {
                name = readNonEmptyString("Tên mới: ");
                if (!UserValidator.isValidUsername(name)) {
                    System.out.println(">>> Tên không hợp lệ (≥4 ký tự).");
                }
            } while (!UserValidator.isValidUsername(name));

            String email;
            do {
                email = readNonEmptyString("Email mới: ");
                if (!Validator.isValidEmail(email)) {
                    System.out.println(">>> Email không đúng định dạng.");
                }
            } while (!Validator.isValidEmail(email));

            String phone;
            do {
                phone = readNonEmptyString("SĐT mới: ");
                if (!Validator.isValidPhone(phone)) {
                    System.out.println(">>> Số điện thoại không hợp lệ.");
                }
            } while (!Validator.isValidPhone(phone));

            String gender;
            do {
                gender = readNonEmptyString("Giới tính (Nam/Nữ): ");
                if (!gender.equalsIgnoreCase("Nam") && !gender.equalsIgnoreCase("Nữ")) {
                    System.out.println(">>> Giới tính phải là 'Nam' hoặc 'Nữ'.");
                }
            } while (!gender.equalsIgnoreCase("Nam") && !gender.equalsIgnoreCase("Nữ"));

            String dob;
            do {
                dob = readNonEmptyString("Ngày sinh (yyyy-MM-dd): ");
                if (!Validator.isValidDate(dob)) {
                    System.out.println(">>> Ngày sinh không đúng định dạng.");
                }
            } while (!Validator.isValidDate(dob));

            String desc;
            do {
                desc = readNonEmptyString("Giới thiệu bản thân: ");
                if (desc.trim().isEmpty()) {
                    System.out.println(">>> Mô tả không được để trống.");
                }
            } while (desc.trim().isEmpty());

            boolean ok = service.updateUserProfile(
                    candidateId, name, phone, gender, dob, desc, email
            );
            System.out.println(ok
                    ? ">>> Cập nhật thông tin thành công."
                    : ">>> Lỗi khi cập nhật thông tin.");
        } catch (Exception e) {
            System.out.println(">>> Lỗi bất ngờ khi cập nhật: " + e.getMessage());
        }
    }

    private static void changePassword() {
        while (true) {
            try {
                String oldPass = readNonEmptyString("Mật khẩu hiện tại: ");
                String newPass = readNonEmptyString("Mật khẩu mới: ");
                if (!UserValidator.isValidPassword(newPass)) {
                    System.out.println(">>> Mật khẩu mới phải ≥6 ký tự.");
                    continue;
                }
                boolean ok = service.changePassword(candidateId, oldPass, newPass);
                if (ok) {
                    System.out.println(">>> Đổi mật khẩu thành công.");
                    break;
                } else {
                    System.out.println(">>> Mật khẩu hiện tại không đúng. Thử lại.");
                }
            } catch (Exception e) {
                System.out.println(">>> Lỗi bất ngờ khi đổi mật khẩu: " + e.getMessage());
            }
        }
    }
}

package ra.edu.business.service.user.personalInfo;


import ra.edu.business.model.Candidate;

public interface IPersonalIInfoService {
    boolean updateUserProfile(int candidateId, String name, String phone, String gender, String dob, String description, String email);
    boolean changePassword(int candidateId, String oldPassword, String newPassword);
    Candidate getCandidateProfile(int candidateId);
}

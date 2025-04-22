package ra.edu.business.dao.user.personalInfo;


public interface IPersonalIInfoDAO {
    boolean updateCandidateProfile(int candidateId, String name, String email, String phone, String gender, String dob, String description);
    boolean changeCandidatePassword(int candidateId, String oldPassword, String newPassword);
}

package ra.edu.business.service.user.personalInfo;

import ra.edu.business.dao.user.personalInfo.IPersonalIInfoDAO;
import ra.edu.business.dao.user.personalInfo.IPersonalIInfoDAOImpl;
import ra.edu.business.model.Candidate;

public class IPersonalIInfoServiceImpl implements IPersonalIInfoService {
    private final IPersonalIInfoDAO dao = new IPersonalIInfoDAOImpl();

    @Override
    public Candidate getCandidateProfile(int candidateId) {
        return dao.getCandidateProfile(candidateId);
    }

    @Override
    public boolean updateUserProfile(int candidateId,
                                     String name,
                                     String phone,
                                     String gender,
                                     String dob,
                                     String description,
                                     String email) {
        return dao.updateCandidateProfile(candidateId, name, email, phone, gender, dob, description);
    }

    @Override
    public boolean changePassword(int candidateId,
                                  String oldPassword,
                                  String newPassword) {
        return dao.changeCandidatePassword(candidateId, oldPassword, newPassword);
    }
}

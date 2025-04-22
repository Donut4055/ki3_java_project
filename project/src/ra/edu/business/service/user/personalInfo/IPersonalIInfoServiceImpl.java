package ra.edu.business.service.user.personalInfo;

import ra.edu.business.dao.user.personalInfo.IPersonalIInfoDAO;
import ra.edu.business.dao.user.personalInfo.IPersonalIInfoDAOImpl;

public class IPersonalIInfoServiceImpl implements IPersonalIInfoService {
    private IPersonalIInfoDAO dao = new IPersonalIInfoDAOImpl();

    @Override
    public boolean updateUserProfile(int candidateId, String name, String phone, String gender, String dob, String description, String email) {
        return dao.updateCandidateProfile(candidateId, name, email, phone, gender, dob, description);
    }

    @Override
    public boolean changePassword(int candidateId, String oldPassword, String newPassword) {
        return dao.changeCandidatePassword(candidateId, oldPassword, newPassword);
    }
}

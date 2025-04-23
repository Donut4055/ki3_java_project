package ra.edu.business.service.user.application;

import ra.edu.business.dao.user.application.ApplicationDAOImpl;
import ra.edu.business.dao.user.application.IApplicationDAO;
import ra.edu.business.model.Application;
import ra.edu.business.model.RecruitmentPosition;
import java.util.List;

public class ApplicationServiceImpl implements IApplicationService {
    private final IApplicationDAO dao = new ApplicationDAOImpl();

    @Override
    public List<Application> getSubmittedApplications(int candidateId, int page, int size) {
        return dao.getSubmittedApplications(candidateId, page, size);
    }

    @Override
    public Application getApplicationDetails(int applicationId) {
        return dao.getApplicationDetails(applicationId);
    }

    @Override
    public List<RecruitmentPosition> getActivePositions(int page, int size) {
        return dao.getActivePositions(page, size);
    }

    @Override
    public RecruitmentPosition getPositionDetails(int positionId) {
        return dao.getPositionDetails(positionId);
    }

    @Override
    public boolean submitApplication(int candidateId, int positionId, String cvUrl) {
        return dao.submitApplication(candidateId, positionId, cvUrl);
    }

    @Override
    public int countSubmittedApplications(int candidateId) {
        return dao.countSubmittedApplications(candidateId);
    }

    @Override
    public boolean respondToInterview(int appId, boolean confirm) {
        return dao.respondToInterview(appId, confirm);
    }

    @Override
    public int countActivePositions() {
        return dao.countActivePositions();
    }
}
package ra.edu.business.dao.user.application;

import ra.edu.business.model.Application;
import ra.edu.business.model.RecruitmentPosition;
import java.util.List;

public interface IApplicationDAO {
    List<Application> getSubmittedApplications(int candidateId, int page, int size);
    Application getApplicationDetails(int applicationId);
    List<RecruitmentPosition> getActivePositions(int page, int size);
    RecruitmentPosition getPositionDetails(int positionId);
    boolean submitApplication(int candidateId, int positionId, String cvUrl);
    int countSubmittedApplications(int candidateId);
    /** Ứng viên xác nhận/ từ chối phỏng vấn */
    boolean respondToInterview(int appId, boolean confirm);
    int countActivePositions();
}

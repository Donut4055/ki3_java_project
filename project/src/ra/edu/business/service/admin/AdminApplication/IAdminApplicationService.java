package ra.edu.business.service.admin.AdminApplication;

import ra.edu.business.model.Application;
import java.sql.Timestamp;
import java.util.List;

public interface IAdminApplicationService {
    List<Application> listApplications(int page, int size);
    List<Application> filterByProgress(String progress, int page, int size);
    List<Application> filterByResult(String result, int page, int size);
    boolean cancelApplication(int appId, String reason);
    Application viewApplication(int appId);
    boolean setInterview(int appId, String link, Timestamp time);
    boolean updateResult(int appId, String result, String note);
    int countApplications();
}
package ra.edu.business.service.admin.AdminApplication;

import ra.edu.business.dao.admin.AdminApplication.AdminApplicationDAOImpl;
import ra.edu.business.dao.admin.AdminApplication.IAdminApplicationDAO;
import ra.edu.business.model.Application;
import java.sql.Timestamp;
import java.util.List;

public class AdminApplicationServiceImpl implements IAdminApplicationService {
    private final IAdminApplicationDAO dao = new AdminApplicationDAOImpl();

    @Override
    public List<Application> listApplications(int p, int s) {
        return dao.listApplications(p, s);
    }

    @Override
    public List<Application> filterByProgress(String p, int pg, int sz) {
        return dao.filterByProgress(p, pg, sz);
    }

    @Override
    public List<Application> filterByResult(String r, int pg, int sz) {
        return dao.filterByResult(r, pg, sz);
    }

    @Override
    public boolean cancelApplication(int id, String reason) {
        return dao.cancelApplication(id, reason);
    }

    @Override
    public Application viewApplication(int id) {
        return dao.viewApplication(id);
    }

    @Override
    public boolean setInterview(int id, String link, Timestamp t) {
        return dao.setInterview(id, link, t);
    }

    @Override
    public boolean updateResult(int id, String result, String note) {
        return dao.updateResult(id, result, note);
    }

    @Override
    public int countApplications() {
        return dao.countApplications();
    }
}
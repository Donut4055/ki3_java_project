package ra.edu.business.service.admin.recruitment;

import ra.edu.business.dao.admin.recruitment.IRecruitmentPositionDAO;
import ra.edu.business.dao.admin.recruitment.RecruitmentPositionDAOImpl;
import ra.edu.business.model.RecruitmentPosition;

import java.util.List;

public class RecruitmentPositionServiceImpl implements IRecruitmentPositionService {

    private IRecruitmentPositionDAO dao = new RecruitmentPositionDAOImpl();

    @Override
    public List<RecruitmentPosition> getPositions(int page, int size) {
        return dao.getPositions(page, size);
    }

    @Override
    public int addPosition(RecruitmentPosition rp, List<Integer> techIds) {
        return dao.addPosition(rp, techIds);
    }

    @Override
    public boolean updatePosition(RecruitmentPosition rp, List<Integer> techIds) {
        return dao.updatePosition(rp, techIds);
    }

    @Override
    public boolean deletePosition(int id) {
        return dao.deletePosition(id);
    }

    @Override
    public int countPositions() {
        return dao.countPositions();
    }
}

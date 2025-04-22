package ra.edu.business.dao.admin.recruitment;

import ra.edu.business.model.RecruitmentPosition;
import java.util.List;

public interface IRecruitmentPositionDAO {
    List<RecruitmentPosition> getPositions(int page, int size);
    int addPosition(RecruitmentPosition rp, List<Integer> techIds);
    boolean updatePosition(RecruitmentPosition rp, List<Integer> techIds);
    boolean deletePosition(int id);
}

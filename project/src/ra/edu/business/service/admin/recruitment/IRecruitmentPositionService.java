package ra.edu.business.service.admin.recruitment;

import ra.edu.business.model.RecruitmentPosition;
import java.util.List;

public interface IRecruitmentPositionService {
    List<RecruitmentPosition> getPositions(int page, int size);
    int addPosition(RecruitmentPosition rp, List<Integer> techIds);
    boolean updatePosition(RecruitmentPosition rp, List<Integer> techIds);
    boolean deletePosition(int id);
}

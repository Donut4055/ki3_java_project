package ra.edu.business.dao.admin.technology;

import ra.edu.business.model.Technology;
import java.util.List;

public interface ITechnologyDAO {
    List<Technology> getTechnologies(int pageNumber, int pageSize);
    boolean addTechnology(Technology technology);
    boolean updateTechnology(int id, String newName);
    boolean deleteTechnology(int id);

    boolean isTechnologyNameExist(String technologyName);
    int countTechnologies();
}

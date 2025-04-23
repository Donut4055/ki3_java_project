package ra.edu.business.service.admin.technology;


import ra.edu.business.model.Technology;
import java.util.List;

public interface ITechnologyService {
    List<Technology> getTechnologies(int pageNumber, int pageSize);
    boolean addTechnology(String technologyName);
    boolean updateTechnology(int id, String newName);
    boolean deleteTechnology(int id);
    int countTechnologies();
}


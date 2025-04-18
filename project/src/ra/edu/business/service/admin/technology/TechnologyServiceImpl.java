package ra.edu.business.service.admin.technology;



import ra.edu.business.dao.admin.technology.ITechnologyDAO;
import ra.edu.business.dao.admin.technology.TechnologyDAOImpl;
import ra.edu.business.model.Technology;

import java.util.List;

public class TechnologyServiceImpl implements ITechnologyService {

    private ITechnologyDAO technologyDAO = new TechnologyDAOImpl();

    @Override
    public List<Technology> getTechnologies(int pageNumber, int pageSize) {
        return technologyDAO.getTechnologies(pageNumber, pageSize);
    }

    @Override
    public boolean addTechnology(String technologyName) {
        Technology technology = new Technology(0, technologyName);
        return technologyDAO.addTechnology(technology);
    }

    @Override
    public boolean updateTechnology(int id, String newName) {
        return technologyDAO.updateTechnology(id, newName);
    }

    @Override
    public boolean deleteTechnology(int id) {
        return technologyDAO.deleteTechnology(id);
    }
}


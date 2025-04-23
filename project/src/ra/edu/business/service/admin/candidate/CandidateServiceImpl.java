package ra.edu.business.service.admin.candidate;

import ra.edu.business.dao.admin.candidate.CandidateDAOImpl;
import ra.edu.business.dao.admin.candidate.ICandidateDAO;
import ra.edu.business.model.Candidate;
import java.util.List;

public class CandidateServiceImpl implements ICandidateService {

    private ICandidateDAO candidateDAO = new CandidateDAOImpl();

    @Override
    public List<Candidate> getCandidates(int pageNumber, int pageSize) {
        return candidateDAO.getCandidates(pageNumber, pageSize);
    }

    @Override
    public boolean lockUnlockAccount(int candidateId, String status) {
        return candidateDAO.lockUnlockAccount(candidateId, status);
    }

    @Override
    public String resetPassword(int candidateId) {
        return candidateDAO.resetPassword(candidateId);
    }

    @Override
    public List<Candidate> searchCandidateByName(String name) {
        return candidateDAO.searchCandidateByName(name);
    }

    @Override
    public List<Candidate> filterCandidatesByExperience(int experience) {
        return candidateDAO.filterCandidatesByExperience(experience);
    }

    @Override
    public List<Candidate> filterCandidatesByAge(int age) {
        return candidateDAO.filterCandidatesByAge(age);
    }

    @Override
    public List<Candidate> filterCandidatesByGender(String gender) {
        return candidateDAO.filterCandidatesByGender(gender);
    }

    @Override
    public List<Candidate> filterCandidatesByTechnology(int technologyId) {
        return candidateDAO.filterCandidatesByTechnology(technologyId);
    }

    @Override
    public int countCandidates() {
        return candidateDAO.countCandidates();
    }
}

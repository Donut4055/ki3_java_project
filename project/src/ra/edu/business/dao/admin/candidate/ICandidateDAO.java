package ra.edu.business.dao.admin.candidate;
import ra.edu.business.model.Candidate;
import java.util.List;

public interface ICandidateDAO {
    List<Candidate> getCandidates(int pageNumber, int pageSize);
    boolean lockUnlockAccount(int candidateId, String status);
    String resetPassword(int candidateId);
    List<Candidate> searchCandidateByName(String name);
    List<Candidate> filterCandidatesByExperience(int experience);
    List<Candidate> filterCandidatesByAge(int age);
    List<Candidate> filterCandidatesByGender(String gender);
    List<Candidate> filterCandidatesByTechnology(int technologyId);
    int countCandidates();
}

package ra.edu.business.model;

public class CandidateTechnology {
    private int candidateId;
    private int technologyId;

    public CandidateTechnology() {
    }

    public CandidateTechnology(int candidateId, int technologyId) {
        this.candidateId = candidateId;
        this.technologyId = technologyId;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public int getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(int technologyId) {
        this.technologyId = technologyId;
    }

    @Override
    public String toString() {
        return "CandidateTechnology{" +
                "candidateId=" + candidateId +
                ", technologyId=" + technologyId +
                '}';
    }
}

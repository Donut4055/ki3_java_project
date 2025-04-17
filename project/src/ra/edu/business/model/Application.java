package ra.edu.business.model;

import java.sql.Timestamp;

public class Application {
    private int id;
    private int candidateId;
    private int recruitmentPositionId;
    private String cvUrl;
    private String progress;
    private Timestamp interviewRequestDate;
    private String interviewRequestResult;
    private String interviewLink;
    private Timestamp interviewTime;
    private String interviewResult;
    private String interviewResultNote;
    private Timestamp destroyAt;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String destroyReason;

    public Application() {
    }

    public Application(int id, int candidateId, int recruitmentPositionId, String cvUrl, String progress, Timestamp interviewRequestDate,
                       String interviewRequestResult, String interviewLink, Timestamp interviewTime,
                       String interviewResult, String interviewResultNote, Timestamp destroyAt,
                       Timestamp createAt, Timestamp updateAt, String destroyReason) {
        this.id = id;
        this.candidateId = candidateId;
        this.recruitmentPositionId = recruitmentPositionId;
        this.cvUrl = cvUrl;
        this.progress = progress;
        this.interviewRequestDate = interviewRequestDate;
        this.interviewRequestResult = interviewRequestResult;
        this.interviewLink = interviewLink;
        this.interviewTime = interviewTime;
        this.interviewResult = interviewResult;
        this.interviewResultNote = interviewResultNote;
        this.destroyAt = destroyAt;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.destroyReason = destroyReason;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public int getId() {
        return id;
    }

    public int getRecruitmentPositionId() {
        return recruitmentPositionId;
    }

    public Timestamp getInterviewRequestDate() {
        return interviewRequestDate;
    }

    public String getDestroyReason() {
        return destroyReason;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public Timestamp getDestroyAt() {
        return destroyAt;
    }

    public String getInterviewResultNote() {
        return interviewResultNote;
    }

    public String getInterviewResult() {
        return interviewResult;
    }

    public Timestamp getInterviewTime() {
        return interviewTime;
    }

    public String getInterviewLink() {
        return interviewLink;
    }

    public String getInterviewRequestResult() {
        return interviewRequestResult;
    }

    public String getProgress() {
        return progress;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public void setRecruitmentPositionId(int recruitmentPositionId) {
        this.recruitmentPositionId = recruitmentPositionId;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setInterviewRequestDate(Timestamp interviewRequestDate) {
        this.interviewRequestDate = interviewRequestDate;
    }

    public void setInterviewRequestResult(String interviewRequestResult) {
        this.interviewRequestResult = interviewRequestResult;
    }

    public void setInterviewLink(String interviewLink) {
        this.interviewLink = interviewLink;
    }

    public void setInterviewTime(Timestamp interviewTime) {
        this.interviewTime = interviewTime;
    }

    public void setInterviewResult(String interviewResult) {
        this.interviewResult = interviewResult;
    }

    public void setInterviewResultNote(String interviewResultNote) {
        this.interviewResultNote = interviewResultNote;
    }

    public void setDestroyAt(Timestamp destroyAt) {
        this.destroyAt = destroyAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }

    public void setDestroyReason(String destroyReason) {
        this.destroyReason = destroyReason;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", candidateId=" + candidateId +
                ", recruitmentPositionId=" + recruitmentPositionId +
                ", progress='" + progress + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}

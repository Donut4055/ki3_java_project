package ra.edu.business.model;

import java.sql.Date;

public class Candidate {
    private int id;
    private String name;
    private String email;
    private String phone;
    private int experience;
    private String gender;
    private String status;
    private String description;
    private Date dob;

    public Candidate() {
    }

    public Candidate(int id, String name, String email, String phone, int experience, String gender, String status, String description, Date dob) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.experience = experience;
        this.gender = gender;
        this.status = status;
        this.description = description;
        this.dob = dob;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", experience=" + experience +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", dob=" + dob +
                '}';
    }
}

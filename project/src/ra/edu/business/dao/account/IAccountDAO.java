package ra.edu.business.dao.account;

import ra.edu.business.model.Account;

import java.util.Date;

public interface IAccountDAO {
    Account login(String username, String password, String role) throws Exception;
    boolean createUserAccount(String username, String password, String name, String email, String phone, String gender, String dob, String description, int experience);
    boolean saveUserInfo(int candidateId, String name, String email, String phone, String gender, int experience, String description, String dob);
}



package ra.edu.business.service.account;

import ra.edu.business.model.Account;

import java.util.Date;

public interface IAccountService {
    Account login(String username, String password, String role);
    boolean registerUser(String username, String password);
    boolean updateUserProfile(int candidateId, String name, String phone, String gender, Date dob, String description, String email);
}

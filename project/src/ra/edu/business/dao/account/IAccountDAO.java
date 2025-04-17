package ra.edu.business.dao.account;

import ra.edu.business.model.Account;

public interface IAccountDAO {
    Account login(String username, String password, String role) throws Exception;
}

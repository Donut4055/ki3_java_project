

package ra.edu.business.service.account;

import ra.edu.business.model.Account;

public interface IAccountService {
    Account login(String username, String password, String role);
}

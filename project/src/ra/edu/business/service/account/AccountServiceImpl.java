package ra.edu.business.service.account;


import ra.edu.business.dao.account.AccountDAOImpl;
import ra.edu.business.model.Account;

public class AccountServiceImpl implements IAccountService {
    private AccountDAOImpl accountDAOImpl;

    public AccountServiceImpl(AccountDAOImpl accountDAO) {
        this.accountDAOImpl = accountDAO;
    }

    @Override
    public Account login(String username, String password, String role) {
        try {
            return accountDAOImpl.login(username, password, role);
        } catch (Exception e) {
            System.out.println("Lỗi đăng nhập: " + e.getMessage());
            return null;
        }
    }
}

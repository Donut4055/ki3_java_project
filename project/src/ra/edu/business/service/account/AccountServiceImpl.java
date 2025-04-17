package ra.edu.business.service.account;


import ra.edu.business.dao.account.IAccountDAO;
import ra.edu.business.model.Account;

public class AccountServiceImpl implements IAccountService {
    private IAccountDAO accountDAO;

    public AccountServiceImpl(IAccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public Account login(String username, String password, String role) {
        try {
            return accountDAO.login(username, password, role);
        } catch (Exception e) {
            System.out.println("Lỗi đăng nhập: " + e.getMessage());
            return null;
        }
    }
}

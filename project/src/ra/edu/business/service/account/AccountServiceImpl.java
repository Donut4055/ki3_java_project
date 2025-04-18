package ra.edu.business.service.account;

import ra.edu.business.dao.account.AccountDAOImpl;
import ra.edu.business.model.Account;


public class AccountServiceImpl {

    private AccountDAOImpl accountDAO;

    public AccountServiceImpl(AccountDAOImpl accountDAO) {
        this.accountDAO = accountDAO;
    }

    public boolean registerUser(String username, String password, String name, String email, String phone, String gender, String dob, String description, int experience) {
        return accountDAO.createUserAccount(username, password, name, email, phone, gender, dob, description, experience);
    }

    public boolean saveUserInfo(int candidateId, String name, String email, String phone, String gender, int experience, String description, String dob) {
        return accountDAO.saveUserInfo(candidateId, name, email, phone, gender, experience, description, dob);
    }

    public Account login(String username, String password, String expectedRole) {
        return accountDAO.login(username, password, expectedRole);
    }
}

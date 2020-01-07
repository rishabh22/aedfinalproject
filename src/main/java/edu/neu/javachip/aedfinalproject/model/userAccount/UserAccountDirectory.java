package edu.neu.javachip.aedfinalproject.model.userAccount;

import edu.neu.javachip.aedfinalproject.model.employee.Employee;
import edu.neu.javachip.aedfinalproject.model.role.Role;

import java.util.ArrayList;
import java.util.List;

public class UserAccountDirectory {
    private List<UserAccount> userAccountList = new ArrayList<>();

    public List<UserAccount> getUserAccountList() {
        return userAccountList;
    }

    public UserAccount authenticateUser(String username, String password) {
        return userAccountList.stream()
                .filter(userAccount -> userAccount.getUsername().equalsIgnoreCase(username) &&
                        userAccount.getPassword().equals(password))
                .findFirst().orElse(null);
    }

    public UserAccount addUserAccount(UserAccount userAccount) {
        userAccountList.add(userAccount);
        return userAccount;

    }

    public UserAccount createUserAccount(String username, String password, Employee employee, Role role) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(username);
        userAccount.setPassword(password);
        userAccount.setEmployee(employee);
        userAccount.setRole(role);
        userAccountList.add(userAccount);
        return userAccount;
    }

    public void deleteUserAccount(UserAccount userAccount) {
        userAccountList.remove(userAccount);
    }

    public boolean checkIfUsernameIsUnique(String username) {
        return userAccountList.stream().noneMatch(userAccount -> userAccount.getUsername().equalsIgnoreCase(username));

    }

    public void deleteUserAccountsByEmployee(Employee employee) {
        userAccountList.removeIf(e -> e.getEmployee().equals(employee));
    }
}

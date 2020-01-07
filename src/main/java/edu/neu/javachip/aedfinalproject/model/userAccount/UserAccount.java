package edu.neu.javachip.aedfinalproject.model.userAccount;

import edu.neu.javachip.aedfinalproject.model.employee.Employee;
import edu.neu.javachip.aedfinalproject.model.role.Role;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Objects;
import edu.neu.javachip.aedfinalproject.model.workqueue.WorkQueue;

@Getter
@Setter
public class UserAccount {

    private String username;
    private String password;
    private Role role;
    private Employee employee;
    private WorkQueue workQueue = new WorkQueue();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, employee);
    }
}

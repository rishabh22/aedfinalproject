package edu.neu.javachip.aedfinalproject.model.employee;

import edu.neu.javachip.aedfinalproject.model.enterprise.Enterprise;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
public class Employee {

    private int employeeId;
    private String name;
    private long phoneNumber;
    private String email;
    private Organization organization;
    private Enterprise enterprise;
    private byte[] profilePic;
    private static int counter = 0;

    //This will be applicable across every instantiation
    {
        try {
            this.profilePic = IOUtils.toByteArray(this.getClass().getResourceAsStream("/images/default-profile-pic.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.employeeId = ++counter;
    }


    public Employee(String name) {
        this.name = name;
    }

    public Employee(String name, long phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employeeId == employee.employeeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }
}

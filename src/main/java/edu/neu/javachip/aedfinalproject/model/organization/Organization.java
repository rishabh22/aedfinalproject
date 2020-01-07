package edu.neu.javachip.aedfinalproject.model.organization;

import edu.neu.javachip.aedfinalproject.model.employee.EmployeeDirectory;
import edu.neu.javachip.aedfinalproject.model.enterprise.Enterprise;
import edu.neu.javachip.aedfinalproject.model.role.Role;
import edu.neu.javachip.aedfinalproject.model.userAccount.UserAccountDirectory;
import edu.neu.javachip.aedfinalproject.model.workqueue.WorkQueue;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

public abstract class Organization {

    private int id;
    private Type type;
    private String name;
    private EmployeeDirectory employeeDirectory = new EmployeeDirectory();
    private UserAccountDirectory userAccountDirectory = new UserAccountDirectory();
    private WorkQueue workQueue = new WorkQueue();
    @Getter @Setter
    private Enterprise parentEnterprise;
//    private static int counter = 0;

    /*public enum Type{
        Admin("Admin Organization"), Doctor("Doctor Organization"), Lab("Lab Organization");
        private String value;
        private Type(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }*/

    public enum Type{
        ADMIN("Admin Organization"),
        PRIMARY_CARE_PROVIDER("Primary Care Provider Organization"),
        LAB_ORGANIZATION("Lab Organization"),
        MEDICAL_STORE_ORGANIZATION("Medical Store Organization"),
        CLERICAL_ORGANIZATION("Clerical Organization"),
        APPROVING_ORGANIZATION("Approving Organization"),
        //DOCTORS_ASSISTANT("Doctor's Assistant Organization"),
        DOCTORS_ORGANIZATION("Doctor Organization"),
        PHARMACY_ORGANIZATION("Pharmacy Organization");

        private String value;
        private Type(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public Organization(Organization.Type type) {
        //this();
        this.type = type;
    }

    /*public Organization() {
        this.id = counter++;
    }*/

    public abstract List<Role> getSupportedRole();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeeDirectory getEmployeeDirectory() {
        return employeeDirectory;
    }

    public UserAccountDirectory getUserAccountDirectory() {
        return userAccountDirectory;
    }

    public WorkQueue getWorkQueue() {
        return workQueue;
    }

    public Organization.Type getType() {
        return type;
    }

    public void setType(Organization.Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

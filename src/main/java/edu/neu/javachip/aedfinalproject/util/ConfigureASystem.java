package edu.neu.javachip.aedfinalproject.util;

import edu.neu.javachip.aedfinalproject.model.ecosystem.Ecosystem;
import edu.neu.javachip.aedfinalproject.model.employee.Employee;
import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.model.role.SystemAdminRole;
import edu.neu.javachip.aedfinalproject.model.userAccount.UserAccount;

public class ConfigureASystem {
    public static Ecosystem configure() {

        Ecosystem system = Ecosystem.getInstance();

        //Create a network
        //create an enterprise
        //initialize some organizations
        //have some employees
        //create user account


        Employee employee = new Employee("sysadmin");
        system.getEmployeeDirectory().addEmployee(employee);

        UserAccount ua = system.getUserAccountDirectory().createUserAccount("sysadmin", Utils.getHash("sysadmin"), employee, new SystemAdminRole());

        system.addNetwork(new Network("Massachusetts"));
        system.addNetwork(new Network("New York"));



        return system;
    }
}

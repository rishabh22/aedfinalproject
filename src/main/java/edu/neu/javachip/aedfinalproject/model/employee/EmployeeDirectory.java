package edu.neu.javachip.aedfinalproject.model.employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDirectory {

    private List<Employee> employeeList = new ArrayList<>();

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void addEmployee(Employee employee) {
        //employee.setEmployeeId(employeeList.stream().mapToInt(Employee::getEmployeeId).max().orElse(0) + 1);
        employeeList.add(employee);

    }

    public void deleteEmployee(Employee employee){
        employeeList.remove(employee);
    }
}

package edu.neu.javachip.aedfinalproject.model.enterprise;

import edu.neu.javachip.aedfinalproject.model.employee.Employee;
import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.model.organization.OrganizationDirectory;

import java.util.EnumSet;

public abstract class Enterprise extends Organization {
    private Type enterpriseType;
    private OrganizationDirectory organizationDirectory = new OrganizationDirectory();
    private Network parentNetwork;

    public enum Type {
        PRIMARY_CARE_CENTER("Primary Care Center"),
        //GOVERNMENT("Government"),
        REMOTE_HEALTHCARE_PROVIDER("Remote Healthcare Provider");

        private String value;

        private Type(String value){
            this.value=value;
        }
        public String getValue() {
            return value;
        }
        @Override
        public String toString(){
            return value;
        }
    }

    public Enterprise(Enterprise.Type type){
        super(null);
        this.enterpriseType=type;
    }

    public Enterprise.Type getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(Type enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    public OrganizationDirectory getOrganizationDirectory() {
        return organizationDirectory;
    }



    public abstract EnumSet<Organization.Type> getOrganizationTypes();

    public void deleteEmployeeFromAllSuborganizations(Employee employee){
        organizationDirectory.getOrganizationList().forEach(organization -> {
            organization.getEmployeeDirectory().getEmployeeList().remove(employee);
        });
    }

    public void deleteUserAccountByEmployeeFromAllSuborganizations(Employee employee){
        organizationDirectory.getOrganizationList().forEach(organization -> {
            organization.getUserAccountDirectory().getUserAccountList().removeIf(userAccount -> userAccount.getEmployee().equals(employee));
        });
    }

    public Network getParentNetwork() {
        return parentNetwork;
    }

    public void setParentNetwork(Network parentNetwork) {
        this.parentNetwork = parentNetwork;
    }
}

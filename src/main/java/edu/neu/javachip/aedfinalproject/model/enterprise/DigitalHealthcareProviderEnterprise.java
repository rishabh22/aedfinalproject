package edu.neu.javachip.aedfinalproject.model.enterprise;

import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.model.role.Role;

import java.util.EnumSet;
import java.util.List;

public class DigitalHealthcareProviderEnterprise extends Enterprise{

    private static final EnumSet<Organization.Type> organizationTypes = EnumSet.of(Organization.Type.DOCTORS_ORGANIZATION, Organization.Type.PHARMACY_ORGANIZATION);
    public DigitalHealthcareProviderEnterprise() {
        super(Enterprise.Type.REMOTE_HEALTHCARE_PROVIDER);
    }

    @Override
    public List<Role> getSupportedRole() {
        return null;
    }


    public EnumSet<Organization.Type> getOrganizationTypes() {
        return organizationTypes;
    }


}

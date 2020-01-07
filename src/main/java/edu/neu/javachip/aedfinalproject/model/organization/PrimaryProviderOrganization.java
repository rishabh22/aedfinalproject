package edu.neu.javachip.aedfinalproject.model.organization;

import edu.neu.javachip.aedfinalproject.model.role.PrimaryCareProviderRole;
import edu.neu.javachip.aedfinalproject.model.role.Role;

import java.util.List;

public class PrimaryProviderOrganization extends Organization{

    public PrimaryProviderOrganization() {
        super(Type.PRIMARY_CARE_PROVIDER);
    }

    @Override
    public List<Role> getSupportedRole() {
        return List.of(new PrimaryCareProviderRole());
    }
}

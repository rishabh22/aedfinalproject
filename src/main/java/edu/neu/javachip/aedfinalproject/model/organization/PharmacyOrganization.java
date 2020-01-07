package edu.neu.javachip.aedfinalproject.model.organization;

import edu.neu.javachip.aedfinalproject.model.role.PharmacistRole;
import edu.neu.javachip.aedfinalproject.model.role.Role;

import java.util.Collections;
import java.util.List;

public class PharmacyOrganization extends Organization {

    public PharmacyOrganization() {
        super(Type.PHARMACY_ORGANIZATION);
    }

    @Override
    public List<Role> getSupportedRole() {
        return Collections.singletonList(new PharmacistRole());
    }
}

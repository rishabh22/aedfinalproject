package edu.neu.javachip.aedfinalproject.model.organization;

import edu.neu.javachip.aedfinalproject.model.role.AdminRole;
import edu.neu.javachip.aedfinalproject.model.role.Role;

import java.util.Collections;
import java.util.List;

public class AdminOrganization extends Organization {
    public AdminOrganization(String name) {
        super(Type.ADMIN);
        this.setName(name);
    }

    @Override
    public List<Role> getSupportedRole() {
        return Collections.singletonList(new AdminRole());
    }
}

package edu.neu.javachip.aedfinalproject.model.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrganizationDirectory {
    private List<Organization> organizationList = new ArrayList<>();

    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    public void addOrganization(Organization organization) {
        organization.setId(organizationList.stream().mapToInt(Organization::getId).max().orElse(0) + 1);
        organizationList.add(organization);
    }

    public void deleteOrganization(Organization organization) {
        organizationList.remove(organization);
    }

    public List<Organization> getOrganizationsByType(Organization.Type type) {
        return organizationList.stream()
                .filter(organization -> organization.getType().getValue().equals(type.getValue()))
                .collect(Collectors.toList());
    }

    public List<Organization> getAllOrganizationsExceptAdmin() {
        return organizationList.stream()
                .filter(organization -> !organization.getType().equals(Organization.Type.ADMIN))
                .collect(Collectors.toList());
    }


}

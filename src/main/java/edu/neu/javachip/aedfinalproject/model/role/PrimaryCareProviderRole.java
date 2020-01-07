package edu.neu.javachip.aedfinalproject.model.role;

import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;

public class PrimaryCareProviderRole extends Role {
    public PrimaryCareProviderRole() {
        super(RoleType.PRIMARY_CARE_PROVIDER);
    }

    @Override
    public String getWorkArea() {
        return ViewFXMLs.PRIMARY_CARE_PROVIDER_VIEWS.DASHBOARD.getValue();
    }
}

package edu.neu.javachip.aedfinalproject.model.role;

import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;

public class PharmacistRole extends Role{
    public PharmacistRole() {
        super(RoleType.PHARMACIST);
    }

    @Override
    public String getWorkArea() {
        return ViewFXMLs.PHARMACIST_VIEWS.DASHBOARD.getValue();
    }
}

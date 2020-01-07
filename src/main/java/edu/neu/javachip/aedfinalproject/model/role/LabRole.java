package edu.neu.javachip.aedfinalproject.model.role;

import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;

public class LabRole extends Role {
    public LabRole() {
        super(RoleType.LAB_TECHNICIAN);
    }

    @Override
    public String getWorkArea() {
        return ViewFXMLs.LAB_TECHS_VIEWS.DASHBOARD.getValue();
    }

}

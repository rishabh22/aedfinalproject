package edu.neu.javachip.aedfinalproject.model.role;

import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;

public class DoctorRole extends Role {
    public DoctorRole() {
        super(RoleType.DOCTOR);
    }

    @Override
    public String getWorkArea() {
        return ViewFXMLs.DOCTOR_VIEWS.DASHBOARD.getValue();
    }
}

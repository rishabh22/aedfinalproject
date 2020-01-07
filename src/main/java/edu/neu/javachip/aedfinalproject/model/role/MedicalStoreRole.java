package edu.neu.javachip.aedfinalproject.model.role;

import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;

public class MedicalStoreRole extends Role {
    public MedicalStoreRole() {
        super(RoleType.MEDICAL_STORE_PROVIDER);
    }

    @Override
    public String getWorkArea() {
        return ViewFXMLs.MEDICAL_STORE_PROVIDER_VIEWS.DASHBOARD.getValue();
    }
}

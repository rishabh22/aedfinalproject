package edu.neu.javachip.aedfinalproject.model.organization;

import edu.neu.javachip.aedfinalproject.model.medicalstore.MedicalStoreInventory;
import edu.neu.javachip.aedfinalproject.model.role.MedicalStoreRole;
import edu.neu.javachip.aedfinalproject.model.role.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class MedicalStoreOrganization extends Organization {
    private MedicalStoreInventory medicalStoreInventory = new MedicalStoreInventory();

    public MedicalStoreOrganization(Type type) {
        super(type);

    }

    public MedicalStoreOrganization() {
        super(Type.MEDICAL_STORE_ORGANIZATION);
    }

    @Override
    public List<Role> getSupportedRole() {
        return Collections.singletonList(new MedicalStoreRole());
    }
}

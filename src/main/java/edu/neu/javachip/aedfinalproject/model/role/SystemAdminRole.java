package edu.neu.javachip.aedfinalproject.model.role;

import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;

public class SystemAdminRole extends Role {
    public SystemAdminRole() {
        super(RoleType.SYSTEM_ADMIN);
    }

    @Override
    public String getWorkArea() {
        return ViewFXMLs.SYSADMIN_VIEWS.DASHBOARD.getValue();
    }
}

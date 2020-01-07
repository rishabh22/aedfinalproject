package edu.neu.javachip.aedfinalproject.model.role;

import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;

public class AdminRole extends Role {
    public AdminRole() {
        super(RoleType.ADMIN);
    }
/*@Override
    public JPanel createWorkArea(JPanel userProcessContainer, UserAccount account, Organization organization, Enterprise enterprise, EcoSystem business) {
        return new AdminWorkAreaJPanel(userProcessContainer, enterprise);
    }*/

    @Override
    public String getWorkArea() {
        return ViewFXMLs.ENTERPRISE_ADMIN_VIEWS.DASHBOARD.getValue();
    }
}
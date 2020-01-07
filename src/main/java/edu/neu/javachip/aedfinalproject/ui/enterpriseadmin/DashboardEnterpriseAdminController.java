package edu.neu.javachip.aedfinalproject.ui.enterpriseadmin;

import edu.neu.javachip.aedfinalproject.ui.AbstractParentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardEnterpriseAdminController extends AbstractParentController {

    @FXML
    protected void initialize() {
        super.initialize();

        ManageOrganizations manageOrganizations = new ManageOrganizations(this);
        mainPane.setCenter(manageOrganizations);

        setTitle("Manage Enterprise");
    }

    @FXML
    private void openManageOrganization(ActionEvent actionEvent) {
        ManageOrganizations manageOrganizations = new ManageOrganizations(this);
        mainPane.setCenter(manageOrganizations);
    }

    @FXML
    private void openManageEmployees(ActionEvent actionEvent) {
        ManageEmployees manageEmployees = new ManageEmployees(this);
        mainPane.setCenter(manageEmployees);
    }

    @FXML
    private void openManageUserAccounts(ActionEvent actionEvent) {
        ManageUserAccounts manageUserAccounts = new ManageUserAccounts(this);
        mainPane.setCenter(manageUserAccounts);
    }


    /*@FXML
    private void manageNetworks() {
        ManageNetworks manageNetworks = new ManageNetworks(this);
        mainPane.setCenter(manageNetworks);
    }

    void manageEnterprises(Network network) {
        ManageEnterprises manageEnterprises = new ManageEnterprises(this, network);
        mainPane.setCenter(manageEnterprises);
        setTitle("Manage Enterprise for " + network.getName());
    }*/

}

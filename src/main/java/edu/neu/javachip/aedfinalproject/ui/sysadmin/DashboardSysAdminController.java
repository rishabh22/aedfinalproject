package edu.neu.javachip.aedfinalproject.ui.sysadmin;

import edu.neu.javachip.aedfinalproject.model.enterprise.Enterprise;
import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.ui.AbstractParentController;
import javafx.fxml.FXML;
import javafx.scene.Node;

import java.util.AbstractMap;

public class DashboardSysAdminController extends AbstractParentController {
    private Sidebar sidebar;

    @FXML
    protected void initialize() {
        super.initialize();

        sidebar = new Sidebar(this);
        mainPane.setLeft(sidebar);

        manageNetworks();

    }


    @FXML
    private void manageNetworks() {
        ManageNetworks manageNetworks = new ManageNetworks(this);
        replaceContent(new AbstractMap.SimpleEntry<>("Manage Networks",manageNetworks));
    }

    void manageEnterprises(Network network) {
        ManageEnterprises manageEnterprises = new ManageEnterprises(this, network);
        replaceContent(new AbstractMap.SimpleEntry<>("Manage Enterprise for " + network.getName(),manageEnterprises));
    }


    void manageAdmins(Enterprise enterprise) {
        ManageAdmins manageAdmins = new ManageAdmins(this, enterprise);
        replaceContent(new AbstractMap.SimpleEntry<>("Manage Admins for " + enterprise.getName(),manageAdmins));
    }

    private void replaceContent(AbstractMap.SimpleEntry<String, Node> node) {
        this.addNodeToStack(node);
        if (getNodeStackCount() > 1) {
            sidebar.enableBackButton(true);
        } else {
            sidebar.enableBackButton(false);
        }
        setTitle(node.getKey());
        mainPane.setCenter(node.getValue());
    }


    void repopulateSidebarTree() {
        sidebar.populateTree();
    }

    void handleBackPressed() {
        removeCurrentNodeFromStack();
        Node node = getCurrentNodeFromStack().getValue();
        setTitle(getCurrentNodeFromStack().getKey());
        if (node != null) {
            if (getNodeStackCount() > 1) {
                sidebar.enableBackButton(true);
            } else {
                sidebar.enableBackButton(false);
            }
            mainPane.setCenter(node);
        }
    }
}

package edu.neu.javachip.aedfinalproject.ui.sysadmin;

import edu.neu.javachip.aedfinalproject.model.enterprise.Enterprise;
import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.io.IOException;
import java.util.List;

public class Sidebar extends VBox {
    private Context app;
    private DashboardSysAdminController parentController;

    @FXML
    private Button btnBack;

    @FXML
    private TreeView<String> treeViewEcoSystem;

    public Sidebar(DashboardSysAdminController dashboardSysAdminController) {
        this.parentController = dashboardSysAdminController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.SYSADMIN_VIEWS.SIDEBAR.getValue()));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void initialize() {
        app = Context.getInstance();
        populateTree();

        btnBack.setGraphic(new FontAwesome().create(FontAwesome.Glyph.LONG_ARROW_LEFT));
    }

    void populateTree() {
        List<Network> networkList = app.getSystem().getNetworkList();

        TreeItem<String> rootItem = new TreeItem<>("Networks");
        treeViewEcoSystem.setRoot(rootItem);
        ObservableList<TreeItem<String>> children = rootItem.getChildren();

        for (int i = 0; i < networkList.size(); i++) {
            Network network = networkList.get(i);

            TreeItem<String> networkItem = new TreeItem<>(network.getName());
            children.add(networkItem);

            for (Enterprise enterprise : network.getEnterpriseDirectory().getEnterpriseList()) {
                TreeItem<String> enterpriseItem = new TreeItem<>(enterprise.getName());
                networkItem.getChildren().add(enterpriseItem);
                for (Organization organization : enterprise.getOrganizationDirectory().getOrganizationList()) {
                    TreeItem<String> organizationItem = new TreeItem<>(organization.getName());
                    enterpriseItem.getChildren().add(organizationItem);
                }
            }


        }

        treeViewEcoSystem.refresh();


        /*DefaultMutableTreeNode networks=new DefaultMutableTreeNode("Networks");
        DefaultMutableTreeNode root=(DefaultMutableTreeNode)model.getRoot();
        root.removeAllChildren();
        root.insert(networks, 0);

        DefaultMutableTreeNode networkNode;
        DefaultMutableTreeNode enterpriseNode;
        DefaultMutableTreeNode organizationNode;

        for(int i=0;i<networkList.size();i++){
            network=networkList.get(i);
            networkNode=new DefaultMutableTreeNode(network.getName());
            networks.insert(networkNode, i);

            enterpriseList=network.getEnterpriseDirectory().getEnterpriseList();
            for(int j=0; j<enterpriseList.size();j++){
                enterprise=enterpriseList.get(j);
                enterpriseNode=new DefaultMutableTreeNode(enterprise.getName());
                networkNode.insert(enterpriseNode, j);

                organizationList=enterprise.getOrganizationDirectory().getOrganizationList();
                for(int k=0;k<organizationList.size();k++){
                    organization=organizationList.get(i);
                    organizationNode=new DefaultMutableTreeNode(organization.getName());
                    enterpriseNode.insert(organizationNode, k);
                }
            }
        }
        model.reload();*/
    }

    void enableBackButton(boolean enable){
        btnBack.setDisable(!enable);
    }

    @FXML
    public void handleBackPressed(){
        parentController.handleBackPressed();
    }
}

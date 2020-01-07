package edu.neu.javachip.aedfinalproject.ui.sysadmin;

import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.ui.StringConstants;
import edu.neu.javachip.aedfinalproject.ui.Validation;
import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import edu.neu.javachip.aedfinalproject.util.Utils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.table.TableFilter;

import java.io.IOException;
import java.util.Optional;

public class ManageNetworks extends AnchorPane {
    private Context app;
    private DashboardSysAdminController parentController;

    @FXML
    private TableView<Network> tblNetwork;

    @FXML
    private TableColumn<Network, String> colNetworkName;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtOrigName;

    @FXML
    private Button btnManageEnterprises;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDeleteNetwork;

    @FXML
    private TextField txtNewName;

    private Network toUpdate;

    private ObservableList<Network> networkObservableList;


    ManageNetworks(DashboardSysAdminController dashboardSysAdminController) {
        this.parentController = dashboardSysAdminController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.SYSADMIN_VIEWS.MANAGE_NETWORKS.getValue()));

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
        networkObservableList = FXCollections.observableArrayList(app.getSystem().getNetworkList());
        tblNetwork.setItems(networkObservableList);
        mapTableColumns();
        TableFilter.forTableView(tblNetwork).apply();

        tblNetwork.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Network> observableValue, Network oldSelection, Network newSelection) {
                //toUpdate=null;
                boolean disable = true;
                if (newSelection != null) {
                    disable = false;
                }
                txtOrigName.setText("");
                btnManageEnterprises.setDisable(disable);
                btnUpdate.setDisable(disable);
                btnSave.setDisable(!disable);
                btnDeleteNetwork.setDisable(disable);
            }
        });

    }


    public void mapTableColumns() {
//        colNetworkName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNetworkName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));

    }

    @FXML
    private void addNetwork() {
        app.removeErrorBorderFromField(txtName);

        if (!txtName.getText().matches(Validation.PATTERN_ALPHASPACES)) {
            displayErrorAlert("Please enter a valid name");
            app.addErrorBorderToField(txtName);
            return;
        }

        if (app.getSystem().getNetworkList().stream().anyMatch(n -> n.getName().equalsIgnoreCase(txtName.getText()))) {
            displayErrorAlert("Network already exists");
            app.addErrorBorderToField(txtName);
            return;
        }

        Network network = new Network(txtName.getText());
        app.getSystem().addNetwork(network);
        networkObservableList.add(network);


        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Network added successfully").show();
        txtName.setText("");

        Utils.getGlobalLogger().info("Network added");

        postChange();
    }

    @FXML
    private void updateNetwork() {

        Utils.getGlobalLogger().info("Update network button clicked");

        if (tblNetwork.getSelectionModel().getSelectedIndex() < 0) {
            displayErrorAlert("Please select a row from the table");
            return;
        }

        toUpdate = tblNetwork.getSelectionModel().getSelectedItem();
        txtOrigName.setText(toUpdate.getName());
        txtNewName.setDisable(false);
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);

    }

    @FXML
    private void save() {
        app.removeErrorBorderFromField(txtNewName);

        if (!txtNewName.getText().matches(Validation.PATTERN_ALPHASPACES)) {
            displayErrorAlert("Please enter a valid name");
            app.addErrorBorderToField(txtNewName);
            return;
        }

        if (app.getSystem().getNetworkList().stream().anyMatch(n -> n.getName().equalsIgnoreCase(txtNewName.getText()))) {
            displayErrorAlert("Network already exists with the same name");
            app.addErrorBorderToField(txtNewName);
            return;
        }


        int origindex = networkObservableList.indexOf(toUpdate);
        toUpdate.setName(txtNewName.getText());
        networkObservableList.set(origindex, toUpdate);


        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Network updated successfully").show();


        Utils.getGlobalLogger().info("Network Updated");

        btnUpdate.setDisable(false);
        btnSave.setDisable(true);
        txtNewName.setText("");
        txtOrigName.setText("");
        txtNewName.setDisable(true);
        txtOrigName.setDisable(true);
        postChange();
    }




    @FXML
    private void manageEnterprises() {
        parentController.manageEnterprises(tblNetwork.getSelectionModel().getSelectedItem());

        Utils.getGlobalLogger().info("Manage enterprise scene loaded");

    }

    @FXML
    private void deleteNetwork() {
        if (tblNetwork.getSelectionModel().getSelectedIndex() < 0) {
            displayErrorAlert("Please select a row from the table");
            return;
        }

        //Alert alert = app.buildAlert(Alert.AlertType.CONFIRMATION, )

        Alert alert = app.buildAlert(Alert.AlertType.CONFIRMATION, StringConstants.CONFIRMATION,"Are you sure you want to delete this network?");

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            app.getSystem().deleteNetwork(tblNetwork.getSelectionModel().getSelectedItem());
            networkObservableList.remove(tblNetwork.getSelectionModel().getSelectedItem());

            app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS,"Network deleted successfully").show();

            Utils.getGlobalLogger().info("Network Deleted");
            postChange();
        }
    }

    public void postChange() {
        app.storeSystem();
        parentController.repopulateSidebarTree();
    }

    public void displayErrorAlert(String message) {
        app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR,message).show();
    }

}
package edu.neu.javachip.aedfinalproject.ui.sysadmin;

import edu.neu.javachip.aedfinalproject.model.enterprise.DigitalHealthcareProviderEnterprise;
import edu.neu.javachip.aedfinalproject.model.enterprise.Enterprise;
import edu.neu.javachip.aedfinalproject.model.enterprise.PrimaryCareCentreEnterprise;
import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.model.organization.AdminOrganization;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.ui.StringConstants;
import edu.neu.javachip.aedfinalproject.ui.Validation;
import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import edu.neu.javachip.aedfinalproject.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.util.Optional;

public class ManageEnterprises extends AnchorPane {
    private Context app;
    private DashboardSysAdminController parentController;
    private Network network;

    @FXML
    private TableView<Enterprise> tblEnterprise;

    @FXML
    private TableColumn<Enterprise, Integer> colId;

    @FXML
    private TableColumn<Enterprise, String> colName;

    @FXML
    private TableColumn<Enterprise, String> colEnterpriseType;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ComboBox<Enterprise.Type> comboEnterpriseType;

    @FXML
    private TextField txtName;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnManageAdmin;

    private ValidationSupport validationSupport;

    private Enterprise toUpdate;

    private ObservableList<Enterprise> enterpriseObservableList;

    ManageEnterprises(DashboardSysAdminController dashboardSysAdminController, Network network) {
        this.parentController = dashboardSysAdminController;
        this.network = network;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.SYSADMIN_VIEWS.MANAGE_ENTERPRISES.getValue()));

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
        enterpriseObservableList = FXCollections.observableArrayList(network.getEnterpriseDirectory().getEnterpriseList());
        tblEnterprise.setItems(enterpriseObservableList);
        mapTableColumns();
        TableFilter.forTableView(tblEnterprise).apply();

        tblEnterprise.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Enterprise>() {
            @Override
            public void changed(ObservableValue<? extends Enterprise> observableValue, Enterprise oldSelection, Enterprise newSelection) {
                //toUpdate=null;
                boolean disable = true;
                if(newSelection!=null){
                    disable = false;
                }

                btnUpdate.setDisable(disable);
                btnDelete.setDisable(disable);
                btnManageAdmin.setDisable(disable);
            }
        });
    }

    public void mapTableColumns() {
        colId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        colName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        colEnterpriseType.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEnterpriseType().getValue()));
    }

    @FXML
    public void add() {
        AnchorPane addPane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.SYSADMIN_VIEWS.ADD_ENTERPRISE.getValue()));

        fxmlLoader.setRoot(addPane);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        splitPane.getItems().set(1, addPane);

        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(
                txtName,
                true,
                Validator.createRegexValidator("Please enter a valid name", Validation.PATTERN_ALPHASPACES, Severity.ERROR)
        );

        validationSupport.registerValidator(comboEnterpriseType, Validator.createEmptyValidator( "Please select an enterprise type"));

        comboEnterpriseType.setItems(FXCollections.observableArrayList(Enterprise.Type.values()));

        Utils.getGlobalLogger().info("Add enterprise button clicked");

    }

    @FXML
    public void createEnterprise() {
        Enterprise enterprise = null;


        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        if(network.getEnterpriseDirectory().getEnterpriseList().stream().anyMatch(n -> n.getName().equalsIgnoreCase(txtName.getText()))){
            displayErrorAlert("Enterprise already exists");
            return;
        }


        switch (comboEnterpriseType.getSelectionModel().getSelectedItem()) {
            case PRIMARY_CARE_CENTER:
                enterprise = new PrimaryCareCentreEnterprise();
                break;
            /*case GOVERNMENT:
                enterprise = new Government();
                break;*/
            case REMOTE_HEALTHCARE_PROVIDER:
                enterprise = new DigitalHealthcareProviderEnterprise();
                break;
        }

        enterprise.setName(txtName.getText());
        enterprise.setParentNetwork(network);
        AdminOrganization adminOrganization = new AdminOrganization(Organization.Type.ADMIN.getValue());
        adminOrganization.setParentEnterprise(enterprise);
        enterprise.getOrganizationDirectory().addOrganization(adminOrganization);

        network.getEnterpriseDirectory().addEnterprise(enterprise);
        enterpriseObservableList.add(enterprise);

        app.buildAlert(Alert.AlertType.INFORMATION,StringConstants.SUCCESS,"Enterprise added successfully").show();

        txtName.setText("");
//        comboEnterpriseType.getSelectionModel().clearSelection();

        postChange();
        Utils.getGlobalLogger().info("New enterprise created");

    }

    @FXML
    public void update() {

        toUpdate= tblEnterprise.getSelectionModel().getSelectedItem();
        AnchorPane updatePane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.SYSADMIN_VIEWS.UPDATE_ENTERPRISE.getValue()));
        fxmlLoader.setRoot(updatePane);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        splitPane.getItems().set(1, updatePane);

        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(
                txtName,
                true,
                Validator.createRegexValidator("Please enter a valid name", Validation.PATTERN_ALPHASPACES, Severity.ERROR)
        );


        Utils.getGlobalLogger().info("Update enterprise button clicked");


        txtName.setText(toUpdate.getName());

    }

    @FXML
    public void updateEnterprise(){


        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        if(txtName.getText().trim().isEmpty()){
            displayErrorAlert("Please type a new name for enterprise!");

            return;
        }

        if(network.getEnterpriseDirectory().getEnterpriseList().stream().anyMatch(n -> n.getName().equalsIgnoreCase(txtName.getText()))){
            displayErrorAlert("Enterprise already exists");
            return;
        }
        int origindex = enterpriseObservableList.indexOf(toUpdate);
        toUpdate.setName(txtName.getText());
        enterpriseObservableList.set(origindex, toUpdate);

        txtName.setText("");

        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Enterprise updated successfully").show();

        Utils.getGlobalLogger().info("Enterprise updated");

        postChange();

    }




    @FXML
    public void deleteEnterprise() {

       if (tblEnterprise.getSelectionModel().getSelectedIndex() < 0) {
           displayErrorAlert("Please select a row from the table");
           return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Check!");
        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        alert.setContentText("Are you sure you want to delete this enterprise?");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {

            network.getEnterpriseDirectory().deleteEnterprise(tblEnterprise.getSelectionModel().getSelectedItem());
            enterpriseObservableList.remove(tblEnterprise.getSelectionModel().getSelectedItem());

            app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Enterprise deleted successfully").show();

            postChange();

        }

        Utils.getGlobalLogger().info("Enterprise deleted successfully");

    }


    @FXML
    private void manageAdmins(){
        parentController.manageAdmins(tblEnterprise.getSelectionModel().getSelectedItem());

        Utils.getGlobalLogger().info("Manage admins button clicked");

    }

    public void displayErrorAlert(String message) {
        app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR, message).show();
    }

    public void postChange() {
        app.storeSystem();
        parentController.repopulateSidebarTree();
    }


}
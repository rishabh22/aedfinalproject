package edu.neu.javachip.aedfinalproject.ui.enterpriseadmin;

import edu.neu.javachip.aedfinalproject.model.organization.*;
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
import javafx.scene.layout.Region;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.util.Optional;

public class ManageOrganizations extends AnchorPane {
    private Context app;
    private DashboardEnterpriseAdminController parentController;

    @FXML
    private TableView<Organization> tblOrganization;

    @FXML
    private TableColumn<Organization, String> colOrganizationName;

    @FXML
    private TableColumn<Organization, Integer> colId;

    @FXML
    private TableColumn<Organization, String> colOrganizationType;
    @FXML
    private SplitPane splitPane;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtUpdateName;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private ComboBox<Organization.Type> comboOrganizationType;

    private ValidationSupport validationSupport;

    private Organization toUpdate;

    private ObservableList<Organization> organizationsObservableList;

    ManageOrganizations(DashboardEnterpriseAdminController dashboardEnterpriseAdminController) {
        this.parentController = dashboardEnterpriseAdminController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.ENTERPRISE_ADMIN_VIEWS.MANAGE_ORGANIZATIONS.getValue()));

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
        organizationsObservableList = FXCollections.observableArrayList(app.getLoggedInUserAccount().getEmployee().getEnterprise().getOrganizationDirectory().getOrganizationList());
        tblOrganization.setItems(organizationsObservableList);
        mapTableColumns();
        TableFilter.forTableView(tblOrganization).apply();
        tblOrganization.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Organization> observableValue, Organization oldSelection, Organization newSelection) {
                //toUpdate=null;
                boolean disable = true;
                if (newSelection != null) {
                    disable = false;
                }
                btnUpdate.setDisable(disable);
                btnDelete.setDisable(disable);
            }
        });


    }


    public void mapTableColumns() {

        colOrganizationName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        colId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        colOrganizationType.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getType().getValue()));

    }


    @FXML
    public void add() {

        Utils.getGlobalLogger().info("Add organization button clicked");

        AnchorPane addPane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.ENTERPRISE_ADMIN_VIEWS.ADD_ORGANIZATION.getValue()));

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

        validationSupport.registerValidator(comboOrganizationType, Validator.createEmptyValidator("Please select an organization type"));


        comboOrganizationType.setItems(FXCollections.observableArrayList(app.getLoggedInUserAccount().getEmployee().getEnterprise().getOrganizationTypes()));
        /*comboOrganizationType.setCellFactory(lv -> new ListCell<>() {

            @Override
            protected void updateItem(Organization.Type item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getValue());
            }
        });*/
    }

    @FXML
    public void addOrganization() {
        Organization organization = null;

        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.WARNING, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        if (app.getLoggedInUserAccount().getEmployee().getEnterprise().getOrganizationDirectory().getOrganizationList().stream().anyMatch(n -> n.getName().equalsIgnoreCase(txtName.getText()))) {
            displayErrorAlert("Organization already exists with the same name");
            return;
        }


        if (comboOrganizationType.getSelectionModel().getSelectedIndex() < 0) {
            return;
        }

        switch (comboOrganizationType.getSelectionModel().getSelectedItem()) {
            case PRIMARY_CARE_PROVIDER:
                organization = new PrimaryProviderOrganization();
                break;
            case MEDICAL_STORE_ORGANIZATION:
                if (!app.getLoggedInUserAccount().getEmployee().getEnterprise().getOrganizationDirectory().getOrganizationsByType(Organization.Type.MEDICAL_STORE_ORGANIZATION).isEmpty()) {
                    displayErrorAlert("Only one organization can be created of this type per enterprise");
                    return;
                }
                organization = new MedicalStoreOrganization();
                break;
            case LAB_ORGANIZATION:
                organization = new LabOrganization();
                break;
            /*case CLERICAL_ORGANIZATION:
                organization = new ClericalOrganization();
                break;
            case APPROVING_ORGANIZATION:
                organization = new ApprovingOrganization();
                break;*/
            /*case DOCTORS_ASSISTANT:
                organization = new DoctorAssistantOrganization();
                break;*/
            case DOCTORS_ORGANIZATION:
                organization = new DoctorOrganization();
                break;
            case PHARMACY_ORGANIZATION:
                organization = new PharmacyOrganization();
                break;


        }

        organization.setName(txtName.getText());
        organization.setParentEnterprise(app.getLoggedInUserAccount().getEmployee().getEnterprise());
        organization.setType(comboOrganizationType.getSelectionModel().getSelectedItem());
        app.getLoggedInUserAccount().getEmployee().getEnterprise().getOrganizationDirectory().addOrganization(organization);
        organizationsObservableList.add(organization);

        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Organization created successfully").show();

        app.storeSystem();

        Utils.getGlobalLogger().info("New organization created");

        txtName.setText("");
//        comboOrganizationType.getSelectionModel().clearSelection();
    }

    @FXML
    public void update() {

        Utils.getGlobalLogger().info("Update organization button clicked");

        if (tblOrganization.getSelectionModel().getSelectedItem().getType().equals(Organization.Type.ADMIN)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setTitle(StringConstants.ERROR);
            alert.setContentText("Sorry, you do not have the authority to modify Admin Organizations");
            alert.show();
            return;
        }

        toUpdate = tblOrganization.getSelectionModel().getSelectedItem();
        AnchorPane updatePane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.ENTERPRISE_ADMIN_VIEWS.UPDATE_ORGANIZATION.getValue()));
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
                txtUpdateName,
                true,
                Validator.createRegexValidator("Please enter a valid name", Validation.PATTERN_ALPHASPACES, Severity.ERROR)
        );


        txtUpdateName.setText(toUpdate.getName());

    }

    @FXML
    public void updateOrganization() {

        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        if (app.getLoggedInUserAccount().getEmployee().getEnterprise().getOrganizationDirectory().getOrganizationList().stream().anyMatch(n -> n.getName().equalsIgnoreCase(txtUpdateName.getText()))) {
            displayErrorAlert("Organization already exists with the same name");
            return;
        }

        int origindex = organizationsObservableList.indexOf(toUpdate);
        toUpdate.setName(txtUpdateName.getText());
        organizationsObservableList.set(origindex, toUpdate);


        txtUpdateName.setText("");

        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Organization updated successfully").show();

        app.storeSystem();

        Utils.getGlobalLogger().info("Organization updated");
    }

    @FXML
    public void deleteOrganization() {

        if (tblOrganization.getSelectionModel().getSelectedIndex() < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setTitle(StringConstants.ERROR);
            alert.setContentText("Please select a row from the table");
            alert.show();
            return;
        }

        if (tblOrganization.getSelectionModel().getSelectedItem().getType().equals(Organization.Type.ADMIN)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setTitle(StringConstants.ERROR);
            alert.setContentText("Sorry, you do not have the authority to modify Admin Organizations");
            alert.show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Check!");
        alert.setContentText("Are you sure you want to delete this organization?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.YES)) {
            Organization organizationToDelete = tblOrganization.getSelectionModel().getSelectedItem();
            organizationToDelete.getEmployeeDirectory().getEmployeeList().forEach(employee -> {
                        app.getLoggedInUserAccount().getEmployee().getEnterprise().deleteEmployeeFromAllSuborganizations(employee);
                        app.getLoggedInUserAccount().getEmployee().getEnterprise().getEmployeeDirectory().deleteEmployee(employee);
                        app.getLoggedInUserAccount().getEmployee().getEnterprise().deleteUserAccountByEmployeeFromAllSuborganizations(employee);
                        app.getLoggedInUserAccount().getEmployee().getEnterprise().getUserAccountDirectory().deleteUserAccountsByEmployee(employee);
                    }
            );
            app.getLoggedInUserAccount().getEmployee().getEnterprise().getOrganizationDirectory().deleteOrganization(organizationToDelete);
            organizationsObservableList.remove(tblOrganization.getSelectionModel().getSelectedItem());

            app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Orgnization deleted successfully").show();

            app.storeSystem();

            Utils.getGlobalLogger().info("Organization deleted");

        }
    }

    public void displayErrorAlert(String message) {
        app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR, message).show();
    }
}
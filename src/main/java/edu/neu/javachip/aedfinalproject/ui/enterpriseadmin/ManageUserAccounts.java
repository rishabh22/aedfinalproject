package edu.neu.javachip.aedfinalproject.ui.enterpriseadmin;

import edu.neu.javachip.aedfinalproject.model.employee.Employee;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.model.role.Role;
import edu.neu.javachip.aedfinalproject.model.userAccount.UserAccount;
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
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.util.Optional;

public class ManageUserAccounts extends AnchorPane {
    private Context app;
    private DashboardEnterpriseAdminController parentController;

    @FXML
    private TableView<UserAccount> tblUserAccounts;

    @FXML
    private TableColumn<UserAccount, String> colUsername;

    @FXML
    private TableColumn<UserAccount, String> colEmployeeName;

    @FXML
    private TableColumn<UserAccount, String> colOrganization;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField pwdPassword;

    @FXML
    private PasswordField pwdConfirmPassword;

    @FXML
    private ComboBox<Organization> comboOrganization;

    @FXML
    private ComboBox<Employee> comboEmployees;

    @FXML
    private ComboBox<Role> comboRoles;

    @FXML
    private TextField txtOldUsername;

    @FXML
    private PasswordField pwdOldPassword;

    @FXML
    private ComboBox<Organization> comboOldOrganization;

    @FXML
    private ComboBox<Employee> comboOldEmployees;

    @FXML
    private ComboBox<Role> comboOldRoles;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    private UserAccount toUpdate;

    private ObservableList<UserAccount> userAccountsObservableList;

    private ValidationSupport validationSupport;


    ManageUserAccounts(DashboardEnterpriseAdminController dashboardEnterpriseAdminController) {
        this.parentController = dashboardEnterpriseAdminController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.ENTERPRISE_ADMIN_VIEWS.MANAGE_USER_ACCOUNTS.getValue()));

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

        userAccountsObservableList = FXCollections.observableArrayList(app.getLoggedInUserAccount().getEmployee().getEnterprise().getUserAccountDirectory().getUserAccountList());
        tblUserAccounts.setItems(userAccountsObservableList);
        mapTableColumns();
        TableFilter.forTableView(tblUserAccounts).apply();
        tblUserAccounts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends UserAccount> observableValue, UserAccount oldSelection, UserAccount newSelection) {
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
        colUsername.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUsername()));
        colEmployeeName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEmployee().getName()));
        colOrganization.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEmployee().getOrganization().getName()));
    }


    @FXML
    public void add() {
        AnchorPane addPane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.ENTERPRISE_ADMIN_VIEWS.ADD_USER_ACCOUNT.getValue()));

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
                txtUsername,
                true,
                Validator.createRegexValidator("Please enter a valid username", Validation.PATTERN_USERNAME, Severity.ERROR)
        );

        validationSupport.registerValidator(
                pwdPassword,
                true,
                Validator.createRegexValidator("Please enter a valid password", Validation.PATTERN_PASSWORD, Severity.ERROR)
        );

        validationSupport.registerValidator(comboOrganization, Validator.createEmptyValidator( "Please select an organization"));

        validationSupport.registerValidator(comboEmployees, Validator.createEmptyValidator( "Please select an employee"));

        validationSupport.registerValidator(comboRoles, Validator.createEmptyValidator( "Please select a role"));

        comboOrganization.setItems(FXCollections.observableArrayList(app.getLoggedInUserAccount().getEmployee().getEnterprise().getOrganizationDirectory().getAllOrganizationsExceptAdmin()));
        comboOrganization.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Organization> observableValue, Organization oldSelection, Organization newSelection) {
                if (newSelection != null) {
                    comboEmployees.setItems(FXCollections.observableArrayList(newSelection.getEmployeeDirectory().getEmployeeList()));
                    comboRoles.setItems(FXCollections.observableArrayList(newSelection.getSupportedRole()));
                }
            }
        });



    }

    @FXML
    public void addUserAccount() {
        UserAccount userAccount = new UserAccount();




        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        if(app.getLoggedInUserAccount().getEmployee().getEnterprise().getUserAccountDirectory().getUserAccountList().stream().anyMatch(n -> n.getUsername().equalsIgnoreCase(txtUsername.getText()))){
            displayErrorAlert("Username already exists");
            return;
        }


        if(app.getLoggedInUserAccount().getEmployee().getEnterprise().getUserAccountDirectory().getUserAccountList().stream().anyMatch(n -> n.getEmployee().equals(comboEmployees.getValue()) && n.getRole().getType().getValue().equals(comboRoles.getValue().getType().getValue()))){
            displayErrorAlert("User already exists for this employee in the same role");
            return;
        }

        userAccount.setUsername(txtUsername.getText());
        userAccount.setPassword(Utils.getHash(pwdPassword.getText()));

        if (txtUsername.getText().trim().isEmpty() || pwdPassword.getText().isEmpty()) {
            Alert fail = new Alert(Alert.AlertType.INFORMATION);
            fail.setHeaderText("Failure");
            fail.setContentText("Please fill all the above fields!");
            fail.showAndWait();
            return;
        }

        userAccount.setEmployee(comboEmployees.getSelectionModel().getSelectedItem());

        userAccount.setRole(comboRoles.getSelectionModel().getSelectedItem());

        app.getLoggedInUserAccount().getEmployee().getEnterprise().getUserAccountDirectory().addUserAccount(userAccount);
        app.getSystem().getUserAccountDirectory().addUserAccount(userAccount);

        userAccountsObservableList.add(userAccount);

        txtUsername.setText("");
        pwdPassword.setText("");
        comboOrganization.getSelectionModel().clearSelection();
        comboEmployees.getSelectionModel().clearSelection();
        comboRoles.getSelectionModel().clearSelection();

        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "User Account created successfully").show();

        app.storeSystem();

        validationSupport.redecorate();
        Utils.getGlobalLogger().info("User Account created");

    }

    @FXML
    public void update() {
        if(tblUserAccounts.getSelectionModel().getSelectedItem().getEmployee().getOrganization().getType().getValue().equals(Organization.Type.ADMIN.getValue())) {
            displayErrorAlert("Sorry, you do not have the authority to modify Admin Organizations");
            return;
        }

        toUpdate = tblUserAccounts.getSelectionModel().getSelectedItem();
        AnchorPane updatePane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.ENTERPRISE_ADMIN_VIEWS.UPDATE_USER_ACCOUNT.getValue()));
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
                pwdPassword,
                true,
                Validator.createRegexValidator("Please enter a valid password", Validation.PATTERN_PASSWORD, Severity.ERROR)
        );

        validationSupport.registerValidator(
                pwdConfirmPassword,
                true,
                Validator.createRegexValidator("Please enter a valid password", Validation.PATTERN_PASSWORD, Severity.ERROR)
        );

        /*validationSupport.registerValidator(
                txtUsername,
                true,
                Validator.createRegexValidator("Please enter a valid username", Validation.PATTERN_USERNAME, Severity.ERROR)
        );

        validationSupport.registerValidator(comboOrganization, Validator.createEmptyValidator( "Please select an organization"));

        validationSupport.registerValidator(comboEmployees, Validator.createEmptyValidator( "Please select an employee"));

        validationSupport.registerValidator(comboRoles, Validator.createEmptyValidator( "Please select a role"));*/

        /*txtOldUsername.setDisable(true);
        pwdOldPassword.setDisable(true);
        comboOldEmployees.setDisable(true);
        comboOldOrganization.setDisable(true);
        comboOldRoles.setDisable(true);

        txtOldUsername.setText(toUpdate.getUsername());
        pwdOldPassword.setText(toUpdate.getPassword());

        comboOldOrganization.setItems(FXCollections.observableArrayList(app.getLoggedInUserAccount().getEmployee().getEnterprise().getOrganizationDirectory().getAllOrganizationsExceptAdmin()));
        comboOldOrganization.getSelectionModel().select(toUpdate.getEmployee().getOrganization());
        comboOldEmployees.setItems(FXCollections.observableArrayList(app.getLoggedInUserAccount().getEmployee().getOrganization().getEmployeeDirectory().getEmployeeList()));
        comboOldEmployees.getSelectionModel().select(toUpdate.getEmployee());
        comboOldRoles.getSelectionModel().select(toUpdate.getRole());*/


        txtUsername.setText(toUpdate.getUsername());
        /*pwdPassword.setText(toUpdate.getPassword());

        comboOrganization.setItems(FXCollections.observableArrayList(app.getLoggedInUserAccount().getEmployee().getEnterprise().getOrganizationDirectory().getAllOrganizationsExceptAdmin()));
        comboOrganization.getSelectionModel().select(toUpdate.getEmployee().getOrganization());
        comboEmployees.setItems(FXCollections.observableArrayList(app.getLoggedInUserAccount().getEmployee().getOrganization().getEmployeeDirectory().getEmployeeList()));
        comboEmployees.getSelectionModel().select(toUpdate.getEmployee());
        comboRoles.getSelectionModel().select(toUpdate.getRole());

        comboOrganization.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Organization> observableValue, Organization oldSelection, Organization newSelection) {
                if (newSelection != null) {
                    comboEmployees.setItems(FXCollections.observableArrayList(newSelection.getEmployeeDirectory().getEmployeeList()));
                    comboRoles.setItems(FXCollections.observableArrayList(newSelection.getSupportedRole()));
                }
            }
        });*/

    }

    @FXML
    public void updateUserAccount() {
       /* if (txtUsername.getText().trim().isEmpty() || pwdPassword.getText().trim().isEmpty()) {
            Alert fail = new Alert(Alert.AlertType.INFORMATION);
            fail.setHeaderText("Failure");
            fail.setContentText("Please select a row from the table!");
            fail.showAndWait();
            return;
        }

        */

        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.WARNING, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }


        if(!pwdConfirmPassword.equals(pwdPassword))
        {
            app.buildAlert(Alert.AlertType.WARNING,StringConstants.ERROR, "Passwords do not match");
        }

        int origindex = userAccountsObservableList.indexOf(toUpdate);
//        toUpdate.setUsername(txtUsername.getText());
        toUpdate.setPassword(Utils.getHash(pwdPassword.getText()));
        /*toUpdate.setEmployee(comboEmployees.getSelectionModel().getSelectedItem());
        toUpdate.setRole(comboRoles.getSelectionModel().getSelectedItem());*/
        userAccountsObservableList.set(origindex, toUpdate);

        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "User Account updated successfully").show();

        app.storeSystem();

        Utils.getGlobalLogger().info("User Account updated");


        /*txtOldUsername.setText(txtUsername.getText());
        pwdOldPassword.setText(pwdPassword.getText());*/

        //unable to get old names in combo boxes and updating them in the old box itself
      //  comboOldEmployees.getSelectionModel().getSelectedItem(comboEmployees.);


        /*txtUsername.setText("");
        pwdPassword.setText("");
        comboOrganization.getSelectionModel().clearSelection();
        comboEmployees.getSelectionModel().clearSelection();
        comboRoles.getSelectionModel().clearSelection();
        validationSupport.redecorate();*/


        splitPane.getItems().set(1,new AnchorPane());

    }

    @FXML
    public void deleteUserAccount() {

        if(tblUserAccounts.getSelectionModel().getSelectedItem().getEmployee().getOrganization().getType().getValue().equals(Organization.Type.ADMIN.getValue())) {
            displayErrorAlert("Sorry, you do not have the authority to modify Admin Organizations");
            return;
        }

        Alert alert = app.buildAlert(Alert.AlertType.CONFIRMATION, StringConstants.CONFIRMATION, "Are you sure you want to delete this user account?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.YES)) {

            app.getLoggedInUserAccount().getEmployee().getOrganization().getUserAccountDirectory().deleteUserAccount(tblUserAccounts.getSelectionModel().getSelectedItem());
            app.getLoggedInUserAccount().getEmployee().getEnterprise().getUserAccountDirectory().deleteUserAccount(tblUserAccounts.getSelectionModel().getSelectedItem());
            userAccountsObservableList.remove(tblUserAccounts.getSelectionModel().getSelectedItem());
            app.getSystem().getUserAccountDirectory().deleteUserAccount(tblUserAccounts.getSelectionModel().getSelectedItem());

            app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "User Account deleted successfully").show();

            app.storeSystem();

            Utils.getGlobalLogger().info("User Account deleted");

        }

    }

    public void displayErrorAlert(String message){
        Alert fail = new Alert(Alert.AlertType.WARNING);
        fail.setHeaderText(StringConstants.ERROR);
        fail.setContentText(message);
        fail.showAndWait();
    }


}
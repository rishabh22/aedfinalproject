package edu.neu.javachip.aedfinalproject.ui.sysadmin;

import edu.neu.javachip.aedfinalproject.model.employee.Employee;
import edu.neu.javachip.aedfinalproject.model.enterprise.Enterprise;
import edu.neu.javachip.aedfinalproject.model.organization.AdminOrganization;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.model.role.AdminRole;
import edu.neu.javachip.aedfinalproject.model.userAccount.UserAccount;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ManageAdmins extends AnchorPane {
    private Context app;
    private DashboardSysAdminController parentController;
    private Enterprise enterprise;

    @FXML
    private TableView<UserAccount> tblUserAccount;

    @FXML
    private TableColumn<UserAccount, String> colEmployeeName;

    @FXML
    private TableColumn<UserAccount, String> colUsername;

    @FXML
    private SplitPane splitPane;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField txtEmployeeName;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField pwdPassword;

    private ValidationSupport validationSupport;

    private UserAccount toUpdate;

    private ObservableList<UserAccount> userAccountObservableList;

    ManageAdmins(DashboardSysAdminController dashboardSysAdminController, Enterprise enterprise) {
        this.parentController = dashboardSysAdminController;
        this.enterprise = enterprise;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.SYSADMIN_VIEWS.MANAGE_ADMINS.getValue()));

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
        userAccountObservableList = FXCollections.observableArrayList(enterprise.getOrganizationDirectory().getOrganizationList().stream().filter(o -> o instanceof AdminOrganization).map(o -> o.getUserAccountDirectory().getUserAccountList()).flatMap(List::stream).collect(Collectors.toList()));
        tblUserAccount.setItems(userAccountObservableList);
        mapTableColumns();
        TableFilter.forTableView(tblUserAccount).apply();

        tblUserAccount.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
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
        colEmployeeName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getEmployee().getName()));
        colUsername.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUsername()));
    }

    @FXML
    public void add() {
        AnchorPane addPane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.SYSADMIN_VIEWS.ADD_USER_ACCOUNT.getValue()));

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
                txtEmployeeName,
                true,
                Validator.createRegexValidator("Please enter a valid name in the form 'FirstName LastName'", Validation.PATTERN_NAME, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtUsername,
                true,
                Validator.createRegexValidator("Please enter a valid name", Validation.PATTERN_USERNAME, Severity.ERROR)
        );

        validationSupport.registerValidator(
                pwdPassword,
                true,
                Validator.createRegexValidator("Please enter a valid password", Validation.PATTERN_PASSWORD, Severity.ERROR)
        );

        Utils.getGlobalLogger().info("Add admin button clicked");


    }

    @FXML
    public void update() {

        toUpdate = tblUserAccount.getSelectionModel().getSelectedItem();
        AnchorPane updatePane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.SYSADMIN_VIEWS.UPDATE_USER_ACCOUNT.getValue()));
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
                txtEmployeeName,
                true,
                Validator.createRegexValidator("Please enter a valid name in the form 'FirstName LastName'", Validation.PATTERN_NAME, Severity.ERROR)
        );

        /*validationSupport.registerValidator(
                txtUsername,
                true,
                Validator.createRegexValidator("Please enter a valid username", Validation.PATTERN_USERNAME, Severity.ERROR)
        );*/

        validationSupport.registerValidator(
                pwdPassword,
                true,
                Validator.createRegexValidator("Please enter a valid password", Validation.PATTERN_PASSWORD, Severity.ERROR)
        );

        Utils.getGlobalLogger().info("Update admin button clicked");


        txtUsername.setText(toUpdate.getUsername());
        pwdPassword.setText(toUpdate.getPassword());
        txtEmployeeName.setText(toUpdate.getEmployee().getName());

    }

    @FXML
    public void updateUserAccount() {

        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.WARNING, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        /*if (enterprise.getUserAccountDirectory().getUserAccountList().stream().anyMatch(n -> n.getUsername().equalsIgnoreCase(txtUsername.getText()))) {
            displayErrorAlert("Username already exists");
            return;
        }*/


        if (txtEmployeeName.getText().trim().isEmpty() || txtUsername.getText().trim().isEmpty() || pwdPassword.getText().trim().isEmpty()) {
            displayErrorAlert("Please fill all the above fields");
            return;
        }

        int origindex = userAccountObservableList.indexOf(toUpdate);
        toUpdate.setUsername(txtUsername.getText());
        toUpdate.setPassword(Utils.getHash(pwdPassword.getText()));
        toUpdate.getEmployee().setName(txtEmployeeName.getText());
        userAccountObservableList.set(origindex, toUpdate);


        txtUsername.setText("");
        pwdPassword.setText("");
        txtEmployeeName.setText("");

        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Admin updated successfully").show();

        Utils.getGlobalLogger().info("Admin updated");
        app.storeSystem();
    }


    @FXML
    public void createUserAccount() {
        Employee employee = new Employee();

        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        if (app.getSystem().getUserAccountDirectory().getUserAccountList().stream().anyMatch(n -> n.getUsername().equalsIgnoreCase(txtUsername.getText()))) {
            displayErrorAlert("Username already exists");
            return;
        }

        employee.setName(txtEmployeeName.getText());

        if (txtEmployeeName.getText().trim().isEmpty() || txtUsername.getText().trim().isEmpty() || pwdPassword.getText().trim().isEmpty()) {
            displayErrorAlert("Please fill all the above fields");
            return;
        }


        for (Organization o : enterprise.getOrganizationDirectory().getOrganizationList()) {
            if (o instanceof AdminOrganization) {
                employee.setOrganization(o);
                employee.setEnterprise(enterprise);
                o.getEmployeeDirectory().addEmployee(employee);
                enterprise.getEmployeeDirectory().addEmployee(employee);
                app.getSystem().getEmployeeDirectory().addEmployee(employee);
                UserAccount userAccount = new UserAccount();
                userAccount.setUsername(txtUsername.getText());
                userAccount.setPassword(Utils.getHash(pwdPassword.getText()));
                userAccount.setEmployee(employee);
                userAccount.setRole(new AdminRole());
                //Add account to current admin organization
                o.getUserAccountDirectory().addUserAccount(userAccount);
                //Add account details to enterprise
                enterprise.getUserAccountDirectory().addUserAccount(userAccount);
//                userAccount = o.getUserAccountDirectory().createUserAccount(txtUsername.getText(), pwdPassword.getText(), employee, new AdminRole());
                //Add account details to ecosystem
                app.getSystem().getUserAccountDirectory().addUserAccount(userAccount);
                userAccountObservableList.add(userAccount);
                app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "New admin created successfully").show();
                txtEmployeeName.setText("");
                txtUsername.setText("");
                pwdPassword.setText("");
                break;
            }


            Utils.getGlobalLogger().info("User account created");
            app.storeSystem();
        }
    }


    @FXML
    public void deleteUserAccount() {

        UserAccount userAccount = tblUserAccount.getSelectionModel().getSelectedItem();
        Alert alert = app.buildAlert(Alert.AlertType.CONFIRMATION, StringConstants.CONFIRMATION, "Are you sure you want to delete this admin?");

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {

            userAccount.getEmployee().getOrganization().getUserAccountDirectory().deleteUserAccount(userAccount);
            userAccount.getEmployee().getOrganization().getEmployeeDirectory().deleteEmployee(userAccount.getEmployee());
            userAccount.getEmployee().getEnterprise().getUserAccountDirectory().deleteUserAccount(userAccount);
            userAccount.getEmployee().getEnterprise().getEmployeeDirectory().deleteEmployee(userAccount.getEmployee());
            app.getSystem().getUserAccountDirectory().deleteUserAccount(userAccount);
            userAccountObservableList.remove(userAccount);

            app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Successfully deleted user account").show();
            app.storeSystem();
            parentController.repopulateSidebarTree();
        }

    }

    public void displayErrorAlert(String message) {
        Alert fail = new Alert(Alert.AlertType.WARNING);
        fail.setHeaderText(StringConstants.ERROR);
        fail.setContentText(message);
        fail.showAndWait();
    }


}
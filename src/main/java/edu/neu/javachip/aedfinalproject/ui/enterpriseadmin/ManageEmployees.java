package edu.neu.javachip.aedfinalproject.ui.enterpriseadmin;

import edu.neu.javachip.aedfinalproject.model.employee.Employee;
import edu.neu.javachip.aedfinalproject.model.enterprise.Enterprise;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ManageEmployees extends AnchorPane {
    private Context app;
    private DashboardEnterpriseAdminController parentController;

    @FXML
    private TableView<Employee> tblEmployees;

    @FXML
    private TableColumn<Employee, String> colName;

    @FXML
    private TableColumn<Employee, String> colEmail;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtMobile;

    @FXML
    private ComboBox<Organization> comboOrganizations;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private ComboBox<Enterprise> comboEnterpriseType;

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane imgWrapper;

    @FXML
    private TableColumn<Employee, String> colOrganization;

    private byte[] imageFile;

    private Employee toUpdate;

    private ObservableList<Employee> employeesObservableList;

    private ValidationSupport validationSupport;


    ManageEmployees(DashboardEnterpriseAdminController dashboardEnterpriseAdminController) {
        this.parentController = dashboardEnterpriseAdminController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.ENTERPRISE_ADMIN_VIEWS.MANAGE_EMPLOYEES.getValue()));

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
        employeesObservableList = FXCollections.observableArrayList(app.getLoggedInUserAccount().getEmployee().getEnterprise().getEmployeeDirectory().getEmployeeList());
        tblEmployees.setItems(employeesObservableList);
        mapTableColumns();
        TableFilter.forTableView(tblEmployees).apply();
        tblEmployees.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Employee> observableValue, Employee oldSelection, Employee newSelection) {
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
        //if (!employeesObservableList.isEmpty()) {
//            colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        colEmail.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEmail()));
        colOrganization.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getOrganization().getName()));


    }


    @FXML
    public void add() {
        imageFile = null;
        AnchorPane addPane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.ENTERPRISE_ADMIN_VIEWS.ADD_EMPLOYEE.getValue()));

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
                Validator.createRegexValidator("Please enter a valid name in the form 'FirstName LastName", Validation.PATTERN_NAME, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtEmail,
                true,
                Validator.createRegexValidator("Please enter a valid email", Validation.PATTERN_EMAIL, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtMobile,
                true,
                Validator.createRegexValidator("Please enter a valid mobile number", Validation.PATTERN_MOBILENUMBER, Severity.ERROR)
        );

        validationSupport.registerValidator(comboOrganizations, Validator.createEmptyValidator("Please select an organization"));


        comboOrganizations.setItems(FXCollections.observableArrayList(app.getLoggedInUserAccount().getEmployee().getEnterprise().getOrganizationDirectory().getAllOrganizationsExceptAdmin()));

        setDefaultUserImage();
        imgWrapper.setEffect(new DropShadow(20, Color.CHOCOLATE));
        /*comboOrganizations.setCellFactory(lv -> new ListCell<>() {

            @Override
            protected void updateItem(Organization item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });*/
    }

    @FXML
    public void addEmployee() {
        Employee employee = new Employee();


        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.WARNING, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        employee.setName(txtName.getText());
        employee.setEmail(txtEmail.getText());
        employee.setPhoneNumber(Long.parseLong(txtMobile.getText()));

        employee.setOrganization(comboOrganizations.getSelectionModel().getSelectedItem());
        employee.setEnterprise(app.getLoggedInUserAccount().getEmployee().getEnterprise());
        if (imageFile != null && imageFile.length > 0) {
            employee.setProfilePic(imageFile);
        }
        app.getLoggedInUserAccount().getEmployee().getEnterprise().getEmployeeDirectory().addEmployee(employee);
        comboOrganizations.getSelectionModel().getSelectedItem().getEmployeeDirectory().addEmployee(employee);
        app.getSystem().getEmployeeDirectory().addEmployee(employee);

        txtName.setText("");
        txtEmail.setText("");
        txtMobile.setText("");
        setDefaultUserImage();
//        comboOrganizations.getSelectionModel().clearSelection();
        imageFile = null;

        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Employee added successfully").show();

        app.storeSystem();

        employeesObservableList.add(employee);

        validationSupport.redecorate();
        Utils.getGlobalLogger().info("Employee added");


    }

    @FXML
    public void update() {
        imageFile = null;
        if (tblEmployees.getSelectionModel().getSelectedItem().getOrganization().getType().getValue().equals(Organization.Type.ADMIN.getValue())) {
            displayErrorAlert("Sorry, you do not have the authority to modify Admin Organizations");
            return;
        }

        toUpdate = tblEmployees.getSelectionModel().getSelectedItem();
        AnchorPane updatePane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.ENTERPRISE_ADMIN_VIEWS.UPDATE_EMPLOYEE.getValue()));
        fxmlLoader.setRoot(updatePane);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        splitPane.getItems().set(1, updatePane);

        setDefaultUserImage();
            imgWrapper.setEffect(new DropShadow(20, Color.CHOCOLATE));

        validationSupport = new ValidationSupport();

        validationSupport.registerValidator(
                txtName,
                true,
                Validator.createRegexValidator("Please enter a valid name in the form 'FirstName LastName", Validation.PATTERN_NAME, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtEmail,
                true,
                Validator.createRegexValidator("Please enter a valid email", Validation.PATTERN_EMAIL, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtMobile,
                true,
                Validator.createRegexValidator("Please enter a valid mobile number", Validation.PATTERN_MOBILENUMBER, Severity.ERROR)
        );

//        validationSupport.registerValidator(comboOrganizations, Validator.createEmptyValidator("Please select an organization"));

        txtName.setText(toUpdate.getName());
        txtEmail.setText(toUpdate.getEmail());
        try {
            txtMobile.setText(Long.toString(toUpdate.getPhoneNumber()));
        } catch (Exception ignored) {

        }

        if (toUpdate.getProfilePic() != null) {
            imageView.setImage(new Image(new ByteArrayInputStream(toUpdate.getProfilePic())));
        }
        //comboOrganizations.setItems(FXCollections.observableArrayList(app.getUserEnterprise().getOrganizationDirectory().getAllOrganizationsExceptAdmin()));

        //comboOrganizations.getSelectionModel().select(toUpdate.getOrganization());
    }

    @FXML
    public void updateEmployee() {


        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.WARNING, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        int origindex = employeesObservableList.indexOf(toUpdate);

        toUpdate.setName(txtName.getText());

        toUpdate.setEmail(txtEmail.getText());

        toUpdate.setPhoneNumber(Long.parseLong(txtMobile.getText()));

        if (imageFile != null && imageFile.length > 0) {
            toUpdate.setProfilePic(imageFile);
        }

        //If organization has been changed
        /*if (!comboOrganizations.getSelectionModel().getSelectedItem().equals(toUpdate.getOrganization())) {
            //Remove employee from old organization
            toUpdate.getOrganization().getEmployeeDirectory().deleteEmployee(toUpdate);
            //Set employee's new organization
            toUpdate.setOrganization(comboOrganizations.getSelectionModel().getSelectedItem());
            //Add employee to the new organizations directory
            comboOrganizations.getSelectionModel().getSelectedItem().getEmployeeDirectory().addEmployee(toUpdate);
        }*/

        employeesObservableList.set(origindex, toUpdate);

        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Employee updated successfully").show();

        app.storeSystem();

        Utils.getGlobalLogger().info("Employee updated");

        /*txtName.setText("");
        txtEmail.setText("");
        txtMobile.setText("");
        comboOrganizations.getSelectionModel().clearSelection();
        validationSupport.redecorate();*/
        splitPane.getItems().set(1, new AnchorPane());

    }

    @FXML
    public void deleteEmployee() {

        if (tblEmployees.getSelectionModel().getSelectedItem().getOrganization().getType().getValue().equals(Organization.Type.ADMIN.getValue())) {
            displayErrorAlert("Sorry, you do not have the authority to modify Admin Organizations");
            return;
        }


        Alert alert = app.buildAlert(Alert.AlertType.CONFIRMATION, StringConstants.CONFIRMATION, "Are you sure you want to delete this employee?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.YES)) {

            Employee selected = tblEmployees.getSelectionModel().getSelectedItem();
            app.getLoggedInUserAccount().getEmployee().getEnterprise().getEmployeeDirectory().deleteEmployee(selected);
            app.getLoggedInUserAccount().getEmployee().getEnterprise().deleteEmployeeFromAllSuborganizations(selected);
            app.getLoggedInUserAccount().getEmployee().getEnterprise().deleteUserAccountByEmployeeFromAllSuborganizations(selected);
            app.getLoggedInUserAccount().getEmployee().getEnterprise().getUserAccountDirectory().deleteUserAccountsByEmployee(selected);
            employeesObservableList.remove(tblEmployees.getSelectionModel().getSelectedItem());
            app.getSystem().getEmployeeDirectory().deleteEmployee(selected);
            app.getSystem().getUserAccountDirectory().deleteUserAccountsByEmployee(selected);

            app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Employee deleted successfully").show();

            app.storeSystem();

            Utils.getGlobalLogger().info("Employee deleted");

        }

    }

    public void uploadImage(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();

        chooser.setTitle("Select a  File");

        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg", "*.gif"));

        File selectedFile = chooser.showOpenDialog(imageView.getScene().getWindow());

        if (selectedFile != null) {
            try {
                imageFile = FileUtils.readFileToByteArray(selectedFile);
                imageView.setImage(new Image(FileUtils.openInputStream(selectedFile)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private void setDefaultUserImage() {
        imageView.setImage(new Image(this.getClass().getResourceAsStream("/images/default-profile-pic.png")));
    }

    public void displayErrorAlert(String message) {
        app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR, message).show();
    }

}
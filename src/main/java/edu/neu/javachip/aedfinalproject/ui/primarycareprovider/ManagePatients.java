package edu.neu.javachip.aedfinalproject.ui.primarycareprovider;

import edu.neu.javachip.aedfinalproject.Gender;
import edu.neu.javachip.aedfinalproject.model.patient.Patient;
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
import javafx.util.StringConverter;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ManagePatients extends AnchorPane {
    private Context app;
    private DashboardPrimaryCareProviderController parentController;

    @FXML
    private TableView<Patient> tblPatients;

    @FXML
    private TableColumn<Patient, String> colPatientName;

    @FXML
    private TableColumn<Patient, String> colDob;

    @FXML
    private TableColumn<Patient, String> colGender;


    @FXML
    private SplitPane splitPane;

    @FXML
    private DatePicker dpDob;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnStartVisit;

    @FXML
    private TextField txtName;

    @FXML
    private ComboBox<Gender> comboGender;

    @FXML
    private TextField txtHeight;

    @FXML
    private TextField txtWeight;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextArea txtAddress;

    private Patient toUpdate;

    private ObservableList<Patient> patientsObservableList;

    private ValidationSupport validationSupport;


    ManagePatients(DashboardPrimaryCareProviderController dashboardPrimaryCareProviderController) {
        this.parentController = dashboardPrimaryCareProviderController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.PRIMARY_CARE_PROVIDER_VIEWS.MANAGE_PATIENTS.getValue()));

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
        patientsObservableList = FXCollections.observableArrayList(app.getLoggedInUserAccount().getEmployee().getEnterprise().getParentNetwork().getPatientDirectory().getPatientList());
        tblPatients.setItems(patientsObservableList);
        mapTableColumns();
        TableFilter.forTableView(tblPatients).apply();
        tblPatients.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Patient> observableValue, Patient oldSelection, Patient newSelection) {
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
        colPatientName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        colDob.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Utils.formatDate(cellData.getValue().getDateOfBirth())));
        colGender.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGender().getValue()));

    }

    @FXML
    public void add() {
        AnchorPane addPane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.PRIMARY_CARE_PROVIDER_VIEWS.ADD_PATIENT.getValue()));

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
                txtHeight,
                true,
                Validator.createRegexValidator("Please enter a number for height", Validation.PATTERN_NUMBER, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtWeight,
                true,
                Validator.createRegexValidator("Please enter a number for weight", Validation.PATTERN_NUMBER, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtPhoneNumber,
                true,
                Validator.createRegexValidator("Please enter a valid phone number", Validation.PATTERN_MOBILENUMBER, Severity.ERROR)
        );

       validationSupport.registerValidator(
                txtEmail,
                true,
                Validator.createRegexValidator("Please enter a valid email ID", Validation.PATTERN_EMAIL, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtAddress,
                true,
                Validator.createRegexValidator("Please enter a home address", Validation.PATTERN_ADDRESS, Severity.ERROR)
        );


        validationSupport.registerValidator(comboGender, Validator.createEmptyValidator( "Please select a gender"));

        validationSupport.registerValidator(dpDob, Validator.createEmptyValidator( "Please enter the date of birth"));


        comboGender.setItems(FXCollections.observableArrayList(Gender.values()));

        dpDob.setConverter(new StringConverter<>() {
            String pattern = "MM-dd-yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                dpDob.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        dpDob.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) > 0 );
            }
        });
    }

    @FXML
    public void updatePatientButton() {

        if (tblPatients.getSelectionModel().getSelectedIndex()<0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !! ");
            alert.setContentText("Please select a row from the table");
            alert.show();
            return;
        }


        toUpdate = tblPatients.getSelectionModel().getSelectedItem();
        AnchorPane updatePane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.PRIMARY_CARE_PROVIDER_VIEWS.UPDATE_PATIENT.getValue()));
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
                Validator.createRegexValidator("Please enter a valid name in the form 'FirstName LastName", Validation.PATTERN_NAME, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtHeight,
                true,
                Validator.createRegexValidator("Please enter a number for height", Validation.PATTERN_NUMBER, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtWeight,
                true,
                Validator.createRegexValidator("Please enter a number for weight", Validation.PATTERN_NUMBER, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtEmail,
                true,
                Validator.createRegexValidator("Please enter a valid email ID", Validation.PATTERN_EMAIL, Severity.ERROR)
        );

        validationSupport.registerValidator(
                txtAddress,
                true,
                Validator.createRegexValidator("Please enter a home address", Validation.PATTERN_ADDRESS, Severity.ERROR)
        );

        validationSupport.registerValidator(comboGender, Validator.createEmptyValidator( "Please select a gender"));

        validationSupport.registerValidator(dpDob, Validator.createEmptyValidator( "Please enter the date of birth"));

        txtName.setText(toUpdate.getName());
        dpDob.setValue(toUpdate.getDateOfBirth());
        txtEmail.setText(toUpdate.getEmail());
        txtAddress.setText(toUpdate.getAddress());
        comboGender.setValue(toUpdate.getGender());
        txtName.setText(toUpdate.getName());
        try {
            txtPhoneNumber.setText(Long.toString(toUpdate.getPhoneNumber()));
        } catch (Exception ignored) {

        }

        try {
            txtHeight.setText(Integer.toString(toUpdate.getHeight()));
        } catch (Exception ignored) {

        }

        try {
            txtWeight.setText(Integer.toString(toUpdate.getWeight()));
        } catch (Exception ignored) {

        }

        comboGender.setItems(FXCollections.observableArrayList(Gender.values()));
        dpDob.setConverter(new StringConverter<>() {
            String pattern = "MM-dd-yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                dpDob.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    @FXML
    public void updatePatient(){

        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        int origindex = patientsObservableList.indexOf(toUpdate);

        toUpdate.setName(txtName.getText());
        toUpdate.setHeight(Integer.parseInt(txtHeight.getText()));
        toUpdate.setWeight(Integer.parseInt(txtWeight.getText()));
        toUpdate.setGender(comboGender.getValue());
        toUpdate.setDateOfBirth(dpDob.getValue());
        toUpdate.setPhoneNumber(Long.parseLong(txtPhoneNumber.getText()));
        toUpdate.setEmail(txtEmail.getText());
        toUpdate.setAddress(txtAddress.getText());

        patientsObservableList.set(origindex, toUpdate);

        app.buildAlert(Alert.AlertType.CONFIRMATION, StringConstants.SUCCESS, "Patient details updated successfully").show();
        Utils.getGlobalLogger().info("Patient details updated");

        app.storeSystem();


        txtName.setText("");
        dpDob.setValue(null);
        comboGender.getSelectionModel().clearSelection();;
        txtHeight.setText("");
        txtWeight.setText("");
        txtPhoneNumber.setText("");
        txtAddress.setText("");
        txtEmail.setText("");

    }

    @FXML
    public void addPatient(){
        Patient patient = new Patient();

        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        patient.setName(txtName.getText());
        patient.setDateOfBirth(dpDob.getValue());
        patient.setGender(comboGender.getValue());
        patient.setHeight(Integer.parseInt(txtHeight.getText()));
        patient.setWeight(Integer.parseInt(txtWeight.getText()));
        patient.setPhoneNumber(Long.parseLong(txtPhoneNumber.getText()));
        patient.setEmail(txtEmail.getText());
        patient.setAddress(txtAddress.getText());

        app.getLoggedInUserAccount().getEmployee().getEnterprise().getParentNetwork().getPatientDirectory().addPatient(patient);
        patientsObservableList.add(patient);

        txtName.setText("");
        txtPhoneNumber.setText("");
        txtWeight.setText("");
        txtHeight.setText("");
        comboGender.getSelectionModel().clearSelection();
        dpDob.setValue(null);
        txtEmail.setText("");
        txtAddress.setText("");

        app.buildAlert(Alert.AlertType.CONFIRMATION, StringConstants.SUCCESS, "Patient added successfully").show();
        Utils.getGlobalLogger().info("Patient added");

        app.storeSystem();

        validationSupport.redecorate();

    }

    public void displayErrorAlert(String message){
        Alert fail = new Alert(Alert.AlertType.WARNING);
        fail.setHeaderText(StringConstants.ERROR);
        fail.setContentText(message);
        fail.showAndWait();
    }

    @FXML
    public void deletePatient() {

        if (tblPatients.getSelectionModel().getSelectedIndex() < 0) {
           displayErrorAlert("Please select a row from the table");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Check!");
        alert.setContentText("Are you sure you want to delete this patient ?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.YES)) {

            app.getLoggedInUserAccount().getEmployee().getEnterprise().getParentNetwork().getPatientDirectory().deletePatient(tblPatients.getSelectionModel().getSelectedItem());

            patientsObservableList.remove(tblPatients.getSelectionModel().getSelectedItem());

            app.buildAlert(Alert.AlertType.CONFIRMATION, StringConstants.SUCCESS, "Patient deleted successfully").show();
            Utils.getGlobalLogger().info("Patient deleted");

            app.storeSystem();

        }

    }

    @FXML
    private void startVisit(){
        if (tblPatients.getSelectionModel().getSelectedIndex() < 0) {
            displayErrorAlert("Please select a row from the table");
            return;
        }

        parentController.openRecordPatientProblem(tblPatients.getSelectionModel().getSelectedItem());
    }
}
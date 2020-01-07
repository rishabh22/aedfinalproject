package edu.neu.javachip.aedfinalproject.ui.primarycareprovider;

import edu.neu.javachip.aedfinalproject.model.enterprise.Enterprise;
import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.model.patient.Patient;
import edu.neu.javachip.aedfinalproject.model.workqueue.DoctorOpinionRequest;
import edu.neu.javachip.aedfinalproject.ui.StringConstants;
import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import edu.neu.javachip.aedfinalproject.util.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.File;
import java.io.IOException;

public class RecordPatientProblem extends AnchorPane {
    private Context app;
    private DashboardPrimaryCareProviderController parentController;
    private Patient patient;

    @FXML
    private TextField txtPatientName;

    @FXML
    private TextField txtPatientDob;

    @FXML
    private TextField txtPatientGender;

    @FXML
    private TextField txtPatientPhone;

    @FXML
    private TextField txtPatientHeight;

    @FXML
    private TextField txtPatientWeight;

    @FXML
    private HTMLEditor txtPatientProblemDesc;

    @FXML
    private ImageView imgPatientProblemPhoto;

    @FXML
    private ComboBox<Network> comboNetworks;

    @FXML
    private ComboBox<Enterprise> comboEnterprises;

    @FXML
    private ComboBox<Organization> comboOrganizations;

    @FXML
    private Button btnRemoveImage;

    private ValidationSupport validationSupport;

    private byte[] imageFile;

    RecordPatientProblem(DashboardPrimaryCareProviderController dashboardPrimaryCareProviderController, Patient patient) {
        this.parentController = dashboardPrimaryCareProviderController;
        this.patient = patient;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.PRIMARY_CARE_PROVIDER_VIEWS.RECORD_PATIENT_PROBLEM.getValue()));

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
        imageFile = null;
        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(
                txtPatientProblemDesc,
                true,
                Validator.createEmptyValidator("Please enter a description for patient problem")
        );
        validationSupport.registerValidator(comboNetworks, Validator.createEmptyValidator( "Please select a network"));
        validationSupport.registerValidator(comboEnterprises, Validator.createEmptyValidator( "Please select an enteprise"));
        validationSupport.registerValidator(comboOrganizations, Validator.createEmptyValidator( "Please select an organization"));



        txtPatientName.setText(patient.getName());
        txtPatientDob.setText(Utils.formatDate(patient.getDateOfBirth()));
        txtPatientGender.setText(patient.getGender().getValue());
        txtPatientHeight.setText(Integer.toString(patient.getHeight()));
        txtPatientWeight.setText(Integer.toString(patient.getWeight()));
        txtPatientPhone.setText(Long.toString(patient.getPhoneNumber()));

        imgPatientProblemPhoto.setEffect(new DropShadow( 15, Color.CHOCOLATE ));

        comboNetworks.setItems(FXCollections.observableArrayList(app.getSystem().getNetworkList()));

        comboNetworks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Network> observableValue, Network oldSelection, Network newSelection) {
                if (newSelection != null) {
                    comboEnterprises.setItems(FXCollections.observableArrayList(newSelection.getEnterpriseDirectory().getEnterprisesByType(Enterprise.Type.REMOTE_HEALTHCARE_PROVIDER)));

                    comboEnterprises.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
                        @Override
                        public void changed(ObservableValue<? extends Enterprise> observableValue, Enterprise oldSelection, Enterprise newSelection) {
                            if (newSelection != null) {
                                comboOrganizations.setItems(FXCollections.observableArrayList(newSelection.getOrganizationDirectory().getOrganizationsByType(Organization.Type.DOCTORS_ORGANIZATION)));
                            }
                        }
                    });
                }
            }
        });


    }

    @FXML
    public void createWorkRequest() {

        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            return;
        }

        if(txtPatientProblemDesc.getHtmlText().trim().isEmpty()){

            app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.ERROR, "Please enter a problem description").show();

        }

        DoctorOpinionRequest doctorOpinionRequest = new DoctorOpinionRequest();
        doctorOpinionRequest.setPatientProblem(txtPatientProblemDesc.getHtmlText());
        doctorOpinionRequest.setImage(imageFile);
        doctorOpinionRequest.setPatient(patient);
        doctorOpinionRequest.setSender(app.getLoggedInUserAccount());
        doctorOpinionRequest.setSentTo(comboOrganizations.getSelectionModel().getSelectedItem());
        doctorOpinionRequest.setDoctorOpinionRequest(doctorOpinionRequest);
        app.getLoggedInUserAccount().getWorkQueue().addWorkRequest(doctorOpinionRequest);
        doctorOpinionRequest.getSentTo().getWorkQueue().addWorkRequest(doctorOpinionRequest);

        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Request Forwarded Successfully").show();
        Utils.getGlobalLogger().info("Recorded patient problem and forwarded request");

        app.storeSystem();
        parentController.trackRequests();
    }

    @FXML
    private void uploadPhoto(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();

        chooser.setTitle("Select a  File");

        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg", "*.gif"));

        File selectedFile = chooser.showOpenDialog(imgPatientProblemPhoto.getScene().getWindow());

        if (selectedFile != null) {
            try {
                imageFile = FileUtils.readFileToByteArray(selectedFile);
                imgPatientProblemPhoto.setImage(new Image(FileUtils.openInputStream(selectedFile)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        btnRemoveImage.setDisable(false);
    }

    @FXML
    private void removeUploaded(ActionEvent actionEvent) {
        imgPatientProblemPhoto.setImage(new Image(getClass().getResourceAsStream("/images/dummy.png")));
        imageFile = null;
        btnRemoveImage.setDisable(true);
    }
}
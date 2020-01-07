package edu.neu.javachip.aedfinalproject.ui.labtechnician;

import edu.neu.javachip.aedfinalproject.model.patient.Patient;
import edu.neu.javachip.aedfinalproject.model.workqueue.LabResultWorkRequest;
import edu.neu.javachip.aedfinalproject.model.workqueue.LabTestWorkRequest;
import edu.neu.javachip.aedfinalproject.model.workqueue.WorkRequest;
import edu.neu.javachip.aedfinalproject.ui.StringConstants;
import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import edu.neu.javachip.aedfinalproject.util.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ProcessLabTestRequest extends AnchorPane {
    private Context app;
    private Dashboard parentController;
    private LabTestWorkRequest labTestWorkRequest;

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
    private TextArea txtPatientProblemDesc;

    @FXML
    private TextField txtNetwork;

    @FXML
    private TextField txtEnterprise;

    @FXML
    private TextField txtOrganization;

    @FXML
    private TextField txtDigitalHealthcareProvider;

    @FXML
    private TextArea txtResults;

    @FXML
    private SplitPane splitPane;

    ProcessLabTestRequest(Dashboard dashboard, LabTestWorkRequest labTestWorkRequest) {
        this.parentController = dashboard;
        this.labTestWorkRequest = labTestWorkRequest;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.LAB_TECHS_VIEWS.PROCESS_TEST_REQUEST.getValue()));

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

        Patient patient = labTestWorkRequest.getPatient();

        txtPatientName.setText(patient.getName());
        txtPatientDob.setText(Utils.formatDate(patient.getDateOfBirth()));
        txtPatientGender.setText(patient.getGender().getValue());
        txtPatientHeight.setText(Integer.toString(patient.getHeight()));
        txtPatientWeight.setText(Integer.toString(patient.getWeight()));
        txtPatientPhone.setText(Long.toString(patient.getPhoneNumber()));
        txtPatientProblemDesc.setText(labTestWorkRequest.getLabTests());

        txtNetwork.setText(labTestWorkRequest.getSender().getEmployee().getEnterprise().getParentNetwork().getName());
        txtEnterprise.setText(labTestWorkRequest.getSender().getEmployee().getEnterprise().getName());
        txtOrganization.setText(labTestWorkRequest.getSender().getEmployee().getOrganization().getName());
        txtDigitalHealthcareProvider.setText(labTestWorkRequest.getSender().getEmployee().getName());

    }

    @FXML
    private void reportTestResults() {
        labTestWorkRequest.setStatus(WorkRequest.WorkRequestStatus.SUCCESS);
        labTestWorkRequest.setMessage("Test results sent back to digital healthcare provider");
        LabResultWorkRequest labResultWorkRequest = new LabResultWorkRequest();
        labResultWorkRequest.setLabResults(txtResults.getText());
        labResultWorkRequest.setPatient(labTestWorkRequest.getPatient());
        labResultWorkRequest.setDoctorOpinionRequest(labTestWorkRequest.getDoctorOpinionRequest());
        labResultWorkRequest.setSender(app.getLoggedInUserAccount());
        labResultWorkRequest.setLabTestWorkRequest(labTestWorkRequest);
        labResultWorkRequest.setReceiver(labTestWorkRequest.getSender());
        labResultWorkRequest.setSentTo(labTestWorkRequest.getSender().getEmployee().getOrganization());
        labResultWorkRequest.setStatus(WorkRequest.WorkRequestStatus.ASSIGNED);
        labResultWorkRequest.getLabTestWorkRequest().getDoctorOpinionRequest().addLabResult(labResultWorkRequest);
        //Send back to the doctor who requested the tests
        labTestWorkRequest.getSender().getWorkQueue().addWorkRequest(labResultWorkRequest);
        app.getLoggedInUserAccount().getWorkQueue().addWorkRequest(labResultWorkRequest);
        labTestWorkRequest.getSender().getEmployee().getOrganization().getWorkQueue().addWorkRequest(labResultWorkRequest);

        Utils.sendSms("Lab Results for Patient " + labTestWorkRequest.getPatient().getName() + " are ready", labResultWorkRequest.getReceiver().getEmployee().getPhoneNumber());
        Utils.sendEmail(labResultWorkRequest.getReceiver().getEmployee().getEmail(), app.getLoggedInUserAccount().getEmployee().getEmail(), "[" + StringConstants.APP_NAME + "] Lab Test Results ready", "Dear " + labResultWorkRequest.getReceiver().getEmployee().getName() + "\n\nLab results for " + labResultWorkRequest.getPatient().getName() + " are ready");

        app.storeSystem();

        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Request Forwarded Successfully")
                .show();
        parentController.trackRequests();
        Utils.getGlobalLogger().info("Lab test request processed and report generated");

    }

}
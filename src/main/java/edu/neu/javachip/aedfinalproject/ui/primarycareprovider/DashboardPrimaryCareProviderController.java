package edu.neu.javachip.aedfinalproject.ui.primarycareprovider;

import edu.neu.javachip.aedfinalproject.model.patient.Patient;
import edu.neu.javachip.aedfinalproject.ui.AbstractParentController;
import javafx.fxml.FXML;

public class DashboardPrimaryCareProviderController extends AbstractParentController {

    @FXML
    protected void initialize() {
        super.initialize();

        setTitle("Primary Care Provider");
        openDashboard();
    }

    @FXML
    private void openDashboard() {
        DashboardContent dashboardContent = new DashboardContent(this);
        mainPane.setCenter(dashboardContent);
    }


    @FXML
    private void managePatients() {
        ManagePatients managePatients = new ManagePatients(this);
        mainPane.setCenter(managePatients);
        setTitle("Manage Patients");
    }

    void openRecordPatientProblem(Patient patient) {
        RecordPatientProblem recordPatientProblem = new RecordPatientProblem(this, patient);
        mainPane.setCenter(recordPatientProblem);
        setTitle("Record Patient Problem");
    }

    @FXML
    void trackRequests() {
        TrackWorkRequests trackWorkRequests= new TrackWorkRequests(this);
        mainPane.setCenter(trackWorkRequests);
        setTitle("Track Doctor's Opinion Requests");
    }

    @FXML
    private void openWorkArea() {
        WorkArea dashboardContent = new WorkArea(this);
        mainPane.setCenter(dashboardContent);
        setTitle("Work Area");
    }

    @FXML
    private void openChatClient() {
        ChatClient dashboardContent = new ChatClient(this);
        mainPane.setCenter(dashboardContent);
        setTitle("Chat");
    }



    /*void manageEnterprises(Network network) {
        ManageEnterprises manageEnterprises = new ManageEnterprises(this, network);
        mainPane.setCenter(manageEnterprises);
        setTitle("Manage Enterprise for " + network.getName());
    }*/

}

package edu.neu.javachip.aedfinalproject.ui.medicalstoreprovider;

import edu.neu.javachip.aedfinalproject.model.workqueue.ProvideMedicalItemsWorkRequest;
import edu.neu.javachip.aedfinalproject.ui.AbstractParentController;
import javafx.fxml.FXML;

public class Dashboard extends AbstractParentController {

    @FXML
    protected void initialize() {
        super.initialize();

        setTitle("Work Area");
        openWorkArea();
    }

    @FXML
    private void openWorkArea() {
        WorkArea dashboardContent = new WorkArea(this);
        mainPane.setCenter(dashboardContent);
    }

    void openRequestForProcessing(ProvideMedicalItemsWorkRequest provideMedicalItemsWorkRequest) {
        ProcessMedicalItemsRequest processMedicalItemsRequest = new ProcessMedicalItemsRequest(this, provideMedicalItemsWorkRequest);
        mainPane.setCenter(processMedicalItemsRequest);
    }

    @FXML
    void trackRequests() {
        TrackWorkRequests trackWorkRequests= new TrackWorkRequests(this);
        mainPane.setCenter(trackWorkRequests);
        setTitle("Track Work Requests");
    }
}

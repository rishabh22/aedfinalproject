package edu.neu.javachip.aedfinalproject.ui.pharmacist;

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
}

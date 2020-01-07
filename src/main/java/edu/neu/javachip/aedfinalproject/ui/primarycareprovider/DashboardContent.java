package edu.neu.javachip.aedfinalproject.ui.primarycareprovider;

import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.model.workqueue.WorkRequest;
import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.stream.Collectors;

public class DashboardContent extends AnchorPane {
    private Context app;
    private DashboardPrimaryCareProviderController parentController;

    @FXML
    private Text txtNetworkName;

    @FXML
    private Label lblPatientCount;

    @FXML
    private Label lblWorkRequestsCreatedCount;

    @FXML
    private Label lblWorkRequestsPendingCount;

    @FXML
    private PieChart patientGenderChart;

    @FXML
    private BarChart<String, Long> barChartYearWisePatientJoining;

    private Network parentNetwork;

    DashboardContent(DashboardPrimaryCareProviderController dashboardPrimaryCareProviderController) {
        this.parentController = dashboardPrimaryCareProviderController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.PRIMARY_CARE_PROVIDER_VIEWS.DASHBOARD_CONTENT.getValue()));

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
        parentNetwork = app.getLoggedInUserAccount().getEmployee().getEnterprise().getParentNetwork();

        txtNetworkName.setText(parentNetwork.getName());
        lblPatientCount.setText(Integer.toString(parentNetwork.getPatientDirectory().getPatientCount()));
        lblWorkRequestsCreatedCount.setText(
                Long.toString(
                        app.getLoggedInUserAccount()
                                .getWorkQueue()
                                .getAllWorkRequests()
                                .stream()
                                .filter(workRequest -> workRequest.getSender().equals(app.getLoggedInUserAccount()))
                                .count()
                )
        );

        lblWorkRequestsPendingCount.setText(
                Long.toString(
                        app.getLoggedInUserAccount()
                                .getWorkQueue()
                                .getRequestsByStatus(WorkRequest.WorkRequestStatus.ASSIGNED)
                                .stream()
                                .filter(workRequest -> workRequest.getReceiver().equals(app.getLoggedInUserAccount()))
                                .count()
                )
        );

        patientGenderChart.setData(FXCollections.observableArrayList(
                parentNetwork.getPatientDirectory().getPatientList()
                        .stream()
                        .collect(Collectors.groupingBy(patient -> patient.getGender().getValue(), Collectors.counting()))
                        .entrySet()
                        .stream()
                        .map(map -> new PieChart.Data(map.getKey(), map.getValue()))
                        .collect(Collectors.toList())
        ));
        patientGenderChart.setTitle("Patients by Gender");

        XYChart.Series<String,Long> series = new XYChart.Series<>();
        series.setName("Number of Patients");
        series.setData(FXCollections.observableArrayList(
                parentNetwork.getPatientDirectory().getPatientList()
                        .stream()
                        .collect(Collectors.groupingBy(patient -> patient.getCreatedOn().getYear(), Collectors.counting()))
                        .entrySet()
                        .stream()
                        .map(map -> new XYChart.Data<>(Integer.toString(map.getKey()), map.getValue()))
                        .collect(Collectors.toList())
        ));
        barChartYearWisePatientJoining.getData().add(series);
        barChartYearWisePatientJoining.setTitle("Year Wise Patients Joining System");

    }
}

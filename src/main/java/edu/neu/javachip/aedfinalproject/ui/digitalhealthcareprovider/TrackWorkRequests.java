package edu.neu.javachip.aedfinalproject.ui.digitalhealthcareprovider;

import edu.neu.javachip.aedfinalproject.model.workqueue.WorkRequest;
import edu.neu.javachip.aedfinalproject.ui.StringConstants;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.table.TableFilter;

import java.io.IOException;

public class TrackWorkRequests extends AnchorPane {
    private Context app;
    private Dashboard parentController;

    @FXML
    private TableView<WorkRequest> tblAllRequests;

    @FXML
    private TableColumn<WorkRequest, String> colOriginatingNetwork;

    @FXML
    private TableColumn<WorkRequest, String> colOriginatingEnterprise;

    @FXML
    private TableColumn<WorkRequest, String> colPatientName;

    @FXML
    private TableColumn<WorkRequest, String> colSender;

    @FXML
    private TableColumn<WorkRequest, String> colReceiver;

    @FXML
    private TableColumn<WorkRequest, String> colStatus;

    @FXML
    private TableColumn<WorkRequest, String> colSentOn;

    @FXML
    private TableColumn<WorkRequest, String> colCompletedOn;

    @FXML
    private TableColumn<WorkRequest, String> colMessage;

    @FXML
    private TableColumn<WorkRequest, String> colReceivingNetwork;

    @FXML
    private TableColumn<WorkRequest, String> colReceivingEnterprise;

    @FXML
    private TableColumn<WorkRequest, String> colReceivingOrganization;

    @FXML
    private Button btnCancelRequest;

    private ObservableList<WorkRequest> allRequestsObservableList;

    TrackWorkRequests(Dashboard dashboard) {
        this.parentController = dashboard;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.TRACK_WORK_REQUESTS));

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
        allRequestsObservableList = FXCollections.observableArrayList(app.getLoggedInUserAccount().getWorkQueue().getSentRequests(app.getLoggedInUserAccount()));
        tblAllRequests.setItems(allRequestsObservableList);
        mapTableColumns();
        TableFilter.forTableView(tblAllRequests).apply();
        tblAllRequests.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends WorkRequest> observableValue, WorkRequest oldSelection, WorkRequest newSelection) {
                //toUpdate=null;
                boolean disable = true;
                if (newSelection != null) {
                    disable = false;
                }
                btnCancelRequest.setDisable(disable);
            }
        });

    }

    public void mapTableColumns() {
        colOriginatingNetwork.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSender().getEmployee().getEnterprise().getParentNetwork().getName()));
        colOriginatingEnterprise.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSender().getEmployee().getEnterprise().getName()));
        colPatientName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPatient().getName()));
        colSender.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSender().getEmployee().getName()));
        colReceiver.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getReceiver() != null ? cellData.getValue().getReceiver().getEmployee().getName() : null));
        colStatus.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus().getValue()));
        colSentOn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Utils.formatDateTime(cellData.getValue().getRequestDate())));
        colCompletedOn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getResolveDate() != null ? Utils.formatDateTime(cellData.getValue().getResolveDate()) : null));
        colMessage.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMessage()));
        colReceivingNetwork.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSentTo().getParentEnterprise().getParentNetwork().getName()));
        colReceivingEnterprise.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSentTo().getParentEnterprise().getName()));
        colReceivingOrganization.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSentTo().getName()));
    }

    @FXML
    private void cancelWorkRequest() {
        WorkRequest workRequest = tblAllRequests.getSelectionModel().getSelectedItem();
        if (!workRequest.getStatus().equals(WorkRequest.WorkRequestStatus.PENDING)) {
            app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR, "The selected work request is not pending and therefore cannot be cancelled")
                    .show();
            return;
        }
        int index = allRequestsObservableList.indexOf(workRequest);

        workRequest.getSentTo().getWorkQueue().getAllWorkRequests().remove(workRequest);

        workRequest.setStatus(WorkRequest.WorkRequestStatus.CANCELLED);
        allRequestsObservableList.set(index, workRequest);
    }


}

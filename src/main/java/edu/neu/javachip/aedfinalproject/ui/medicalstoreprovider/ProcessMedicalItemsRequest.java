package edu.neu.javachip.aedfinalproject.ui.medicalstoreprovider;

import edu.neu.javachip.aedfinalproject.model.enterprise.Enterprise;
import edu.neu.javachip.aedfinalproject.model.medicalstore.MedicalStoreInventoryItem;
import edu.neu.javachip.aedfinalproject.model.medicalstore.MedicalStoreRequestTableModel;
import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.model.organization.MedicalStoreOrganization;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.model.workqueue.ProvideMedicalItemsWorkRequest;
import edu.neu.javachip.aedfinalproject.model.workqueue.ProvideTreatmentWorkRequest;
import edu.neu.javachip.aedfinalproject.model.workqueue.RestockMedicalInventoryWorkRequest;
import edu.neu.javachip.aedfinalproject.model.workqueue.WorkRequest;
import edu.neu.javachip.aedfinalproject.ui.StringConstants;
import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import edu.neu.javachip.aedfinalproject.util.Utils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessMedicalItemsRequest extends AnchorPane {
    private Context app;
    private Dashboard parentController;
    private ProvideMedicalItemsWorkRequest provideMedicalItemsWorkRequest;


    @FXML
    private ComboBox<Network> comboNetworks;

    @FXML
    private ComboBox<Enterprise> comboEnterprises;

    @FXML
    private ComboBox<Organization> comboOrganizations;

    @FXML
    private TextField txtDigitalHealthcareProvider;

    @FXML
    private TableView<MedicalStoreRequestTableModel> tblMedicalItemsRequest;

    @FXML
    private TableColumn<MedicalStoreRequestTableModel, String> colItemName;

    @FXML
    private TableColumn<MedicalStoreRequestTableModel, String> colItemType;

    @FXML
    private TableColumn<MedicalStoreRequestTableModel, Integer> colQuantityRequested;

    @FXML
    private TableColumn<MedicalStoreRequestTableModel, Integer> colQuantityAvailable;

    @FXML
    private TableColumn<MedicalStoreRequestTableModel, Integer> colShortfall;

    private List<MedicalStoreRequestTableModel> tableData;

    @FXML
    private Button btnRequestRestock;

    @FXML
    private Button btnSubmitRestockRequest;

    @FXML
    private Button btnSupply;

    ProcessMedicalItemsRequest(Dashboard dashboard, ProvideMedicalItemsWorkRequest provideMedicalItemsWorkRequest) {
        this.parentController = dashboard;
        this.provideMedicalItemsWorkRequest = provideMedicalItemsWorkRequest;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.MEDICAL_STORE_PROVIDER_VIEWS.PROCESS_MEDICAL_ITEM_REQUEST.getValue()));

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
                                comboOrganizations.setItems(FXCollections.observableArrayList(newSelection.getOrganizationDirectory().getOrganizationsByType(Organization.Type.PHARMACY_ORGANIZATION)));
                            }
                        }
                    });
                }
            }
        });

        MedicalStoreOrganization userOrg = (MedicalStoreOrganization) app.getLoggedInUserAccount().getEmployee().getOrganization();
        //userOrg.setMedicalStoreInventory(new MedicalStoreInventory());
        tableData = provideMedicalItemsWorkRequest.getMedicalStoreInventoryItems()
                .stream()
                .map(
                        medicalStoreInventoryItem -> new MedicalStoreRequestTableModel(
                                new SimpleStringProperty(medicalStoreInventoryItem.getMedicalItem().getName()),
                                new SimpleStringProperty(medicalStoreInventoryItem.getMedicalItem().getType().getValue()),
                                new SimpleIntegerProperty(medicalStoreInventoryItem.getQuantity()),
                                new SimpleIntegerProperty(userOrg.getMedicalStoreInventory().getQuantityByItem(medicalStoreInventoryItem.getMedicalItem())),
                                new SimpleIntegerProperty(Math.max(0, medicalStoreInventoryItem.getQuantity() - userOrg.getMedicalStoreInventory().getQuantityByItem(medicalStoreInventoryItem.getMedicalItem())))
                        )
                ).collect(Collectors.toList());
        tblMedicalItemsRequest.setItems(FXCollections.observableList(tableData));

        mapTableColumns();

        colShortfall.setCellFactory(column -> {
            return new TableCell<MedicalStoreRequestTableModel, Integer>() {
                @Override
                protected void updateItem(Integer shortfall, boolean empty) {
                    super.updateItem(shortfall, empty);

                    setText(empty ? "" : getItem().toString());
                    setGraphic(null);

                    TableRow<MedicalStoreRequestTableModel> currentRow = getTableRow();

                    if (!isEmpty()) {
                        if (shortfall > 0) {
                            currentRow.setStyle("-fx-background-color:lightcoral");
                            btnSupply.setDisable(true);
                            btnRequestRestock.setDisable(false);
                        }/*
                        else
                            currentRow.setStyle("-fx-background-color:lightgreen");*/
                    }
                }
            };
        });
    }

    public void mapTableColumns() {

        colItemName.setCellValueFactory(new PropertyValueFactory<>("medicalItem"));
        colItemType.setCellValueFactory(new PropertyValueFactory<>("medicalItemType"));
        colQuantityRequested.setCellValueFactory(new PropertyValueFactory<>("quantityRequested"));
        colQuantityAvailable.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));
        colShortfall.setCellValueFactory(new PropertyValueFactory<>("shortfall"));

    }


    @FXML
    private void enableRequestRestock() {
        comboNetworks.setDisable(false);
        comboEnterprises.setDisable(false);
        comboOrganizations.setDisable(false);
        btnSubmitRestockRequest.setDisable(false);
    }

    @FXML
    private void processResupplyRequest() {

        List<MedicalStoreInventoryItem> shortfallList = tableData.stream()
                .filter(medicalStoreRequestTableModel -> medicalStoreRequestTableModel.getShortfall() > 0)
                .map(medicalStoreRequestTableModel -> new MedicalStoreInventoryItem(app.getSystem().getMedicalEquipmentCatalogue().getItemByName(medicalStoreRequestTableModel.getMedicalItem()), medicalStoreRequestTableModel.getShortfall()))
                .collect(Collectors.toList());

//        provideMedicalItemsWorkRequest.setStatus(WorkRequest.WorkRequestStatus.SUCCESS);
        provideMedicalItemsWorkRequest.setStatus(WorkRequest.WorkRequestStatus.WAITING);
        provideMedicalItemsWorkRequest.setMessage("Requested restock of inventory");

        RestockMedicalInventoryWorkRequest restockMedicalInventoryWorkRequest = new RestockMedicalInventoryWorkRequest();
        restockMedicalInventoryWorkRequest.setMedicalStoreInventoryItems(shortfallList);
        restockMedicalInventoryWorkRequest.setPatient(provideMedicalItemsWorkRequest.getPatient());
        restockMedicalInventoryWorkRequest.setProvideMedicalItemsWorkRequest(provideMedicalItemsWorkRequest);
        restockMedicalInventoryWorkRequest.setDoctorOpinionRequest(provideMedicalItemsWorkRequest.getDoctorOpinionRequest());
        restockMedicalInventoryWorkRequest.setSender(app.getLoggedInUserAccount());
        restockMedicalInventoryWorkRequest.setSentTo(comboOrganizations.getSelectionModel().getSelectedItem());
        app.getLoggedInUserAccount().getWorkQueue().addWorkRequest(restockMedicalInventoryWorkRequest);
        comboOrganizations.getSelectionModel().getSelectedItem().getWorkQueue().addWorkRequest(restockMedicalInventoryWorkRequest);
        app.storeSystem();
        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Request Forwarded Successfully")
                .show();
        parentController.trackRequests();
        Utils.getGlobalLogger().info("Requested restock of medicines");

    }

    @FXML
    private void processSupplyRequest() {
        provideMedicalItemsWorkRequest.setStatus(WorkRequest.WorkRequestStatus.SUCCESS);
        provideMedicalItemsWorkRequest.setMessage("Supplied");
        ProvideTreatmentWorkRequest provideTreatmentWorkRequest = new ProvideTreatmentWorkRequest();
        provideTreatmentWorkRequest.setPrescription(provideMedicalItemsWorkRequest.getPrescription());
        provideTreatmentWorkRequest.setMedicalStoreInventoryItems(provideMedicalItemsWorkRequest.getMedicalStoreInventoryItems());
        /*MedicalStoreOrganization medicalStoreOrganization = (MedicalStoreOrganization) app.getLoggedInUserAccount().getEmployee().getOrganization();
        for (MedicalStoreInventoryItem itemRequested : provideTreatmentWorkRequest.getMedicalStoreInventoryItems()) {
            MedicalStoreInventoryItem itemInInventory = medicalStoreOrganization.getMedicalStoreInventory().getItemByName(itemRequested.getMedicalItem().getName());
            itemInInventory.setQuantity(itemInInventory.getQuantity()-itemRequested.getQuantity());
        }*/
        provideTreatmentWorkRequest.setPatient(provideMedicalItemsWorkRequest.getPatient());
        provideTreatmentWorkRequest.setDoctorOpinionRequest(provideMedicalItemsWorkRequest.getDoctorOpinionRequest());
        provideTreatmentWorkRequest.setSender(app.getLoggedInUserAccount());
        provideTreatmentWorkRequest.setReceiver(provideTreatmentWorkRequest.getDoctorOpinionRequest().getSender());
        provideTreatmentWorkRequest.setSentTo(provideTreatmentWorkRequest.getReceiver().getEmployee().getOrganization());
        provideTreatmentWorkRequest.setStatus(WorkRequest.WorkRequestStatus.ASSIGNED);
        //Send back to the primary care provider who initiated the treatment
        provideTreatmentWorkRequest.getDoctorOpinionRequest().getSender().getWorkQueue().addWorkRequest(provideTreatmentWorkRequest);
        app.getLoggedInUserAccount().getWorkQueue().addWorkRequest(provideTreatmentWorkRequest);
        provideTreatmentWorkRequest.getDoctorOpinionRequest().getSender().getEmployee().getOrganization().getWorkQueue().addWorkRequest(provideTreatmentWorkRequest);

        Utils.sendSms("Medical Supplies for " + provideTreatmentWorkRequest.getPatient().getName() + " are ready", provideTreatmentWorkRequest.getReceiver().getEmployee().getPhoneNumber());
        Utils.sendEmail(provideTreatmentWorkRequest.getReceiver().getEmployee().getEmail(), app.getLoggedInUserAccount().getEmployee().getEmail(), "[" + StringConstants.APP_NAME + "] Medical Supplies Ready", "Dear " + provideTreatmentWorkRequest.getReceiver().getEmployee().getName() + "\n\nMedical supplies for " + provideTreatmentWorkRequest.getPatient().getName() + " are ready");


        app.storeSystem();
        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Request Forwarded Successfully")
                .show();
        parentController.trackRequests();
        Utils.getGlobalLogger().info("Forwarded medical supply request");

    }

}
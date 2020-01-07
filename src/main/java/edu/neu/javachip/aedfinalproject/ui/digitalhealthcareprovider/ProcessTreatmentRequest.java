package edu.neu.javachip.aedfinalproject.ui.digitalhealthcareprovider;

import edu.neu.javachip.aedfinalproject.model.enterprise.Enterprise;
import edu.neu.javachip.aedfinalproject.model.medicalstore.MedicalItem;
import edu.neu.javachip.aedfinalproject.model.medicalstore.MedicalStoreInventoryItem;
import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.model.patient.Patient;
import edu.neu.javachip.aedfinalproject.model.workqueue.*;
import edu.neu.javachip.aedfinalproject.ui.StringConstants;
import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import edu.neu.javachip.aedfinalproject.util.Utils;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import org.apache.commons.io.IOUtils;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ProcessTreatmentRequest extends AnchorPane {
    private Context app;
    private Dashboard parentController;
    //    private LabResultWorkRequest labResultWorkRequest;
    private DoctorOpinionRequest doctorOpinionRequest;
    private ToggleGroup radioGroup;

    @FXML
    private RadioButton rbPrescription;
    @FXML
    private RadioButton rbLabTests;

    @FXML
    private RadioButton rbChat;

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
    private WebView txtPatientProblemDesc;

    @FXML
    private TextArea txtLabResults;

    @FXML
    private TextField txtNetwork;

    @FXML
    private TextField txtEnterprise;

    @FXML
    private ComboBox<Organization> comboOrganization;

    /*@FXML
    private TextField txtMedicalStore;*/

    @FXML
    private TextArea txtPrescription;

    @FXML
    private TextArea txtTests;

    @FXML
    private TextArea txtPatientTests;

    @FXML
    private TextField txtMedicalItem;

    @FXML
    private TextField txtMedicalItemQuantity;

    @FXML
    private ImageView imgPatientProblem;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TableView<MedicalStoreInventoryItem> tblItemList;

    @FXML
    private TableColumn<MedicalStoreInventoryItem, String> colMedicalItemName;

    @FXML
    private TableColumn<MedicalStoreInventoryItem, String> colMedicalItemType;

    @FXML
    private TableColumn<MedicalStoreInventoryItem, Integer> colMedicalItemQuantity;

    @FXML
    private Button btnMedicalItemRemove;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtMessage;

    @FXML
    private TextArea txtLog;

    @FXML
    private Button btnStartServer;

    @FXML
    private Button btnCloseServer;

    @FXML
    private Button btnSendMessage;

    private MedicalItem currentAutoCompleted;

    private ObservableList<MedicalStoreInventoryItem> medicalStoreInventoryObservableItems = FXCollections.observableArrayList();
    private ObservableList<MedicalItem> overallMedicalItemList;

    private AutoCompletionBinding<MedicalItem> autoCompletionBinding;

    private ChatHandler chatHandler;


    ProcessTreatmentRequest(Dashboard dashboard, DoctorOpinionRequest workRequest) {
        this.parentController = dashboard;
        /*if (workRequest instanceof LabResultWorkRequest) {
            this.doctorOpinionRequest = ((LabResultWorkRequest) workRequest).getLabTestWorkRequest().getDoctorOpinionRequest();
            this.labResultWorkRequest = (LabResultWorkRequest) workRequest;
        } else {
            this.doctorOpinionRequest = (DoctorOpinionRequest) workRequest;
        }*/
        this.doctorOpinionRequest = workRequest;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.DOCTOR_VIEWS.PROCESS_TREATMENT_REQUEST.getValue()));

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


        radioGroup = new ToggleGroup();
        rbPrescription.setToggleGroup(radioGroup);
        rbLabTests.setToggleGroup(radioGroup);
        rbChat.setToggleGroup(radioGroup);

        Patient patient = doctorOpinionRequest.getPatient();

        txtPatientName.setText(patient.getName());
        txtPatientDob.setText(Utils.formatDate(patient.getDateOfBirth()));
        txtPatientGender.setText(patient.getGender().getValue());
        txtPatientHeight.setText(Integer.toString(patient.getHeight()));
        txtPatientWeight.setText(Integer.toString(patient.getWeight()));
        txtPatientPhone.setText(Long.toString(patient.getPhoneNumber()));
//        app.hideHTMLEditorToolbars(txtPatientProblemDesc);
        txtPatientProblemDesc.getEngine().loadContent(doctorOpinionRequest.getPatientProblem().replaceAll("contenteditable=\"true\"","contenteditable=\"false\""));
        if (doctorOpinionRequest.getImage() != null) {
            imgPatientProblem.setImage(new Image(new ByteArrayInputStream(doctorOpinionRequest.getImage())));
        }

        radioGroup.selectedToggleProperty().addListener(new ChangeListener<>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle oldToggle, Toggle newToggle) {
                if (radioGroup.getSelectedToggle() != null) {
                    if (radioGroup.getSelectedToggle().equals(rbLabTests)) {
                        openPrescribeLabTests();
                    } else if (radioGroup.getSelectedToggle().equals(rbPrescription)) {
                        openPrescribeTreatment();
                    } else if (radioGroup.getSelectedToggle().equals(rbChat)) {
                        openChat();
                    }
                }
            }
        });

        if (!doctorOpinionRequest.getLabResultWorkRequests().isEmpty()) {
            StringBuilder labResultsTxt = new StringBuilder();
            for (LabResultWorkRequest labResultWorkRequest : doctorOpinionRequest.getLabResultWorkRequests()) {
                labResultsTxt.append("Tests Requested:\n").append(labResultWorkRequest.getLabTestWorkRequest().getLabTests());
                labResultsTxt.append("\n\n");
                labResultsTxt.append("Results:\n").append(labResultWorkRequest.getLabResults());
                labResultsTxt.append("\n\n");
                labResultsTxt.append("-------------------------------------------------------------------");
                labResultsTxt.append("\n\n");
            }
            txtPatientTests.setText(labResultsTxt.toString());
        }


    }



    private void openPrescribeLabTests() {
        AnchorPane addPane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.DOCTOR_VIEWS.PRESCRIBE_TESTS.getValue()));

        fxmlLoader.setRoot(addPane);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        splitPane.getItems().set(1, addPane);

        txtNetwork.setText(doctorOpinionRequest.getSender().getEmployee().getEnterprise().getParentNetwork().getName());
        txtEnterprise.setText(doctorOpinionRequest.getSender().getEmployee().getEnterprise().getName());
        Network network = app.getSystem().getNetworkById(doctorOpinionRequest.getSender().getEmployee().getEnterprise().getParentNetwork().getId());
        if (network != null) {
            Enterprise enterprise = network.getEnterpriseDirectory().getEnterpriseById(doctorOpinionRequest.getSender().getEmployee().getEnterprise().getId());
            if (enterprise != null) {
                comboOrganization.setItems(FXCollections.observableArrayList(enterprise.getOrganizationDirectory().getOrganizationsByType(Organization.Type.LAB_ORGANIZATION)));
            }
        }
    }

    private void openPrescribeTreatment() {
        AnchorPane addPane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.DOCTOR_VIEWS.PRESCRIBE_TREATMENT.getValue()));

        fxmlLoader.setRoot(addPane);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        splitPane.getItems().set(1, addPane);

        txtNetwork.setText(doctorOpinionRequest.getSender().getEmployee().getEnterprise().getParentNetwork().getName());
        txtEnterprise.setText(doctorOpinionRequest.getSender().getEmployee().getEnterprise().getName());
        /*List<Organization> medicalStoreList = doctorOpinionRequest.getSender().getEmployee().getEnterprise().getOrganizationDirectory().getOrganizationsByType(Organization.Type.MEDICAL_STORE_ORGANIZATION);
        if (!medicalStoreList.isEmpty()) {
            txtMedicalStore.setText(medicalStoreList.get(0).getName());
        }*/

        txtMedicalItemQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtMedicalItemQuantity.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        overallMedicalItemList = FXCollections.observableArrayList(app.getSystem().getMedicalEquipmentCatalogue().getMedicalItemsSet());
        /*AutoCompletionBinding<MedicalItem> autoCompletionBinding = TextFields.bindAutoCompletion(txtMedicalItem, overallMedicalItemList);

        autoCompletionBinding.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<MedicalItem>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<MedicalItem> medicalItemAutoCompletionEvent) {
                currentAutoCompleted = medicalItemAutoCompletionEvent.getCompletion();
            }
        });*/
        bindAutocompletion();
        txtMedicalItem.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    if (currentAutoCompleted == null || !currentAutoCompleted.getName().equals(txtMedicalItem.getText())) {
                        app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR, "Please select a value only from the autocompletion").show();
                        //txtMedicalItem.requestFocus();
                        currentAutoCompleted = null;
                    }
                }
            }
        });
        tblItemList.setItems(medicalStoreInventoryObservableItems);
        colMedicalItemName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMedicalItem().getName()));
        colMedicalItemType.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMedicalItem().getType().getValue()));
        colMedicalItemQuantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantity()));
        tblItemList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends MedicalStoreInventoryItem> observableValue, MedicalStoreInventoryItem oldSelection, MedicalStoreInventoryItem newSelection) {
                //toUpdate=null;
                boolean disable = true;
                if (newSelection != null) {
                    disable = false;
                }
                btnMedicalItemRemove.setDisable(disable);
            }
        });
    }

    private void openChat() {
        AnchorPane addPane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.DOCTOR_VIEWS.CHAT_SERVER.getValue()));

        fxmlLoader.setRoot(addPane);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        splitPane.getItems().set(1, addPane);
    }

    @FXML
    private void prescribeTreatment() {

        if (txtPrescription.getText().isBlank()) {
            app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR, "Please provide a prescription").show();
            return;
        }


        if (tblItemList.getItems().isEmpty()) {
            doctorOpinionRequest.setMessage("Prescription Provided");
            ProvideTreatmentWorkRequest provideTreatmentWorkRequest = new ProvideTreatmentWorkRequest();
            provideTreatmentWorkRequest.setPrescription(txtPrescription.getText());
            provideTreatmentWorkRequest.setPatient(doctorOpinionRequest.getPatient());
            provideTreatmentWorkRequest.setDoctorOpinionRequest(doctorOpinionRequest);
            provideTreatmentWorkRequest.setSender(app.getLoggedInUserAccount());
            provideTreatmentWorkRequest.setSentTo(doctorOpinionRequest.getSender().getEmployee().getOrganization());
            provideTreatmentWorkRequest.setReceiver(doctorOpinionRequest.getSender());
            provideTreatmentWorkRequest.setStatus(WorkRequest.WorkRequestStatus.ASSIGNED);
            app.getLoggedInUserAccount().getWorkQueue().addWorkRequest(provideTreatmentWorkRequest);
            provideTreatmentWorkRequest.getSentTo().getWorkQueue().addWorkRequest(provideTreatmentWorkRequest);
        } else {
            List<Organization> medicalStoreList = doctorOpinionRequest.getSender().getEmployee().getEnterprise().getOrganizationDirectory().getOrganizationsByType(Organization.Type.MEDICAL_STORE_ORGANIZATION);
            if (medicalStoreList.isEmpty()) {
                app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR, "No Medical Stores found for the Primary Care Organization").show();
                return;
            }

            doctorOpinionRequest.setMessage("Prescription sent to medical store");
            ProvideMedicalItemsWorkRequest provideMedicalItemsWorkRequest = new ProvideMedicalItemsWorkRequest();
            provideMedicalItemsWorkRequest.setPrescription(txtPrescription.getText());
            provideMedicalItemsWorkRequest.setMedicalStoreInventoryItems(new ArrayList<>(tblItemList.getItems()));
            provideMedicalItemsWorkRequest.setPatient(doctorOpinionRequest.getPatient());
            provideMedicalItemsWorkRequest.setDoctorOpinionRequest(doctorOpinionRequest);
            provideMedicalItemsWorkRequest.setSender(app.getLoggedInUserAccount());
            provideMedicalItemsWorkRequest.setSentTo(medicalStoreList.get(0));
            app.getLoggedInUserAccount().getWorkQueue().addWorkRequest(provideMedicalItemsWorkRequest);
            provideMedicalItemsWorkRequest.getSentTo().getWorkQueue().addWorkRequest(provideMedicalItemsWorkRequest);

        }
        doctorOpinionRequest.setStatus(WorkRequest.WorkRequestStatus.SUCCESS);
        closeLabResultRequest(doctorOpinionRequest);
        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Request Forwarded Successfully")
                .show();
        app.storeSystem();
        parentController.trackRequests();
        Utils.getGlobalLogger().info("Request forwarded successfully");


    }

    @FXML
    private void prescribeTests() {
        doctorOpinionRequest.setStatus(WorkRequest.WorkRequestStatus.SUCCESS);
        doctorOpinionRequest.setMessage("Tests recommended");
        LabTestWorkRequest labTestWorkRequest = new LabTestWorkRequest();
        labTestWorkRequest.setLabTests(txtTests.getText());
        labTestWorkRequest.setPatient(doctorOpinionRequest.getPatient());
        labTestWorkRequest.setDoctorOpinionRequest(doctorOpinionRequest);
        labTestWorkRequest.setSender(app.getLoggedInUserAccount());
        labTestWorkRequest.setSentTo(comboOrganization.getSelectionModel().getSelectedItem());
        labTestWorkRequest.setDoctorOpinionRequest(doctorOpinionRequest);
        closeLabResultRequest(doctorOpinionRequest);
        //labTestWorkRequest.getTestRequests().add(labTestWorkRequest);
        app.getLoggedInUserAccount().getWorkQueue().addWorkRequest(labTestWorkRequest);
        labTestWorkRequest.getSentTo().getWorkQueue().addWorkRequest(labTestWorkRequest);

        app.buildAlert(Alert.AlertType.INFORMATION, StringConstants.SUCCESS, "Request Forwarded Successfully")
                .show();
        app.storeSystem();
        parentController.trackRequests();

    }


    private void closeLabResultRequest(DoctorOpinionRequest doctorOpinionRequest) {
        if (!doctorOpinionRequest.getLabResultWorkRequests().isEmpty()) {
            doctorOpinionRequest.getLabResultWorkRequests().get(doctorOpinionRequest.getLabResultWorkRequests().size() - 1).setStatus(WorkRequest.WorkRequestStatus.SUCCESS);
        }
    }

    @FXML
    private void addMedicalItem() {
        if (currentAutoCompleted == null) {
            app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR, "Please select an item from the autocomplete").show();
            return;
        }
        if (txtMedicalItemQuantity.getText().isBlank()) {
            app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR, "Please enter a quantity for the selected item").show();
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(txtMedicalItemQuantity.getText());
        } catch (NumberFormatException e) {
            app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR, "Please enter a valid quantity").show();
            return;
        }
        if (quantity <= 0 || quantity > 1000) {
            app.buildAlert(Alert.AlertType.WARNING, StringConstants.ERROR, "Quantity should be between 1-1000").show();
            return;
        }


        medicalStoreInventoryObservableItems.add(new MedicalStoreInventoryItem(currentAutoCompleted, quantity));
        overallMedicalItemList.remove(currentAutoCompleted);
        bindAutocompletion();
        txtMedicalItem.setText("");
        currentAutoCompleted = null;
        txtMedicalItemQuantity.setText("");

    }

    @FXML
    private void removeMedicalItem() {
        medicalStoreInventoryObservableItems.remove(tblItemList.getSelectionModel().getSelectedItem());
        overallMedicalItemList.add(tblItemList.getSelectionModel().getSelectedItem().getMedicalItem());
        bindAutocompletion();
    }

    private void bindAutocompletion() {
        if (autoCompletionBinding != null) {
            autoCompletionBinding.dispose();
        }
        autoCompletionBinding = TextFields.bindAutoCompletion(txtMedicalItem, overallMedicalItemList);
        autoCompletionBinding.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<MedicalItem>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<MedicalItem> medicalItemAutoCompletionEvent) {
                currentAutoCompleted = medicalItemAutoCompletionEvent.getCompletion();
            }
        });
    }


    @FXML
    private void startServer() {
        try {
            ServerSocket listener = new ServerSocket(Integer.parseInt(txtPort.getText()));
            txtLog.appendText("Started listening for connections on port "+ txtPort.getText());
            txtLog.appendText(System.lineSeparator());
            new Thread(() -> {
                try {
                    chatHandler = new ChatHandler(listener.accept(), txtLog, app.getLoggedInUserAccount().getEmployee().getName(), btnSendMessage, txtMessage);
                    chatHandler.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

//            server = new Server(Integer.parseInt(txtPort.getText()),txtLog);
//            server.start();

            btnStartServer.setDisable(true);
            btnCloseServer.setDisable(false);
//            btnSendMessage.setDisable(false);
//            txtMessage.setDisable(false);
        } catch (Exception e) {
            app.buildAlert(Alert.AlertType.ERROR, StringConstants.ERROR, "Some error occurred").show();
            e.printStackTrace();
        }
    }

    @FXML
    private void sendMessage() {
        try {
            chatHandler.write(txtMessage.getText());
//            server.send(txtMessage.getText());
            txtMessage.setText("");
        } catch (Exception e) {
            app.buildAlert(Alert.AlertType.ERROR, StringConstants.ERROR, "Some error occurred").show();
            e.printStackTrace();
        }
    }


    @FXML
    private void closeServer() {
        try {
//            server.stop();
            chatHandler.closeConnections();
            btnCloseServer.setDisable(true);
            btnStartServer.setDisable(false);
            txtMessage.setDisable(true);
            btnSendMessage.setDisable(true);
        } catch (Exception e) {
            app.buildAlert(Alert.AlertType.ERROR, StringConstants.ERROR, "Some error occurred").show();
            e.printStackTrace();
        }
    }


    private static class ChatHandler extends Thread {
        private Socket socket;
        private OutputStream os;
        private InputStream is;
        private TextArea txtLog;
        private String empName;
        private Button btnSendMessage;
        private TextField txtMessage;

        public ChatHandler(Socket socket, TextArea txtLog, String empName, Button btnSendMessage, TextField txtMessage) throws IOException {
            this.socket = socket;
            this.txtLog = txtLog;
            this.empName = empName;
            this.btnSendMessage = btnSendMessage;
            this.txtMessage = txtMessage;
        }

        @Override
        public void run() {
            try {
                //is = socket.getInputStream();
                //os = socket.getOutputStream();

                if (socket.isConnected()) {
                    Platform.runLater(() -> {
                        txtLog.appendText("Started connection with client on port " + socket.getPort() + System.lineSeparator());
                        btnSendMessage.setDisable(false);
                        txtMessage.setDisable(false);
                    });
                }
                while (!socket.isClosed() && socket.isConnected()) {
                    if (socket.getInputStream().available() > 0) {
                        byte[] buffer;
                        buffer = new byte[socket.getInputStream().available()];
                        socket.getInputStream().read(buffer);
                        String test = new String(buffer);
                        Platform.runLater(() -> {
                            txtLog.appendText(test + System.lineSeparator());
                        });

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnections();
            }
        }


        /*
         * Creates and sends a Message type to the listeners.
         */
        private void write(String message) throws IOException {
            Platform.runLater(() -> {
                txtLog.appendText(empName + ": " + message + System.lineSeparator());
            });
            IOUtils.write((empName + ": " + message).getBytes(), socket.getOutputStream());
        }

        /*
         * Once a user has been disconnected, we close the open connections and remove the writers
         */
        private synchronized void closeConnections() {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
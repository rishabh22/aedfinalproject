package edu.neu.javachip.aedfinalproject.ui.primarycareprovider;

import edu.neu.javachip.aedfinalproject.ui.StringConstants;
import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.io.IOUtils;
import org.controlsfx.validation.ValidationSupport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatClient extends AnchorPane {
    private Context app;
    private DashboardPrimaryCareProviderController parentController;

    @FXML
    private TextArea txtLog;

    @FXML
    private TextField txtServerAddress;

    @FXML
    private TextField txtServerPort;

    @FXML
    private TextField txtMessage;

    @FXML
    private Button btnStartConnection;

    @FXML
    private Button btnDisconnect;

    @FXML
    private Button btnSendMessage;

    private ValidationSupport validationSupport;

    private ChatHandler chatHandler;
    private InputStream in;
    private OutputStream out;


    ChatClient(DashboardPrimaryCareProviderController dashboardPrimaryCareProviderController) {
        this.parentController = dashboardPrimaryCareProviderController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.PRIMARY_CARE_PROVIDER_VIEWS.CHAT_CLIENT.getValue()));

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

    }

    @FXML
    private void startConnection() {
        try {
            Socket socket = new Socket(txtServerAddress.getText(), Integer.parseInt(txtServerPort.getText()));
            out = socket.getOutputStream();
            /*out = socket.getOutputStream();
            new Thread(() -> {
                try {
                    while (socket.isConnected()) {
                        if (socket.getInputStream().available() > 0) {
                            byte[] buffer;
                            buffer = new byte[socket.getInputStream().available()];
                            socket.getInputStream().read(buffer);
                            String test = new String(buffer);
                            Platform.runLater(() -> {
                                txtLog.appendText(test);
                            });

                            // Your code here to deal with buffer.

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();*/
            chatHandler = new ChatHandler(socket, txtLog, app.getLoggedInUserAccount().getEmployee().getName());
            chatHandler.start();
            btnStartConnection.setDisable(true);
            btnDisconnect.setDisable(false);
            btnSendMessage.setDisable(false);
            txtMessage.setDisable(false);
        } catch (Exception e) {
            app.buildAlert(Alert.AlertType.ERROR, StringConstants.ERROR, "Some error occurred").show();
            e.printStackTrace();
        }
    }

    @FXML
    private void sendMessage() {
        try {
            String message = txtMessage.getText();
            /*new Thread(() -> {
                try {
                    out.write(message.getBytes());
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();*/
            chatHandler.write(txtMessage.getText());
            txtMessage.setText("");
        } catch (Exception e) {
            app.buildAlert(Alert.AlertType.ERROR, StringConstants.ERROR, "Some error occurred").show();
            e.printStackTrace();
        }
    }

    @FXML
    private void disconnectClient() {
        try {
            chatHandler.closeConnections();
            btnDisconnect.setDisable(true);
            btnStartConnection.setDisable(false);
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

        public ChatHandler(Socket socket, TextArea txtLog, String empName) throws IOException {
            this.socket = socket;
            this.txtLog = txtLog;
            this.empName = empName;
        }

        @Override
        public void run() {
            try {
                //is = socket.getInputStream();
                //os = socket.getOutputStream();

                if (socket.isConnected()) {
                    Platform.runLater(() -> {
                        txtLog.appendText("Started connection with server on port " + socket.getPort() + System.lineSeparator());
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
            IOUtils.write((empName + ": " +  message).getBytes(), socket.getOutputStream());
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
package edu.neu.javachip.aedfinalproject.chat;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket socket;
    private ConnectionListener connectionListener;
    private TextArea textArea;

    // temp
    private List<Client> clientList = new ArrayList<>();
    // temp end

    public Server(int port, TextArea textArea) {
        this.textArea = textArea;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        connectionListener = new ConnectionListener(this, textArea);
    }

    public void start() throws IOException {
        connectionListener.start();
        new Thread(()->{
            try {
                while (connectionListener.isAlive()) {

                }
                stop();
            } catch (Exception e){

            }
        }).start();

    }

    public void send(String message) {
        // temp will move to a Thread later
        try {
            if (connectionListener.isAlive()) {
                for (Client c : clientList) {
                    c.send("Doctor: " + message);
                }
            }
            //stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // temp end
    }

    public void stop() {
        connectionListener.stop();
        for (Client c : clientList) {
            c.closeSession();
        }

        textArea.appendText("Server terminated!");
    }

    public synchronized void addConnection(Connection connection) {
        Client c = new Client(connection, clientList,textArea);
        clientList.add(c);
        c.startSession();
        textArea.appendText("Client connected");
    }

    public ServerSocket getSocket() {
        return socket;
    }
}

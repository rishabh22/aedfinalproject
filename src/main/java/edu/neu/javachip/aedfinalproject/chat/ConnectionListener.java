package edu.neu.javachip.aedfinalproject.chat;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener implements Runnable {

    private Server server;
    private ServerSocket socket;
    private boolean running;
    private Thread t;
    private TextArea textArea;

    public ConnectionListener(Server server, TextArea textArea) {
        this.server = server;
        this.socket = server.getSocket();
        this.textArea = textArea;
        running = false;
    }

    public synchronized void start() {

        if (running)
            return;

        running = true;
        t = new Thread(this);
        t.start();
    }

    public synchronized void stop() {

        if (!running)
            return;

        textArea.appendText("Terminating connection listener on:" + socket.getLocalSocketAddress() + "...");

        running = false;

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        textArea.appendText("TERMINATED!");
    }

    @Override
    public void run() {

        textArea.appendText("Listening for connections on: " + socket.getLocalSocketAddress());

        try {
            while (running) {
                Socket request = socket.accept();
                Connection connection = new Connection(request);
                server.addConnection(connection);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }

    }

    public boolean isAlive() {

        return running;
    }

}
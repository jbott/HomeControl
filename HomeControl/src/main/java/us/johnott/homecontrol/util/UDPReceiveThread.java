package us.johnott.homecontrol.util;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by jbo on 4/7/14.
 */
public class UDPReceiveThread extends Thread {
    private static final String LOG_NAME = UDPReceiveThread.class.getSimpleName();
    private static final int BUFFER_SIZE = 1024;

    private DatagramSocket socket;

    public UDPReceiveThread(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            Log.e(LOG_NAME, "Error starting socket!");
            e.printStackTrace();
        }
    }

    public void run() {
        byte buffer[] = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(!isInterrupted()) {
            // Wait for UDP Packet
            try {
                socket.receive(packet);
                String data = new String(packet.getData(), packet.getOffset(), packet.getLength()).trim();
                processPacket(data);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_NAME, "Error receiving packet!");
            }
        }
        // Shutdown
        socket.close();
    }

    public void processPacket(String data) {
        Log.d(LOG_NAME, "Data: " + data);
    }
}

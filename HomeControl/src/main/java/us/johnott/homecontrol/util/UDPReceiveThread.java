package us.johnott.homecontrol.util;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import us.johnott.homecontrol.mpd.MPDSocket;

/**
 * Created by jbo on 4/7/14.
 */
public class UDPReceiveThread extends Thread {
    private static final String LOG_NAME = UDPReceiveThread.class.getSimpleName();
    private static final int BUFFER_SIZE = 1024;

    private DatagramSocket listenSocket;
    private static MPDSocket mpdSocket;

    public UDPReceiveThread(int port) {
        try {
            listenSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            Log.e(LOG_NAME, "Error starting listenSocket!");
            e.printStackTrace();
        }
    }

    public void run() {
        mpdSocket = new MPDSocket("192.168.1.91", 6600);
        byte buffer[] = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(!isInterrupted()) {
            // Wait for UDP Packet
            try {
                listenSocket.receive(packet);
                String data = new String(packet.getData(), packet.getOffset(), packet.getLength()).trim();
                processPacket(data);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_NAME, "Error receiving packet!");
            }
        }
        // Shutdown
        listenSocket.close();
    }

    public void processPacket(String data) {
        Log.d(LOG_NAME, "Data: " + data);
        if (data.startsWith("IRCODE=")) {
            data = data.substring(7);
            switch (data) {
                case "77E17A24":
                case "77E1BA24":
                    mpdSocket.sendCommand("pause");
                    break;
                case "77E11024":
                    mpdSocket.sendCommand("previous");
                    break;
                case "77E1E024":
                    mpdSocket.sendCommand("next");
                    break;
            }
        } else {
            switch (data) {
                case "TOUCH":
                    mpdSocket.sendCommand("pause");
                    break;
                case "LONGTOUCH":
                    break;
                case "MOTION":
                    break;
            }
        }

    }
}

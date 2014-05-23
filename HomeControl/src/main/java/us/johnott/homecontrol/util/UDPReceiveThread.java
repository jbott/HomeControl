package us.johnott.homecontrol.util;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import us.johnott.homecontrol.mpd.MPDConnection;
import us.johnott.homecontrol.mpd.command.*;

public class UDPReceiveThread extends Thread {
    private static final String LOG_NAME = UDPReceiveThread.class.getSimpleName();
    private static final int BUFFER_SIZE = 1024;

    private DatagramSocket listenSocket;

    private static MPDConnection mpdConnection;

    public UDPReceiveThread(int port) {
        try {
            listenSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            Log.e(LOG_NAME, "Error starting listenSocket!");
            e.printStackTrace();
        }
    }

    public void run() {
        mpdConnection = new MPDConnection("192.168.1.91", 6600);
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
        if (data.startsWith("IRCODE=")) {
            data = data.substring(7);
            switch (data) {
                case "77E17A24":
                case "77E1BA24":
                    mpdConnection.sendCommand(new PauseCommand());
                    break;
                case "77E11024":
                    mpdConnection.sendCommand(new PreviousCommand());
                    break;
                case "77E1E024":
                    mpdConnection.sendCommand(new NextCommand());
                    break;
                default:
                    // Unsupported code, this may produce large amounts of spam due to false receives, so it is commented out
                    //Log.i(LOG_NAME, "Unknown IRCODE: " + data);
                    break;
            }
        } else {
            switch (data) {
                case "TOUCH":
                    mpdConnection.sendCommand(new PauseCommand());
                    break;
                case "LONGTOUCH":
                    mpdConnection.sendCommand(new NextCommand());
                    break;
                /* Currently Unsupported
                case "MOTION":
                    break;
                */
                default:
                    Log.i(LOG_NAME, "Unknown packet: " + data);
                    break;
            }
        }

    }
}

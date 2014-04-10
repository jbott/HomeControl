package us.johnott.homecontrol.mpd;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

public class MPDSocket {
    private static final String LOG_NAME = MPDSocket.class.getSimpleName();

    private String ip;
    private int port;
    private Socket socket;
    private InputStream input;
    private PrintStream output;

    public MPDSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;
        openSocket();
    }

    private void openSocket() {
         try {
            socket = new Socket(ip, port);
            input = socket.getInputStream();
            output = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_NAME, "Error opening socket!");
        }
    }

    public void closeSocket() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(String command) {
        Log.i("MPDSocket", "Sending command: " + command);
        output.println(command);
        output.flush();
        if (output.checkError()) {
            // Socket died, restart it
            Log.i("MPDSocket", "Socket died, restarting");
            closeSocket();
            openSocket();
            output.println(command);
        }
    }
}

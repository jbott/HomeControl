package us.johnott.homecontrol.mpd;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class MPDSocket {
    private static final String LOG_NAME = MPDSocket.class.getSimpleName();

    private String ip;
    private int port;
    private Socket socket;
    private BufferedReader input;
    private PrintStream output;

    public MPDSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;
        openSocket();
    }

    public void openSocket() {
        try {
            socket = new Socket(ip, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            input.readLine(); // Get rid of the initial OK MPD (Version) line
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
        output.println(command);
        output.flush();
    }

    public boolean hasError() {
        return output.checkError();
    }

    public String getInputLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return "";
        }
    }
}

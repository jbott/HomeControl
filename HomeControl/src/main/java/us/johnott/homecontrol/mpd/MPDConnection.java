package us.johnott.homecontrol.mpd;

import android.util.Log;

import us.johnott.homecontrol.mpd.command.MPDCommand;

public class MPDConnection {
    private static final String LOG_NAME = MPDCommand.class.getSimpleName();

    private static MPDSocket socket;
    private MPDCommand lastCommand;
    private long lastSend;

    public MPDConnection(String ip, int port) {
        socket = new MPDSocket(ip, port);
        lastSend = System.currentTimeMillis();
    }

    // This call is blocking
    public void sendCommand(MPDCommand command) {
        Log.i(LOG_NAME, "Sending command: " + command.getClass().getSimpleName());
        if ((System.currentTimeMillis() - lastSend) > 15000) {
            // Reopen socket if it has already most likely timed out
            Log.i(LOG_NAME, "Over 15 seconds, reopening socket");
            socket.closeSocket();
            socket.openSocket();
        }
        lastSend = System.currentTimeMillis();
        lastCommand = command;
        command.setMpdConnection(this);
        socket.sendCommand(command.getCommand());
        // Try to get response for 2 times
        for (int i = 0; i < 2; i++) {
            if (getResponse())
                break;
            socket.closeSocket();
            socket.openSocket();
        }
    }

    private boolean getResponse() {
        String response = "";
        long startTime = System.currentTimeMillis();
        while (((System.currentTimeMillis() - startTime) < 1000) &&
                !socket.hasError()) {
            String last;
            while (!(last = socket.getInputLine().trim()).equals("")) {
                //Log.i(LOG_NAME, "Response Chunk: " + last);
                response += last + "\n";
                if (last.contains("OK")) {
                    // End of response
                    Log.i(LOG_NAME, "Good command response!");
                    //Log.i(LOG_NAME, "Response:\n" + response);
                    getLastCommand().setResponse(response);
                    return true;
                }
            }
        }
        return false;
    }

    public MPDCommand getLastCommand() {
        return lastCommand;
    }
}

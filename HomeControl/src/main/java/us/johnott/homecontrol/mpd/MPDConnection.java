package us.johnott.homecontrol.mpd;

import android.util.Log;

import us.johnott.homecontrol.mpd.command.MPDCommand;

public class MPDConnection {
    private static final String LOG_NAME = MPDCommand.class.getSimpleName();

    private static MPDSocket socket;
    private MPDCommand lastCommand;

    public MPDConnection(String ip, int port) {
        socket = new MPDSocket(ip, port);
    }

    // This call is blocking
    public void sendCommand(MPDCommand command) {
        Log.i(LOG_NAME, "Sending command: " + command.getClass().getSimpleName());
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
        while (((System.currentTimeMillis() - startTime) < 5000) &&
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

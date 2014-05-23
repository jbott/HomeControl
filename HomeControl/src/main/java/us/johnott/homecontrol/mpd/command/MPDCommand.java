package us.johnott.homecontrol.mpd.command;

import java.util.HashMap;

import us.johnott.homecontrol.mpd.MPDConnection;

public class MPDCommand {
    private String command;
    private String response;
    private boolean didSucceed;

    private boolean parsed;
    private HashMap<String, String> parsedResponse;

    private MPDConnection mpdConnection;

    public MPDCommand(String baseCommand, boolean parsed) {
        command = baseCommand;
        this.parsed = parsed;
        didSucceed = false;
    }

    public MPDCommand(String baseCommand) {
        this(baseCommand, false);
    }

    public String getCommand() {
        return command;
    }

    public void appendToCommand(String toAppend, boolean useSeparator) {
        if (useSeparator)
            command += " ";
        command += toAppend;
    }

    public void appendToCommand(String toAppend) {
        appendToCommand(toAppend, true);
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
        if (parsed)
            parseResponse();
        if (response.contains("OK"))
            didSucceed = true;
    }

    private void parseResponse() {
        parsedResponse = new HashMap<String, String>();
        for (String line : response.split(System.getProperty("line.separator"))) {
            if (!line.contains("OK")) {
                // Not the terminating line
                String[] split = line.split(":");
                parsedResponse.put(split[0].trim(), split[1].trim());
            }
        }
    }

    public HashMap<String, String> getParsedResponse() {
        return parsedResponse;
    }

    public MPDConnection getMpdConnection() {
        return mpdConnection;
    }

    public void setMpdConnection(MPDConnection mpdConnection) {
        this.mpdConnection = mpdConnection;
    }
}

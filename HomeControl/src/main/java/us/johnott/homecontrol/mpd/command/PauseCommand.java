package us.johnott.homecontrol.mpd.command;

public class PauseCommand extends MPDCommand {
    public PauseCommand() {
        super("pause");
    }

    public String getCommand() {
        getMpdConnection().sendCommand(new StatusCommand());
        if (getMpdConnection().getLastCommand().getParsedResponse().get("state").equals("pause")) {
            appendToCommand("0");
        } else {
            appendToCommand("1");
        }

        return super.getCommand();
    }
}

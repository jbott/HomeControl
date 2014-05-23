package us.johnott.homecontrol.mpd.command;

public class PauseCommand extends MPDCommand {
    public PauseCommand() {
        super("pause");
    }

    public String getCommand() {
        StatusCommand status = new StatusCommand();
        getMpdConnection().sendCommand(status);
        if (status.didSucceed() && status.getParsedResponse().get("state").equals("pause")) {
            appendToCommand("0");
        } else {
            appendToCommand("1");
        }

        return super.getCommand();
    }
}

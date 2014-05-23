package us.johnott.homecontrol.mpd.command;

public class StatusCommand extends MPDCommand {
    public StatusCommand() {
        super("status", true);
    }
}

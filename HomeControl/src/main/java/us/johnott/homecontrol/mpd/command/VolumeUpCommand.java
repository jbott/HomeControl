package us.johnott.homecontrol.mpd.command;

/**
 * Created by jbo on 5/22/14.
 */
public class VolumeUpCommand extends MPDCommand{
    public VolumeUpCommand() {
        super("setvol");
    }
    public String getCommand() {
        StatusCommand status = new StatusCommand();
        getMpdConnection().sendCommand(status);
        if (status.didSucceed()) {
            int currVolume = Integer.parseInt(status.getParsedResponse().get("volume"));
            appendToCommand("" + (currVolume + 5));
        }

        return super.getCommand();
    }
}
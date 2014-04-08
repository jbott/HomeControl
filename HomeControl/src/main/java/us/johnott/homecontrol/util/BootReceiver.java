package us.johnott.homecontrol.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jbo on 4/8/14.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Start udp receiver
        Intent udpReceiver = new Intent(context, UDPReceiver.class);
        context.startService(udpReceiver);
    }
}

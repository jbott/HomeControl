package us.johnott.homecontrol.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jbo on 4/8/14.
 */
public class BootReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Start udp reciever
        Intent udpReciever = new Intent(context, UDPReceiver.class);
        context.startService(udpReciever);
    }
}

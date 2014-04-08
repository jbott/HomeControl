package us.johnott.homecontrol.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by jbo on 4/7/14.
 */
public class UDPReceiver extends Service {
    private static final String LOG_NAME = UDPReceiver.class.getSimpleName();
    private static final int UDP_PORT = 12001;

    private UDPReceiveThread receiveThread;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the thread, as it is no longer needed
        receiveThread.interrupt();
        wakeLock.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get wakelock
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOG_NAME);
        wakeLock.acquire();

        // Start UDP Receive thread
        receiveThread = new UDPReceiveThread(UDP_PORT);
        receiveThread.start();
        Log.i(LOG_NAME, "Started!");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

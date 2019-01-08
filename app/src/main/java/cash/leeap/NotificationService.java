package cash.leeap;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {
    static int notificationID = 1;
    int mStartMode = START_STICKY;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
    String TAG = "NotificationService";

    @Override
    public void onCreate() {
        // The service is being created
        Log.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        simpleNotify("Start Notification Center", null, null);
        Log.i(TAG, "onStartCommand");
        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed\
        Log.i(TAG, "onDestroy");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void simpleNotify(String title, String content, Class c) {
        NotificationManager notificationManager;
        Notification mNotification;
        PendingIntent mPendingIntent;

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (c == null) {
            c = Main.class;
        }
        Intent intent = new Intent(this, c);
        mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
        Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());

        mBuilder.setAutoCancel(true); //delete after clicked
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);
        mBuilder.setSmallIcon(R.drawable.ic_logo_leeap);
        mBuilder.setContentIntent(mPendingIntent);
        mBuilder.setOngoing(false); // false : slide to delete
//        mBuilder.setActions()
        //API level 16
//        mBuilder.setSubText("This is short description of android app notification");
//        mBuilder.setNumber(150);
        mNotification = mBuilder.build();
        notificationManager.notify(notificationID++, mNotification);

    }

    private void run() {
        try {
            while (true) {

            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

    }
}
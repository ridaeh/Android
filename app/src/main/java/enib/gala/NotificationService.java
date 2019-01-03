package enib.gala;


import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class NotificationService extends IntentService {

    /**
     * A constructor is required, and must call the super <code><a href="/reference/android/app/IntentService.html#IntentService(java.lang.String)">IntentService(String)</a></code>
     * constructor with a name for the worker thread.
     */
    public NotificationService() {
        super("NotificationService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Toast.makeText(getApplicationContext(), "NotificationService start", Toast.LENGTH_LONG).show();
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            Thread.sleep(10000);
            Notify("test", "content", null);
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

//    @TargetApi(Build.VERSION_CODES.O)
//    private void Notify(String title, String content, Class c)
//    {
//        int ONGOING_NOTIFICATION_ID = 1;
//        String CHANNEL_DEFAULT_IMPORTANCE ="1";
//        Intent notificationIntent = new Intent(this, c);
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//
//        Notification notification =
//                new Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
//                        .setContentTitle(title)
//                        .setContentText(content)
//                        .setSmallIcon(R.drawable.ic_logo_leeap)
//                        .setContentIntent(pendingIntent)
//                        .build();
//        ONGOING_NOTIFICATION_ID++;
//        startForeground(ONGOING_NOTIFICATION_ID, notification);
//    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void Notify(String title, String content, Class c) {
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
        mBuilder.build();
        mNotification = mBuilder.getNotification();
        notificationManager.notify(11, mNotification);

    }

}
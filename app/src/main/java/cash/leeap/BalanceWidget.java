package cash.leeap;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class BalanceWidget extends AppWidgetProvider {
    Double balance;
    String balanceText;
    UserAuth mAuth;
    String TAG = "BalanceWidget";
    private Context mContext;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        final AppWidgetManager mAppWidgetManager = appWidgetManager;
        final int mAppWidgetId = appWidgetId;
        mAuth = new UserAuth(mContext);
        User mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            Log.e(TAG, "updateAppWidget: user disconnected");
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.balance_widget);
            views.setTextViewText(R.id.appwidget_text, mContext.getString(R.string.disconnected));

            Intent intent = new Intent(mContext, Main.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

            // Instruct the widget manager to update the widget
            mAppWidgetManager.updateAppWidget(mAppWidgetId, views);
        } else {
            Log.i(TAG, "updateAppWidget: user connected");
            mAuth.getAllInfo(mAuth.getCurrentUser()).getAllInfoListener(new UserAuth.GetAllInfoListener() {
                @Override
                public void GetAllInfoComplete(User u) {
                    if (u != null) {
                        Log.i("getAllInfo getAllInfoListener return", u.toString());
                        balance = u.getBalance();
                        if (balance != null) {
                            balanceText = balance.toString() + "€";
                        } else {
                            balanceText = "0.0€";
                            Log.e(TAG, "GetAllInfoComplete: balance is null");
                        }

                    } else {
                        Log.e(TAG, "GetAllInfoComplete: user is null");
                    }
                    Log.i("onUpdate balanceText", "-> " + balanceText);
                    CharSequence widgetText = balanceText;
                    if (widgetText == null) {
                        widgetText = "null";
                        Log.e(TAG, "updateAppWidget: widgetText null");
                    }
                    // Construct the RemoteViews object
                    RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.balance_widget);
                    views.setTextViewText(R.id.appwidget_text, widgetText);

                    Intent intent = new Intent(mContext, Main.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
                    views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

                    // Instruct the widget manager to update the widget
                    mAppWidgetManager.updateAppWidget(mAppWidgetId, views);
                }
            });
        }


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        mContext = context;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId); //TODO update from other place
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        mContext = context;

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


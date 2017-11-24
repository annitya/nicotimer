package flageolett.nicotimer;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class IntentReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Boolean accepted = intent.getBooleanExtra("accepted", false);
        SharedPreferences preferences = context.getSharedPreferences(State.PREFERENCES, Context.MODE_PRIVATE);

        Integer unit = Integer.parseInt(preferences.getString("unit", "2"));

        if (!accepted)
        {
            unit = 0;
        }

        Integer currentAccepted = Integer.parseInt(preferences.getString("accepted", "0"));
        Integer newValue = unit + currentAccepted;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("accepted", newValue.toString());
        editor.apply();

        cancelNotification(context);
        scheduleNextNotification(context);
    }

    private void cancelNotification(Context context)
    {
        ((NotificationManager)context
            .getSystemService(Context.NOTIFICATION_SERVICE))
            .cancel("NicoTimer", 1);

        Intent closeDrawerIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeDrawerIntent);
    }

    private void scheduleNextNotification(Context context)
    {
        Intent intent = new Intent(context, TimerService.class);
        context.startService(intent);
    }
}

package flageolett.nicotimer;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class IntentReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Boolean accepted = intent.getBooleanExtra("accepted", false);
        State state = State.getInstance(context);

        Integer unit = accepted ? state.getUnit() : 0;
        Integer currentAccepted = state.getAccepted();

        Integer newValue = currentAccepted + unit;
        state.setAccepted(newValue);

        cancelNotification(context);
        scheduleNextNotification(context);
    }

    private void cancelNotification(Context context)
    {
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel("NicoTimer", 1);

        Intent closeDrawerIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeDrawerIntent);
    }

    private void scheduleNextNotification(Context context)
    {
        Intent intent = new Intent(context, TimerService.class);
        context.startService(intent);
    }
}

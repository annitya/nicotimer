package flageolett.nicotimer.Notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import flageolett.nicotimer.Factory;
import flageolett.nicotimer.State.State;

import java.util.Date;

public class IntentReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Boolean accepted = intent.getBooleanExtra("accepted", false);
        State state = Factory
            .getInstance()
            .getState(context);

        Integer unit = accepted ? state.getUnit() : 0;
        Integer currentAccepted = state.getAccepted();

        Integer newValue = currentAccepted + unit;
        state.setAccepted(newValue);

        cancelNotification(context);
        scheduleNextNotification(context);
    }

    static void cancelNotification(Context context)
    {
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel("NicoTimer", 1);

        Intent closeDrawerIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeDrawerIntent);
    }

    private void scheduleNextNotification(Context context)
    {
        NicoTimer timer = Factory
            .getInstance()
            .getNicoTimer(context);

        Long nextDelay = timer.getNextDelay(new Date().getTime());
        timer.scheduleNextPush(nextDelay);
    }
}

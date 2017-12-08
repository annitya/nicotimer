package flageolett.nicotimer.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import flageolett.nicotimer.R;

public class NicoNotification extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        final Resources res = context.getResources();
        final String title = res.getString(R.string.nico_notification_title_template);

        Intent acceptIntent = new Intent(context, IntentReceiver.class).putExtra("accepted", true);
        Intent rejectIntent = new Intent(context, IntentReceiver.class).putExtra("accepted", false);

        PendingIntent pendingAcceptIntent = PendingIntent.getBroadcast(context, 1, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingRejectIntent = PendingIntent.getBroadcast(context, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder builder = new NotificationCompat
            .Builder(context)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_stat_nico)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setPriority(Notification.PRIORITY_MAX)
            .setWhen(0)
            .setAutoCancel(true)
            .setContentIntent(pendingAcceptIntent)
            .setDeleteIntent(pendingAcceptIntent)
            .addAction(R.drawable.ic_action_stat_reply, res.getString(R.string.action_reject), pendingRejectIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        ((NotificationManager)context
            .getSystemService(Context.NOTIFICATION_SERVICE))
            .notify("NicoTimer", 1, builder.build());
    }
}

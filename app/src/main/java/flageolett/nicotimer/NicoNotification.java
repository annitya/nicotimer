package flageolett.nicotimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

class NicoNotification
{
    static void notify(final Context context)
    {
        final Resources res = context.getResources();
        final String title = res.getString(R.string.nico_notification_title_template);
        String nowOrLater = "It's now or later!";

        Intent acceptIntent = new Intent(context, IntentReceiver.class).putExtra("accepted", true);
        Intent rejectIntent = new Intent(context, IntentReceiver.class).putExtra("accepted", false);

        PendingIntent pendingAcceptIntent = PendingIntent.getBroadcast(context, 1, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingRejectIntent = PendingIntent.getBroadcast(context, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder builder = new NotificationCompat
            .Builder(context)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_stat_nico)
            .setContentTitle(title)
            .setContentText(nowOrLater)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setTicker(nowOrLater)
            .addAction(R.drawable.ic_action_stat_share, res.getString(R.string.accept), pendingAcceptIntent)
            .addAction(R.drawable.ic_action_stat_reply, res.getString(R.string.action_reject), pendingRejectIntent)
            .setDeleteIntent(pendingAcceptIntent)
            .setAutoCancel(true);

        notify(context, builder.build());
    }

    private static void notify(final Context context, final Notification notification)
    {
        ((NotificationManager)context
            .getSystemService(Context.NOTIFICATION_SERVICE))
            .notify("NicoTimer", 0, notification);
    }
}

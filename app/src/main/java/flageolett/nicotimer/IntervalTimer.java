package flageolett.nicotimer;

import android.content.Context;

import java.util.TimerTask;

public class IntervalTimer extends TimerTask
{
    private Context context;

    IntervalTimer(Context context)
    {
        this.context = context;
    }

    @Override
    public void run()
    {
        NicoNotification.notify(context);
    }
}

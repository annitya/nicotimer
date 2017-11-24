package flageolett.nicotimer;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class TimerService extends Service
{
    private Timer timer;

    public TimerService() { timer = new Timer(); }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                NicoNotification.notify(getApplicationContext());
            }
        }, 0, 10000);

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        timer.cancel();
    }

    static Boolean isRunning(ActivityManager manager)
    {
        String className = TimerService.class.getName();

        return manager
            .getRunningServices(Integer.MAX_VALUE)
            .stream()
            .filter(s -> s.service.getClassName().equals(className))
            .collect(Collectors.toList())
            .size() > 0;
    }
}

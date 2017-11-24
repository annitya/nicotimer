package flageolett.nicotimer;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import java.util.Date;
import java.util.Timer;
import java.util.stream.Collectors;

public class TimerService extends Service
{
    private Timer timer;
    private Date initialStart;

    public TimerService() { timer = new Timer(); }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Long delay = calculateNextDelay();

        IntervalTimer intervalTimer = new IntervalTimer(getApplicationContext());
        timer.schedule(intervalTimer, delay);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        timer.cancel();
    }

    private Long calculateNextDelay()
    {
        if (initialStart == null)
        {
            initialStart = new Date();
            return 0L;
        }

        SharedPreferences preferences = getSharedPreferences(State.PREFERENCES, Context.MODE_PRIVATE);

        Date now = new Date();
        Long passedTime = now.getTime() - initialStart.getTime();
        Double lengthOfDay = 16.5 * 60 * 60 * 1000;
        Double remainingTime = lengthOfDay - passedTime;

        Integer target = Integer.parseInt(preferences.getString("target", "0"));
        Integer accepted = Integer.parseInt(preferences.getString("accepted", "0"));

        Integer remainingHits = target - accepted;
        Double nextInterval = remainingTime / remainingHits;

        return Math.round(nextInterval);
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

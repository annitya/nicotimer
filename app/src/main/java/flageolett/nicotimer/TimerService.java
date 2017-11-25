package flageolett.nicotimer;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

        if (delay >= 0)
        {
            IntervalTimer intervalTimer = new IntervalTimer(this);
            timer.schedule(intervalTimer, delay);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        timer.cancel();

        State state = State.getInstance(this);

        Integer target = state.getTarget();
        String startDateText = state.getStartDate();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

        Date startDate;
        try
        {
            startDate = dateFormat.parse(startDateText);
        }
        catch (ParseException e)
        {
            return;
        }

        // http://www.nicobloc.com/images/graph5.jpg
        //
        // The rule of thumb is to reduce by 1/5 by end of 1st week
        // Reduce by 1/4 by end of week 2 compared to begining of week 2
        // Reduce by 1/3 by end of week 3 compared to begining of week 3
        // Reduce by 1/2 by end of week 4 compared to begining of week 4
        // Continue reducing by 1/2 in remaining weeks.

        Date now = new Date();
        Long duration = now.getTime() - startDate.getTime();

        Double daysPassed = duration / 24 / 60 / 60 / 1000d;
        Double reductionPercentage = daysPassed / 7 / 5;
        Long newTarget = Math.round(target - (target * reductionPercentage));

        state.setTarget(Math.toIntExact(newTarget));
    }

    private Long calculateNextDelay()
    {
        if (initialStart == null)
        {
            initialStart = new Date();
            return 0L;
        }

        State state = State.getInstance(this);

        Date now = new Date();
        Long passedTime = now.getTime() - initialStart.getTime();
        Double lengthOfDay = 16.5 * 60 * 60 * 1000;
        Double remainingTime = lengthOfDay - passedTime;

        Integer target = state.getTarget();
        Integer accepted = state.getAccepted();

        Integer remainingHits = target - accepted;

        if (remainingHits == 0)
        {
            return -1L;
        }

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

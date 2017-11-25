package flageolett.nicotimer;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
            Long nextHit = intervalTimer.scheduledExecutionTime();
            State
                .getInstance(this)
                .setNextHit(nextHit);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        timer.cancel();

        State state = State.getInstance(this);

        String startDateText = state.getStartDate();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        Date startDate;
        try
        {
            startDate = dateFormat.parse(startDateText);
        }
        catch (ParseException e)
        {
            return;
        }

        Date now = new Date();
        Long duration = now.getTime() - startDate.getTime();
        Integer daysPassed = Math.round(duration / 24 / 60 / 60 / 1000);

        Integer target = state.getTarget();
        Long newTarget = Math.round(calculateNextTarget(daysPassed, target));

        state.setCurrentTarget(newTarget.toString());
        state.setNextHit(0L);
        state.setAccepted(0);
    }

    private Double calculateNextTarget(Integer daysPassed, Integer originalTarget)
    {
        Integer remainderDays = daysPassed % 7;
        Integer numberOfWeeks = Math.round((daysPassed - remainderDays) / 7);

        // Reduce by 1/5 by end of 1st week.
        // Reduce by 1/4 by end of week 2 compared to begining of week 2.
        // Reduce by 1/3 by end of week 3 compared to begining of week 3.
        // Continue reducing by 1/2 in remaining weeks.
        Double[] reductionFactors = {1/5d, 1/4d, 1/3d, 1/2d};

        List<Double> currentFactors = new ArrayList<>();
        Double currentFactor = reductionFactors[0];

        for (Integer currentWeek = 0; currentWeek < numberOfWeeks; currentWeek++)
        {
            currentFactors.add(currentFactor);

            if (currentWeek < reductionFactors.length)
            {
                currentFactor = reductionFactors[currentWeek];
            }
        }

        Double remainderFactor = remainderDays / 7d * currentFactor;
        currentFactors.add(remainderFactor);

        Double newTarget = (double)originalTarget;

        for (Double factor : currentFactors)
        {
            newTarget -= newTarget * factor;
        }

        return newTarget;
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

        Integer currentTarget = Integer.parseInt(state.getCurrentTarget());
        Integer accepted = state.getAccepted();

        Integer remainingHits = currentTarget - accepted;

        if (remainingHits == 0)
        {
            return -1L;
        }

        // Somebody stayed up late, revert to average interval.
        if (remainingTime < lengthOfDay)
        {
            remainingTime = lengthOfDay;
            remainingHits = currentTarget;
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

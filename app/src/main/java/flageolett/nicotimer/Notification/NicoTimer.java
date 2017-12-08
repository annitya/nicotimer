package flageolett.nicotimer.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import flageolett.nicotimer.Factory;
import flageolett.nicotimer.State.State;

import java.util.*;

public class NicoTimer
{
    private PendingIntent currentIntent;
    private State state;
    private AlarmManager alarmManager;
    private Context context;

    public NicoTimer(State state, AlarmManager alarmManager, Context context)
    {
        this.state = state;
        this.alarmManager = alarmManager;
        this.context = context;
    }

    public void start()
    {
        scheduleNextPush(0L);
    }

    public void stop()
    {
        if (currentIntent != null)
        {
            alarmManager.cancel(currentIntent);
            IntentReceiver.cancelNotification(context);

            currentIntent = null;
            return;
        }

        Date now = new Date();
        Long duration = now.getTime() - state.getStartDate();
        Integer daysPassed = Math.round(duration / 24 / 60 / 60 / 1000);

        Integer target = state.getTarget();
        Long newTarget = Math.round(calculateNextTarget(daysPassed, target));

        state.setCurrentTarget(newTarget.toString());
        state.setNextHit(0L);
        state.setAccepted(0);
    }

    public Boolean isRunning()
    {
        return currentIntent != null;
    }

    void scheduleNextPush(Long delay)
    {
        Intent notificationIntent = new Intent(context, NicoNotification.class);
        currentIntent = PendingIntent.getBroadcast(context, 2, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Long nextPushTime = new Date().getTime() + delay;

        // Delay will be zero on initial start. Send notification immediately.
        if (delay == 0)
        {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextPushTime, currentIntent);
        }
        else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextPushTime, currentIntent);
        }

        state.setNextHit(nextPushTime);
    }

    public Long getNextDelay(Long now)
    {
        Long passedTime = now - Factory.getStartOfDay();
        Double lengthOfDay = Factory
            .getInstance()
            .getLengthOfDay();

        Double remainingTime = lengthOfDay - passedTime;

        Integer currentTarget = Integer.parseInt(state.getCurrentTarget());
        Integer accepted = state.getAccepted();

        Integer remainingHits = currentTarget - accepted;

        if (remainingHits == 0)
        {
            return -1L;
        }

        // Somebody stayed up late, revert to average interval.
        if (remainingTime <= 0)
        {
            remainingTime = lengthOfDay;
            remainingHits = currentTarget;
        }

        Double nextInterval = remainingTime / remainingHits;

        return Math.round(nextInterval);
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
}

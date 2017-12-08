package flageolett.nicotimer;

import android.app.AlarmManager;
import android.content.Context;
import flageolett.nicotimer.Notification.NicoTimer;
import flageolett.nicotimer.State.State;

import java.util.Calendar;

public class Factory
{
    private static final String PREFERENCES = "nicotimer";
    private static Factory instance;

    Factory() {}

    public static Factory getInstance()
    {
        if (instance == null) {
            instance = new Factory();
        }

        return instance;
    }

    static void setInstance(Factory instance)
    {
        Factory.instance = instance;
    }

    public static Long getStartOfDay()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 7);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }

    public Double getLengthOfDay()
    {
        return 16d * 60 * 60 * 1000;
    }

    public State getState(Context context)
    {
        return new State(context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE));
    }

    public NicoTimer getNicoTimer(Context context)
    {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        return new NicoTimer(getState(context), alarmManager, context);
    }
}

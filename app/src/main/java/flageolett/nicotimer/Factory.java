package flageolett.nicotimer;

import android.app.AlarmManager;
import android.content.Context;

import java.util.Calendar;

class Factory
{
    private static final String PREFERENCES = "nicotimer";

    private Context context;

    private Factory(Context context)
    {
        this.context = context;
    }

    static Factory getInstance(Context context)
    {
        return new Factory(context);
    }

    static Long getStartOfDay()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 7);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }

    State getState()
    {
        return new State(context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE));
    }

    NicoTimer getNicoTimer()
    {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        return new NicoTimer(getState(), alarmManager, context);
    }
}

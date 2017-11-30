package flageolett.nicotimer;

import android.app.AlarmManager;
import android.content.Context;

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

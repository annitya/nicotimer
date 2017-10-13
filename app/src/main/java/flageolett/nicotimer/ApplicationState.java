package flageolett.nicotimer;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.stream.Collectors;

public class ApplicationState extends Application
{
    private static State state;

    public static State get()
    {
        return state;
    }

    String getStatusText()
    {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final String className = TimerService.class.getName();

        Boolean isRunning = manager
            .getRunningServices(Integer.MAX_VALUE)
            .stream()
            .filter(s -> s.service.getClassName().equals(className))
            .collect(Collectors.toList())
            .size() > 0;

        return isRunning ? "Running" : "Stopped";
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        SharedPreferences preferences = getSharedPreferences("nicotimer", Context.MODE_PRIVATE);
        state = new State(preferences);
    }
}
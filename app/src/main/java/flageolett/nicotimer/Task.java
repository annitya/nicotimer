package flageolett.nicotimer;

import android.util.Log;

import java.util.TimerTask;

public class Task extends TimerTask
{
    @Override
    public void run()
    {
        Log.d("timer", "Totally doing stuff!");
    }
}

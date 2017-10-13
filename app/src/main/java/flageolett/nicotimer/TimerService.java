package flageolett.nicotimer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.util.Timer;

public class TimerService extends Service
{
    private Timer timer;

    public TimerService()
    {
        timer = new Timer();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("timer", "Starting");
        timer.schedule(new Task(), 3000, 3000);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        Log.d("timer", "Destroyed!");
        timer.cancel();
    }
}

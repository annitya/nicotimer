package flageolett.nicotimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{
    private State state = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (state != null)
        {
            return;
        }

        state = new State(this);
        state.updateGui();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        state.persistState();
    }

    public void startTimer(View view)
    {
        Intent intent = new Intent(this, TimerService.class);
        startService(intent);
        state.updateStatus();
    }

    public void stopTimer(View view)
    {
        Intent intent = new Intent(this, TimerService.class);
        stopService(intent);
        state.updateStatus();
    }
}

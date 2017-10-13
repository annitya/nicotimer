package flageolett.nicotimer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateStatus();
    }

    private void updateStatus()
    {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final String className = TimerService.class.getName();

        Boolean isRunning = manager
            .getRunningServices(Integer.MAX_VALUE)
            .stream()
            .filter(s -> s.service.getClassName().equals(className))
            .collect(Collectors.toList())
            .size() > 0;

        String statusText = isRunning ? "Running" : "Stopped";

        TextView textView = (TextView)findViewById(R.id.textView_status_text);
        textView.setText(statusText);
    }

    public void startTimer(View view)
    {
        Intent intent = new Intent(this, TimerService.class);
        startService(intent);
        updateStatus();
    }

    public void stopTimer(View view)
    {
        Intent intent = new Intent(this, TimerService.class);
        stopService(intent);
        updateStatus();
    }
}

package flageolett.nicotimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private EditText target;
    private TextView status;

    private void updateStatus()
    {
        ApplicationState application = (ApplicationState)getApplication();
        String statusText = application.getStatusText();

        status.setText(statusText);
    }

    private void updateTarget()
    {
        String currentTarget = ApplicationState.get().getTarget();
        target.setText(currentTarget);
    }

    private void storeTarget()
    {
        String currentTarget = target.getText().toString();
        ApplicationState.get().setTarget(currentTarget);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        target = (EditText)findViewById(R.id.editText_target);
        status = (TextView)findViewById(R.id.textView_status_text);

        updateStatus();
        updateTarget();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        storeTarget();
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

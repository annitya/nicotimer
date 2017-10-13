package flageolett.nicotimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private TextView status;
    private EditText target;
    private EditText unit;
    private EditText accepted;

    private void updateStatus()
    {
        ApplicationState application = (ApplicationState)getApplication();
        String statusText = application.getStatusText();

        status.setText(statusText);
    }

    private State state()
    {
        return ApplicationState.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = (TextView)findViewById(R.id.textView_status_text);
        target = (EditText)findViewById(R.id.editText_target);
        unit = (EditText)findViewById(R.id.editText_unit);
        accepted = (EditText)findViewById(R.id.editText_accepted);

        updateStatus();
        target.setText(state().getTarget());
        unit.setText(state().getUnit());
        accepted.setText(state().getAccepted());
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        state().setTarget(target.getText().toString());
        state().setUnit(unit.getText().toString());
        state().setAccepted(accepted.getText().toString());
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

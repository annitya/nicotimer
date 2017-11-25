package flageolett.nicotimer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener
{
    private TextView getStatusTextView()
    {
        return (TextView)findViewById(R.id.textView_status_text);
    }

    private TextView getNextHitTextView()
    {
        return (TextView)findViewById(R.id.textView_nextHit_text);
    }

    EditText getTargetEditText()
    {
        return (EditText)findViewById(R.id.editText_target);
    }

    EditText getUnitEditText()
    {
        return (EditText)findViewById(R.id.editText_unit);
    }

    EditText getAcceptedEditText()
    {
        return (EditText)findViewById(R.id.editText_accepted);
    }

    EditText getStartDateEditText()
    {
        return (EditText)findViewById(R.id.editText_startDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Persist GUI-changes to state.
        GuiStateWatcher.registerChangedListeners(this);

        // Reflect state-changes in GUI.
        State.getInstance(this).addChangeListener(this);

        // Load state.
        onSharedPreferenceChanged(null, null);

        // Is the service running?
        updateStatus();
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        State state = State.getInstance(this);

        String target = String.format(Locale.getDefault(),"%d", state.getTarget());
        getTargetEditText().setText(target);

        String unit = String.format(Locale.getDefault(),"%d", state.getUnit());
        getUnitEditText().setText(unit);

        String accepted = String.format(Locale.getDefault(),"%d", state.getAccepted());
        getAcceptedEditText().setText(accepted);

        getStartDateEditText().setText(state.getStartDate());
        getNextHitTextView().setText(state.getNextHit());
    }

    private void updateStatus()
    {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        String statusText = TimerService.isRunning(manager) ? "Running" : "Stopped";
        getStatusTextView().setText(statusText);
    }
}

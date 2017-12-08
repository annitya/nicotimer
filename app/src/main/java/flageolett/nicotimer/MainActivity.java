package flageolett.nicotimer;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import flageolett.nicotimer.Notification.NicoTimer;
import flageolett.nicotimer.State.GuiStateWatcher;
import flageolett.nicotimer.State.State;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener
{
    private NicoTimer timer;

    private TextView getStatusTextView()
    {
        return (TextView)findViewById(R.id.textView_status_text);
    }

    private TextView getNextHitTextView()
    {
        return (TextView)findViewById(R.id.textView_nextHit_text);
    }

    private TextView getCurrentTargetTextView()
    {
        return (TextView) findViewById(R.id.textView_currentTarget_text);
    }

    public EditText getTargetEditText()
    {
        return (EditText)findViewById(R.id.editText_target);
    }

    public EditText getUnitEditText()
    {
        return (EditText)findViewById(R.id.editText_unit);
    }

    public EditText getAcceptedEditText()
    {
        return (EditText)findViewById(R.id.editText_accepted);
    }

    public EditText getStartDateEditText()
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
        Factory
            .getInstance()
            .getState(this)
            .addChangeListener(this);

        // Load state.
        onSharedPreferenceChanged(null, null);

        timer = Factory
            .getInstance()
            .getNicoTimer(this);

        updateStatus();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        getStatusTextView().setText(R.string.main_t_status_stopped);
    }

    public void startTimer(View view)
    {
        timer.start();
        updateStatus();
    }

    public void stopTimer(View view)
    {
        timer.stop();
        updateStatus();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        State state = Factory
            .getInstance()
            .getState(this);

        String target = String.format(Locale.getDefault(),"%d", state.getTarget());
        getTargetEditText().setText(target);

        String unit = String.format(Locale.getDefault(),"%d", state.getUnit());
        getUnitEditText().setText(unit);

        String accepted = String.format(Locale.getDefault(),"%d", state.getAccepted());
        getAcceptedEditText().setText(accepted);

        getStartDateEditText().setText(state.getStartDateString());
        getNextHitTextView().setText(state.getNextHit());
        getCurrentTargetTextView().setText(state.getCurrentTarget());
    }

    private void updateStatus()
    {
        Integer statusTextId = timer.isRunning() ? R.string.main_t_status_running : R.string.main_t_status_stopped;
        getStatusTextView().setText(statusTextId);
    }
}

package flageolett.nicotimer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;

class State implements SharedPreferences.OnSharedPreferenceChangeListener
{
    static final String PREFERENCES = "nicotimer";
    private SharedPreferences preferences;
    private ActivityManager manager;

    private EditText target;
    private EditText unit;
    private EditText accepted;
    private TextView status;

    State(MainActivity mainActivity)
    {
        preferences = mainActivity.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        manager = (ActivityManager)mainActivity.getSystemService(Context.ACTIVITY_SERVICE);

        status = (TextView)mainActivity.findViewById(R.id.textView_status_text);
        target = (EditText)mainActivity.findViewById(R.id.editText_target);
        unit = (EditText)mainActivity.findViewById(R.id.editText_unit);
        accepted = (EditText)mainActivity.findViewById(R.id.editText_accepted);

        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    void updateStatus()
    {
        String statusText = TimerService.isRunning(manager) ? "Running" : "Stopped";
        status.setText(statusText);
    }

    void updateGui()
    {
        target.setText(getTarget());
        unit.setText(getUnit());
        accepted.setText(getAccepted());

        updateStatus();
    }

    void persistState()
    {
        setTarget(target.getText().toString());
        setUnit(unit.getText().toString());
        setAccepted(accepted.getText().toString());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        updateGui();
    }

    private String getTarget()
    {
        return getString("target", "46");
    }

    private void setTarget(String target)
    {
        setString("target", target);
    }

    private String getUnit()
    {
        return getString("unit", "2");
    }

    private void setUnit(String unit)
    {
        setString("unit", unit);
    }

    private String getAccepted()
    {
        return getString("accepted", "0");
    }

    private void setAccepted(String accepted) { setString("accepted", accepted); }

    private String getString(String key, String defaultValue)
    {
        return preferences.getString(key, defaultValue);
    }

    private void setString(String key, String value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}

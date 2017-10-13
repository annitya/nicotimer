package flageolett.nicotimer;

import android.content.SharedPreferences;

class State
{
    private SharedPreferences preferences;

    State(SharedPreferences preferences)
    {
        this.preferences = preferences;
    }

    String getTarget()
    {
        return getString("target", "46");
    }

    void setTarget(String target)
    {
        setString("target", target);
    }

    String getUnit()
    {
        return getString("unit", "2");
    }

    void setUnit(String unit)
    {
        setString("unit", unit);
    }

    String getAccepted()
    {
        return getString("accepted", "0");
    }

    void setAccepted(String accepted)
    {
        setString("accepted", accepted);
    }

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

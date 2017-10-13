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
        return preferences.getString("target", "46");
    }

    void setTarget(String target)
    {
        setString("target", target);
    }

    private void setString(String key, String value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}

package flageolett.nicotimer;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class State
{
    private static final String PREFERENCES = "nicotimer";
    private SharedPreferences preferences;

    static State getInstance(Context context)
    {
        return new State(context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE));
    }

    private State(SharedPreferences preferences)
    {
        this.preferences = preferences;
    }

    void addChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener)
    {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    void clearChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener)
    {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    Integer getTarget()
    {
        return getInteger("target", 1);
    }

    private void setTarget(Integer target)
    {
        setInteger("target", target);
    }

    void setTarget(CharSequence target)
    {
        setTarget(charSequenceToInteger(target, 1));
    }

    String getCurrentTarget()
    {
        return getString("currentTarget", getTarget().toString());
    }

    void setCurrentTarget(String currentTarget)
    {
        setString("currentTarget", currentTarget);
    }

    Integer getUnit()
    {
        return getInteger("unit", 1);
    }

    private void setUnit(Integer unit)
    {
        setInteger("unit", unit);
    }

    void setUnit(CharSequence unit)
    {
        setUnit(charSequenceToInteger(unit, 1));
    }

    Integer getAccepted()
    {
        return getInteger("accepted", 0);
    }

    void setAccepted(Integer accepted)
    {
        setInteger("accepted", accepted);
    }

    void setAccepted(CharSequence accepted)
    {
        setAccepted(charSequenceToInteger(accepted, 0));
    }

    String getStartDate()
    {
        return getString("startDate", "");
    }

    void setStartDate(String startDate)
    {
        setString("startDate", startDate);
    }

    String getNextHit()
    {
        Long nextHit = preferences.getLong("nextHit", 0);

        if (nextHit == 0)
        {
            return "";
        }

        Date nextHitDate = new Date(nextHit);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        return dateFormat.format(nextHitDate);
    }

    void setNextHit(Long nextHit)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("nextHit", nextHit);
        editor.apply();
    }

    private Integer charSequenceToInteger(CharSequence sequence, Integer defaultValue)
    {
        try
        {
            return Integer.parseInt(sequence.toString());
        }
        catch (NumberFormatException e)
        {
            return defaultValue;
        }
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

    private Integer getInteger(String key, Integer defaultValue)
    {
        return preferences.getInt(key, defaultValue);
    }

    private void setInteger(String key, Integer value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}

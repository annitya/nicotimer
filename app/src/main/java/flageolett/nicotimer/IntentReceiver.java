package flageolett.nicotimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class IntentReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Boolean accepted = intent.getBooleanExtra("accepted", false);

        if (!accepted)
        {
            return;
        }

        SharedPreferences preferences = context.getSharedPreferences(State.PREFERENCES, Context.MODE_PRIVATE);

        Integer unit = Integer.parseInt(preferences.getString("unit", "2"));
        Integer currentAccepted = Integer.parseInt(preferences.getString("accepted", "0"));
        Integer newValue = unit + currentAccepted;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("accepted", newValue.toString());
        editor.apply();
    }
}

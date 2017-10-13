package flageolett.nicotimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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

        Integer unit = Integer.parseInt(ApplicationState.get().getUnit());
        Integer currentAccepted = Integer.parseInt(ApplicationState.get().getAccepted());

        Integer newValue = unit + currentAccepted;
        ApplicationState.get().setAccepted(newValue.toString());
    }
}

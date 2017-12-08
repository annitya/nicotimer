package flageolett.nicotimer.State;

import android.text.Editable;
import android.text.TextWatcher;
import flageolett.nicotimer.Factory;
import flageolett.nicotimer.MainActivity;

public class GuiStateWatcher
{
    public static void registerChangedListeners(MainActivity activity)
    {
        State state = Factory
            .getInstance()
            .getState(activity);

        abstract class Watcher implements TextWatcher
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                state.clearChangeListener(activity);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                state.addChangeListener(activity);
            }
        }

        activity
            .getTargetEditText()
            .addTextChangedListener(new Watcher()
            {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    state.setTarget(s);
                }
            });

        activity
            .getUnitEditText()
            .addTextChangedListener(new Watcher()
            {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    state.setUnit(s);
                }
            });

        activity
            .getAcceptedEditText()
            .addTextChangedListener(new Watcher()
            {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    state.setAccepted(s);
                }
            });

        activity
            .getStartDateEditText()
            .addTextChangedListener(new Watcher()
            {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    state.setStartDate(s.toString());
                }
            });
    }
}

package flageolett.nicotimer;

import android.content.Context;
import flageolett.nicotimer.Notification.NicoTimer;
import flageolett.nicotimer.State.State;

public class UnitTestFactory extends Factory
{
    private StateMock state;

    UnitTestFactory() { super(); }

    @Override
    public State getState(Context context)
    {
        if (state == null)
        {
            state = new StateMock(50, 0);
        }

        return state;
    }

    @Override
    public NicoTimer getNicoTimer(Context context)
    {
        return new NicoTimer(getState(context), null, null);
    }
}

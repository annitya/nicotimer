package flageolett.nicotimer;

import flageolett.nicotimer.Notification.NicoTimer;
import flageolett.nicotimer.State.State;
import org.junit.Test;
import static org.junit.Assert.*;

public class NicoTimerTest
{
    @Test
    public void testEvenDelays() throws Exception
    {
        Factory.setInstance(new UnitTestFactory());

        State state = Factory
            .getInstance()
            .getState(null);

        NicoTimer timer = Factory
            .getInstance()
            .getNicoTimer(null);

        Long now = Factory.getStartOfDay();
        Long firstDelay = timer.getNextDelay(now);
        Long nextDelay = firstDelay;

        for (Integer i = 1; i < state.getTarget(); i++)
        {
            state.setAccepted(state.getAccepted() + 1);
            now += nextDelay;
            nextDelay = timer.getNextDelay(now );

            Integer approximateFirst = Math.round(firstDelay / 1000);
            Integer approximateNext = Math.round(nextDelay / 1000);

            assertEquals(approximateFirst, approximateNext);
        }
    }
}

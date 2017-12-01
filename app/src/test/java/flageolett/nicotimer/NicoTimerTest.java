package flageolett.nicotimer;

import org.junit.Test;
import static org.junit.Assert.*;

public class NicoTimerTest
{
    @Test
    public void testEvenDelays() throws Exception
    {
        StateMock state = new StateMock(50, 0);
        NicoTimer timer = new NicoTimer(state, null, null);

        Long now = Factory.getStartOfDay();
        Long firstDelay = timer.getNextDelay(now);
        Long nextDelay = firstDelay;

        for (Integer i = 1; i < state.getTarget(); i++)
        {
            state.acceptOne();
            now += nextDelay;
            nextDelay = timer.getNextDelay(now );

            Integer approximateFirst = Math.round(firstDelay / 1000);
            Integer approximateNext = Math.round(nextDelay / 1000);

            assertEquals(approximateFirst, approximateNext);
        }
    }
}

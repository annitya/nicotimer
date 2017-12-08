package flageolett.nicotimer;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import flageolett.nicotimer.State.State;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DayElapseTest
{
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @UiThreadTest
    @Test
    public void useAppContext() throws Exception
    {
        // Shorten the day.
        Factory.setInstance(new InstrumentationTestFactory());

        MainActivity mainActivity = mainActivityActivityTestRule.getActivity();
        mainActivity
            .findViewById(R.id.button)
            .performClick();

        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();

        State state = Factory
            .getInstance()
            .getState(context);

//        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        device.openNotification();
//        device.wait(Until.hasObject(By.text(NOTIFICATION_TITLE)), TIMEOUT);
//        UiObject2 title = device.findObject(By.text(NOTIFICATION_TITLE));
//        UiObject2 text = device.findObject(By.text(NOTIFICATION_TEXT));
//        assertEquals(NOTIFICATION_TITLE, title.getText());
//        assertEquals(NOTIFICATION_TEXT, text.getText());
//        title.click();
//        device.wait(Until.hasObject(By.text(ESPRESSO.getName())), TIMEOUT);`

        System.out.println("Next hit: " + state.getNextHit());
    }
}

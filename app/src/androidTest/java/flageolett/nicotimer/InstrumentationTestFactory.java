package flageolett.nicotimer;

public class InstrumentationTestFactory extends Factory
{
    @Override
    public Double getLengthOfDay()
    {
        // 4 minutes should provide sufficient resolution.
        return super.getLengthOfDay() / 240;
    }
}

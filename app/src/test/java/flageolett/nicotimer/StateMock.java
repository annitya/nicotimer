package flageolett.nicotimer;

class StateMock extends State
{
    private final Integer target;
    private Integer accepted;

    StateMock(Integer target, Integer accepted)
    {
        super(null);

        this.target = target;
        this.accepted = accepted;
    }

    @Override
    Integer getTarget()
    {
        return target;
    }

    @Override
    String getCurrentTarget()
    {
        return target.toString();
    }

    @Override
    Integer getAccepted()
    {
        return accepted;
    }

    void acceptOne()
    {
        this.accepted++;
    }
}

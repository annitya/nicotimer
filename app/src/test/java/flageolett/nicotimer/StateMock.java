package flageolett.nicotimer;

import flageolett.nicotimer.State.State;

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
    public Integer getTarget()
    {
        return target;
    }

    @Override
    public String getCurrentTarget()
    {
        return target.toString();
    }

    @Override
    public Integer getAccepted()
    {
        return accepted;
    }

    @Override
    public void setAccepted(Integer accepted)
    {
        this.accepted = accepted;
    }
}

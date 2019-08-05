package xyz.erik.api.helpers;

public class ErikTimer {
    private long prevMS;

    public ErikTimer()
    {
        this.prevMS = 0L;
    }

    public boolean delay(float milliSec)
    {
        return (float)(getTime() - this.prevMS) >= milliSec;
    }

    public void reset()
    {
        this.prevMS = getTime();
    }

    public long getTime()
    {
        return System.nanoTime() / 1000000L;
    }

    public long getDifference()
    {
        return getTime() - this.prevMS;
    }
}

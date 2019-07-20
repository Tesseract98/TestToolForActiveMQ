package main;

import com.google.common.primitives.Ints;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayObject implements Delayed {

    private String data;
    private long startTime;

    public DelayObject(String data, long delayInMilliseconds, long systemTime) {
        this.data = data;
        this.startTime = systemTime + delayInMilliseconds;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = startTime - System.nanoTime();
        return unit.convert(diff, TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Ints.saturatedCast(this.startTime - ((DelayObject) o).startTime);
    }

    public String getData() {
        return data;
    }

//    public long getStartTime() {
//        return startTime;
//    }
}
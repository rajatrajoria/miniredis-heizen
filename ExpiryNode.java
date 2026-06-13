package miniredis;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class ExpiryNode implements Delayed {

    private final Object key;
    private final long expiryTime;

    public ExpiryNode(long expiryTime, Object key) {
        this.expiryTime = expiryTime;
        this.key = key;
    }

    public Object getKey() {
        return key;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    @Override
    public long getDelay(TimeUnit unit){
        long delay = expiryTime - System.currentTimeMillis();
        return unit.convert(delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo (Delayed obj){
        if (this.expiryTime < ((ExpiryNode)obj).getExpiryTime()) {
            return -1;
        }
        if (this.expiryTime > ((ExpiryNode)obj).getExpiryTime()) {
            return 1;
        }
        return 0;
    }



}

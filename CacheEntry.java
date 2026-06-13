package miniredis;

public class CacheEntry{

    private final Object value;
    private long expiryTime;

    public CacheEntry (Object value) {
        this.value = value;
        this.expiryTime = -1;
    }

    public CacheEntry (Object value, long expiryTime) {
        this.value = value;
        this.expiryTime = expiryTime;
    }

    public Object getValue() {
        return value;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiry){
        this.expiryTime = expiry;
    }

    public boolean isExpired(){
        return expiryTime!= -1 && System.currentTimeMillis() - this.expiryTime >= 0;
    }
}
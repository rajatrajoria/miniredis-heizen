package miniredis;

public class MiniRedis{
    private final StorageEngine storageEngine;
    private final ExpiryManager expiryManager;

    public MiniRedis(StorageEngine storageEngine, ExpiryManager expiryManager){
        this.storageEngine = storageEngine;
        this.expiryManager = expiryManager;
    }

    public void set(Object key, Object value){
        storageEngine.set(key, value);
    }

    public void setTtl(Object key, long expiryTime){
        expiryTime +=  + System.currentTimeMillis();
        storageEngine.setExpiry(key, expiryTime);
        ExpiryNode expiryNode = new ExpiryNode(expiryTime, key);
        expiryManager.scheduleExpiry(expiryNode);
    }

    public Object get(Object key){
        return storageEngine.get(key);
    } 

    public void delete(Object key){
        storageEngine.delete(key);
    }

    public long getTtl(Object key){
        return storageEngine.getTtl(key);
    }

    public void increment(Object key){
        storageEngine.increment(key);
    }

    public void decrement(Object key){
        storageEngine.decrement(key);
    }

    public void flushAll(){
        storageEngine.flushAll();
    }
}
package miniredis;

import java.util.concurrent.ConcurrentHashMap;

public class StorageEngine {
    private final ConcurrentHashMap<Object, CacheEntry> store = new ConcurrentHashMap<>();

    public void set(Object key, Object value){
        CacheEntry newEntry = new CacheEntry(value);
        store.put(key, newEntry);
    }

    public Object get(Object key){
        CacheEntry entry = store.get(key);
        if(entry == null)
            return null;
        return entry.getValue();
    }

    public void delete(Object key){
        store.remove(key);
    }

    public void remove(Object key, Object value){
        store.remove(key, value);
    }

    // Thread Safe
    public void setExpiry(Object key, long newExpiry){
        store.compute(key, (k, v) -> {
            if(v == null){
                System.out.println("No such key exists");
                return null;
            }
            CacheEntry updatedCacheEntry = new CacheEntry (v.getValue(), newExpiry);
            return updatedCacheEntry;
        });
    }

    public long getTtl(Object key){
        CacheEntry entry = store.get(key);
        if(entry.isExpired()){
            System.out.println("Key is aleady expired");
            return -1;
        }
        return (entry.getExpiryTime() - System.currentTimeMillis()) / 1000;
    }

    public void increment(Object key){
        store.compute(key, (k, v) -> {
            if(v == null){
                System.out.println("No such key exists");
                return null;
            }
            if(v.getValue() instanceof Number){
                Object value = v.getValue();
                long incrementedNumber = (long) value + 1;
                CacheEntry updatedCacheEntry = new CacheEntry (incrementedNumber, v.getExpiryTime());
                return updatedCacheEntry;
            }
            return null;
        });
    }

    public void decrement(Object key){
        store.compute(key, (k, v) -> {
            if(v == null){
                System.out.println("No such key exists");
                return null;
            }
            if(v.getValue() instanceof Number){
                Object value = v.getValue();
                long incrementedNumber = (long) value - 1;
                CacheEntry updatedCacheEntry = new CacheEntry (incrementedNumber, v.getExpiryTime());
                return updatedCacheEntry;
            }
            return null;
        });
    }

    // public void showAllKeys(){
    //     Iterator<Map.Entry<Object, CacheEntry>> i = store.entrySet().iterator();
    //     i = store.entrySet().iterator();
    //     while(i.hasNext()){
    //         ConcurrentHashMap.Entry<Object, CacheEntry>> entry = i.next();
    //         System.out.println("Key = " + entry.getKey()
    //                 + ", Value = "
    //                 + entry.getValue());
    //     }
    // }

    public void flushAll(){
        store.clear();
        System.out.println("All keys deleted");
    }

    public CacheEntry getEntry(Object key){
        return store.get(key); 
    }
}

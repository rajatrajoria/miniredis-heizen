package miniredis;

import java.util.concurrent.DelayQueue;

public class ExpiryManager {
    private final DelayQueue<ExpiryNode> expiryQueue;
    private final StorageEngine storageEngine;

    public ExpiryManager(StorageEngine storageEngine) {
        this.expiryQueue = new DelayQueue<>();
        this.storageEngine = storageEngine;
        Thread t = new Thread(this::processExpiries);
        t.setDaemon(true);
        t.start();
    }

    public void scheduleExpiry(ExpiryNode expiryNode){
        expiryQueue.offer(expiryNode);
        System.out.println("Added the key to the expiry queue for scheduled deletion");

    }

    public void processExpiries(){
        while(!Thread.currentThread().isInterrupted()){
            try {
                ExpiryNode node = expiryQueue.take();
                Object key = node.getKey();
                long olderExpiryTime = node.getExpiryTime();
                CacheEntry latestEntry = storageEngine.getEntry(key);
                if (latestEntry == null) {
                    continue;
                }
                if(latestEntry.getExpiryTime() != olderExpiryTime){
                    continue;
                }
                storageEngine.remove(key, latestEntry);
                System.out.println("Background thread Removed the key");
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

}

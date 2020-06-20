package main.log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StatisticLog {

    private AtomicInteger amountOfFiles;
    private AtomicInteger amountOfFilesForSec;
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();


    public StatisticLog() {
        amountOfFiles = new AtomicInteger(0);
        amountOfFilesForSec = new AtomicInteger(0);
    }

    public void counterIncrement() {
        r.lock();
        try {
            amountOfFilesForSec.getAndAdd(1);
            amountOfFiles.getAndAdd(1);
        } finally {
            r.unlock();
        }
    }

    public Map<String, Integer> getMeans() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        w.lock();
        try {
            hashMap.put("amountOfFiles", amountOfFiles.get());
            hashMap.put("amountOfFilesForSec", amountOfFilesForSec.get());
            return hashMap;
        } finally {
            w.unlock();
        }
    }

    public void setNullAmountOfFilesForSec() {
        r.lock();
        try {
            amountOfFilesForSec.set(0);
        } finally {
            r.unlock();
        }
    }

    // TODO: Добавить синхронизацию возвращающихся значений!

//    public synchronized Map getMeans(){
//        Map<String, Integer> hashMap = new HashMap();
//        hashMap.put("amountOfFiles", amountOfFiles.get());
//        hashMap.put("amountOfFilesForSec", amountOfFilesForSec.get());
//        return hashMap;
//    }
}

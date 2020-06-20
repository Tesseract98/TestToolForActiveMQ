package main;

import main.log.LoggerThread;
import main.log.StatisticLog;
import main.producer.DelayObject;
import main.producer.Producer;
import main.tools.MainLogic;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * main class
 */

public class App {

    private static final Logger log = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        Future<String> future;
        BlockingQueue<DelayObject> queue;
        AtomicLong counterMessage = new AtomicLong();
        StatisticLog statisticLog = new StatisticLog();
        Producer producer = new Producer();
        try {
            producer.setConnection();
            MainLogic mainLogic = new MainLogic();
            mainLogic.mainLogic();
            queue = mainLogic.getQueue();
            counterMessage.set(mainLogic.getCounterMessage());
            service.scheduleAtFixedRate(new LoggerThread(statisticLog), 300, 1000, TimeUnit.MILLISECONDS);
            executorService.submit(new Producer(queue, counterMessage, statisticLog));
            future = (executorService.submit(new Producer(queue, counterMessage, statisticLog)));
            executorService.shutdown();
            System.out.println(future.get());
        } catch (Exception exp) {
            log.error(exp);
            System.exit(-1);
        } finally {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                log.error(e);
            }
            producer.disconnect();
            service.shutdown();
        }
//              thread(new Producer(queue, counterMessage), false);
//              thread(new Producer(queue, counterMessage), false);
//              thread(new Consumer(), false);
//              Thread.sleep(1000);
    }

//    public static void thread(Runnable runnable, boolean daemon) {
//        Thread brokerThread = new Thread(runnable);
//        brokerThread.setDaemon(daemon);
//        brokerThread.start();
//    }
}
package main.log;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LoggerThread implements Runnable {

    private static final Logger log = LogManager.getLogger(LoggerThread.class);
    private StatisticLog statisticLog;

    public LoggerThread(StatisticLog statisticLog) {
        this.statisticLog = statisticLog;
    }

    @Override
    public void run() {
        log.info(statisticLog.getMeans().get("amountOfFiles") + " " + statisticLog.getMeans().get("amountOfFilesForSec"));
        statisticLog.setNullAmountOfFilesForSec();
    }
}
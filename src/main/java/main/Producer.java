package main;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

public class Producer implements Callable<String> {

    private  static final Logger log = LogManager.getLogger(Producer.class);
    private static Session session;
    private static Connection connection;
    private static MessageProducer producer;
    private BlockingQueue<DelayObject> queue;
    private AtomicLong counterMessage;
    private StatisticLog statisticLog;

    public Producer(){

    }

    public Producer(BlockingQueue<DelayObject> queue, AtomicLong counterMessage, StatisticLog statisticLog) {
        this.counterMessage = counterMessage;
        this.queue = queue;
        this.statisticLog = statisticLog;
    }

    public void setConnection(){
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(PropertyReader.getProperty("connection").getProperty("server"));
            // Create a Connection
            connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(PropertyReader.getProperty("connection").getProperty("queueName"));

            // Create a MessageProducer from the Session to the Topic or Queue
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        }
        catch (Exception e){
            log.error("Caught: ", e);
        }
    }

    public void disconnect(){
        // Clean up
        try {
            connection.close();
        }
        catch (Exception exp){
            log.error("connection ", exp);
        }
        try {
            session.close();
        }
        catch (Exception exp){
            log.error("session ", exp);
        }
    }

    public String call(){
        try {
//            service.scheduleAtFixedRate(new LoggerThread(), 13, 1000, TimeUnit.MILLISECONDS);
            // Add the message in queue
//                String text = content + Thread.currentThread().getName() + " : " + this.hashCode();
            while (counterMessage.getAndDecrement() > 0){
                DelayObject delayObject = queue.take();
                TextMessage message = session.createTextMessage(delayObject.getData());
                statisticLog.counterIncrement();
                log.debug(delayObject);
                // Tell the producer to send the message
//                System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
                producer.send(message);
            }
        }
        catch (Exception e) {
            log.error(e);
//            e.printStackTrace();
        }
        return "All right " + statisticLog.getMeans().get("amountOfFiles") + " msg";
    }
}
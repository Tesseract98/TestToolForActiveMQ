package main;

import exceptions.NotAccessNotFoundFileExc;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainLogic {

    private BlockingQueue<DelayObject> queue;
    private long counterMessage;
    private final Logger log = LogManager.getLogger(MainLogic.class);
    private Map<String, ArrayList<String>> mapFiles;

    public MainLogic(){
        this.counterMessage = 0;
        this.mapFiles = new LinkedHashMap<>();
        this.queue = new DelayQueue<>();
    }

    public void mainLogic() {
        try {
            Properties properties = PropertyReader.getProperty("config");
//            FileOutput fileOutput = new FileOutput(); // Работа с файлами
//            for (String var : properties.stringPropertyNames()) {
//                mapFiles.put(var, fileOutput.readFileAndOut(new File(properties.getProperty(var))));
//            }
            ConnectionDB connectionDB = new ConnectionDB(); // работа с БД
            for (String var : properties.stringPropertyNames()) {
                mapFiles.put(var, connectionDB.getArrayDB(var));
            }
            long time = 0;
            String schedule = "";
            for(String temp : mapFiles.get("schedule")){
                schedule += temp + " ";
            }
            Pattern pattern = Pattern.compile("(\\d+)\\D*/(\\d+)\\D*");
            Matcher matcher = pattern.matcher(schedule);
            CompiledTemplate templateOfMessage = TemplateCompiler.compileTemplate(new File("Configuration/MessageTemplate.txt"));
            Mvel mvel = new Mvel(mapFiles);
            Generator generator = new Generator(templateOfMessage, mvel);
            long systemTime = System.nanoTime();
            while (matcher.find()) {
                int mps = Integer.parseInt(matcher.group(1));
//                int seconds = Integer.parseInt(schedule.substring(matcher.start(), matcher.end()));
                int seconds = Integer.parseInt(matcher.group(2));
                int messages = mps * seconds;
                counterMessage += messages;
                for (long i = 0; i < messages; i++) {
                    //Generate some message
                    String content = generator.createMessage();
//                    time += (int) ((double) 1_000_000_000 / mps);
                    time += (int) ((double) TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS) / mps);
                    queue.add(new DelayObject(content, time, systemTime));
                }
            }
        } catch (NotAccessNotFoundFileExc exp) {
            log.error(exp);
            System.exit(-1);
        }
        catch (MyExceptions exp) {
            log.error(exp);
            System.exit(-1);
        }
        catch (Exception exp) {
//            exp.printStackTrace();
            log.error("Error:", exp);
            System.exit(-1);
        }
    }

    public long getCounterMessage() {
        return counterMessage;
    }

    public BlockingQueue<DelayObject> getQueue() {
        return queue;
    }
}
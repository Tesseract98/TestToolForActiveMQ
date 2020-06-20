package main.templater;

import main.exceptions.MyExceptions;
import main.exceptions.NotUniqueExc;
import main.exceptions.NullKey;
import main.exceptions.WrongTemplateExc;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateRuntime;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Mvel {

    private Map<String, ArrayList<String>> mapFiles;
    private List<String> fileValueUnique;

    public String getContent(CompiledTemplate templateOfMessage) throws MyExceptions {
        fileValueUnique = new LinkedList<>();
        try {
            return (TemplateRuntime.execute(templateOfMessage, this)).toString();
        } catch (Exception exceptions) {
            //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
            throw new MyExceptions("Произошла ошибка в " + exceptions.getMessage(), exceptions.getCause().getCause());
        }
    }

    @SuppressWarnings("unused")
    public int number(int from, int till) {
        return from + (int) (Math.random() * till);
    }

    @SuppressWarnings("unused")
    public String decimal(int from, int till, int afterComma) {
        return String.format("%." + afterComma + "f", from + Math.random() * till);
    }

    @SuppressWarnings("unused")
    public String id(int idLength) {
        String string = "";
        for (int i = 0; i < idLength; i++) {
            Random r = new Random();
            char letter;
            if (r.nextBoolean()) {
                letter = (char) (r.nextInt(26) + 'a');
            } else if (r.nextBoolean()) {
                letter = (char) (48 + r.nextInt(10));
            } else {
                letter = (char) (r.nextInt(26) + 'A');
            }
            string += letter;
        }
        return string;
    }

    @SuppressWarnings("unused")
    public String dateTime(String template) throws WrongTemplateExc {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow;
        try {
            formatForDateNow = new SimpleDateFormat(template);
            return formatForDateNow.format(dateNow);
        } catch (Exception e) {
            throw new WrongTemplateExc("Неправильный шаблон даты " + template);
        }
    }

    @SuppressWarnings("unused")
    public String dateTime(String template, int shift) throws WrongTemplateExc {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter;
        try {
            formatter = DateTimeFormatter.ofPattern(template);
        } catch (Exception e) {
            throw new WrongTemplateExc("Неправильный шаблон даты " + template);
        }
        DayOfWeek dayOfWeek;
        for (int i = 0; i < shift; i++) {
            date = date.plusDays(1);
            dayOfWeek = date.getDayOfWeek();
            switch (dayOfWeek) {
                case SATURDAY:
                    date = date.plusDays(2);
                    break;
                case SUNDAY:
                    date = date.plusDays(1);
                    break;
            }
        }
        return date.format(formatter);
    }

    @SuppressWarnings("unused")
    public String fileValue(String nameOfFile) throws NullKey {
        ArrayList<String> fileOutput = checkFile(nameOfFile);
        Random r = new Random();
        int digit = r.nextInt(fileOutput.size());
        String randomValue = fileOutput.get(digit);
        fileValueUnique.add(randomValue);
        return randomValue;
    }

    @SuppressWarnings("unused")
    public String uniqueFileValue(String nameOfFile) throws NotUniqueExc, NullKey {
        ArrayList<String> fileOutput = checkFile(nameOfFile);
        Random r = new Random();
        String randomValue = fileOutput.get(r.nextInt(fileOutput.size()));
        Set<Integer> checkRandom = new HashSet<>();
        while (fileValueUnique.contains(randomValue) && checkRandom.size() < fileOutput.size()) {
            int rnd = r.nextInt(fileOutput.size());
            checkRandom.add(rnd);
            randomValue = fileOutput.get(rnd);
            if (checkRandom.size() == fileOutput.size() && fileValueUnique.contains(randomValue)) {
                throw new NotUniqueExc("Not enough unique means!");
            }
        }
        fileValueUnique.add(randomValue);
        return randomValue;
    }

    private ArrayList<String> checkFile(String nameOfFile) throws NullKey {
        ArrayList<String> fileOutput;
        if (!mapFiles.containsKey(nameOfFile)) {
            throw new NullKey("Нет ключа " + nameOfFile + " в config.properties");
        }
        fileOutput = mapFiles.get(nameOfFile);
        if (fileOutput.size() == 0) {
            throw new NullKey("Файл " + nameOfFile + " не соответствует норме");
        }
        return fileOutput;
    }

    public Mvel(Map<String, ArrayList<String>> mapFiles) {
        this.mapFiles = mapFiles;
    }

}

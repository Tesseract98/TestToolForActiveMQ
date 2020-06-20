package main.tools;

import main.exceptions.NotAccessNotFoundFileExc;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyReader {
    private PropertyReader() {
    }

    static public Properties readPropertyFile(String propertyName) throws NotAccessNotFoundFileExc {
        FileInputStream fis;
        Properties property = new Properties();
        String configDirectory = String.format("src/main/resources/%s.properties", propertyName);
        try {
            fis = new FileInputStream(configDirectory);
            property.load(fis);
        } catch (Exception e) {
            // TODO: Добавить ошибку доступа
            throw new NotAccessNotFoundFileExc("ОШИБКА: К файлу свойств " + configDirectory + " не может быть получен доступ!", e);
        }
        return property;
    }
}
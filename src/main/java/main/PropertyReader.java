package main;

import exceptions.NotAccessNotFoundFileExc;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyReader {
    private PropertyReader(){}

    static Properties getProperty(String propertyName) throws NotAccessNotFoundFileExc {
        FileInputStream fis;
        Properties property = new Properties();
        String configDirectory = propertyName + ".properties";
        try {
            fis = new FileInputStream(configDirectory);
            property.load(fis);
        } catch (Exception e) {
            throw new NotAccessNotFoundFileExc("ОШИБКА: К файлу свойств " + configDirectory + " не может быть получен доступ!", e); // Добавить ошибку доступа
        }
        return property;
    }
}
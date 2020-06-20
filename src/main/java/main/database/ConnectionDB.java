package main.database;

import main.tools.PropertyReader;
import main.exceptions.NotAccessNotFoundFileExc;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

public class ConnectionDB {

    //    private static final String DB_URL = "jdbc:h2:tcp://localhost/~/test"; // подключение к серверу
    private static final Logger log = LogManager.getLogger(ConnectionDB.class);

    public ConnectionDB() {
    }

    public ArrayList<String> getArrayDB(String tableName) throws NotAccessNotFoundFileExc {
        final String DB_URL = PropertyReader.readPropertyFile("connection").getProperty("dbPath"); // подключение к локалке
        final String DB_Driver = "org.h2.Driver";
        ArrayList<String> fileOutput = new ArrayList<>();
        //соединениесБД
        try (Connection connection = DriverManager.getConnection(DB_URL, "admin", "123456")) {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + tableName);
            while (rs.next()) {
//                System.out.println("id " + rs.getInt("id") + " name " + rs.getString("name"));
                fileOutput.add(rs.getString("name"));
            }
            stmt.close();
        } catch (ClassNotFoundException e) {
            log.error(e);
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            log.error(e);
            System.out.println("Ошибка SQL !");
        }
        return fileOutput;
    }

}

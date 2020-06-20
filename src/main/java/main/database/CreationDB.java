package main.database;

import main.tools.PropertyReader;
import main.exceptions.NotAccessNotFoundFileExc;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreationDB {

    private static final Logger log = LogManager.getLogger(CreationDB.class);

    public static void main(String[] args) throws NotAccessNotFoundFileExc {
        final String DB_URL = PropertyReader.readPropertyFile("connection").getProperty("dbPath"); //Работа с локальной БД
        final String DB_Driver = "org.h2.Driver";
        try {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            Connection connection = DriverManager.getConnection(DB_URL, "admin", "123456"); //соединениесБД
            Statement stmt = connection.createStatement();
            stmt.execute("create table if not exists schedule(id int primary key auto_increment, name varchar(100) not null, unique(name))");
            stmt.execute("create table if not exists currencies(id int primary key auto_increment, name varchar(100) not null, unique(name))");
            stmt.execute("create table if not exists firms(id int primary key auto_increment, name varchar(100) not null, unique(name))");
            stmt.execute("create table if not exists instruments(id int primary key auto_increment, name varchar(100) not null, unique(name))");
            stmt.execute("create table if not exists tradeSources(id int primary key auto_increment, name varchar(100) not null, unique(name))");
            stmt.execute("create table if not exists tradingVenues(id int primary key auto_increment, name varchar(100) not null, unique(name))");
            stmt.execute("insert into schedule (name) values('100/10'), ('200/10'), ('300/20')");
            stmt.execute("insert into currencies (name) values('USD'), ('EUR'), ('AOA'), ('XCD'), ('AFN'), ('RUB')");
            stmt.execute("insert into firms (name) values('PYTEROCHKA1'), ('MAC123'), ('321PEK')");
            stmt.execute("insert into instruments (name) values('OV03054473OV'),('DMI00T78R55Y'), ('54P3YT343512')");
            stmt.execute("insert into tradeSources (name) values('stock'),('futures'), ('option')");
            stmt.execute("insert into tradingVenues (name) values('CSOM'),('IART'), ('ETBA')");
            connection.close();
            stmt.close();
        } catch (ClassNotFoundException | SQLException e) {
            log.error(e);
//            System.out.println("JDBC драйвер для СУБД не найден!");
        } //            System.out.println("Ошибка SQL !");

    }

}

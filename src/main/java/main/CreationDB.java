package main;

import exceptions.NotAccessNotFoundFileExc;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreationDB {

    private  static final Logger log = LogManager.getLogger(CreationDB.class);

    public static void main(String[] args) throws NotAccessNotFoundFileExc {
        final String DB_URL = PropertyReader.getProperty("connection").getProperty("dbPath"); //Работа с локальной БД
        final String DB_Driver = "org.h2.Driver";
        try {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            Connection connection = DriverManager.getConnection(DB_URL,"admin","123456"); //соединениесБД
            Statement stmt = connection.createStatement();
//            stmt.execute("drop table user");
            stmt.execute("create table schedule(id int primary key auto_increment, name varchar(100) not null)");
            stmt.execute("create table currencies(id int primary key auto_increment, name varchar(100) not null)");
            stmt.execute("create table firms(id int primary key auto_increment, name varchar(100) not null)");
            stmt.execute("create table instruments(id int primary key auto_increment, name varchar(100) not null)");
            stmt.execute("create table tradeSources(id int primary key auto_increment, name varchar(100) not null)");
            stmt.execute("create table tradingVenues(id int primary key auto_increment, name varchar(100) not null)");
            stmt.execute("insert into schedule values(1, '100/10'),(null, '200/10'), (null, '300/20')");
            stmt.execute("insert into currencies values(1, 'USD'),(null, 'EUR'), (null, 'AOA'), (null, 'XCD'), (null, 'AFN'), (null, 'RUB')");
            stmt.execute("insert into firms values(1, 'PYTEROCHKA1'),(null, 'MAC123'), (null, '321PEK')");
            stmt.execute("insert into instruments values(1, 'OV03054473OV'),(null, 'DMI00T78R55Y'), (null, '54P3YT343512')");
            stmt.execute("insert into tradeSources values(1, 'stock'),(null, 'futures'), (null, 'option')");
            stmt.execute("insert into tradingVenues values(1, 'CSOM'),(null, 'IART'), (null, 'ETBA')");
            connection.close();
            stmt.close();
        } catch (ClassNotFoundException | SQLException e) {
            log.error(e);
//            System.out.println("JDBC драйвер для СУБД не найден!");
        } //            System.out.println("Ошибка SQL !");

    }
}

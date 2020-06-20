package main.database;

import org.h2.tools.DeleteDbFiles;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;

public class CreationDBTest {
    @Test
    public void checkConnection() throws SQLException, ClassNotFoundException {
        final String DB_DIR = "./db/";
        final String DB_NAME = "test";
        final String DB_URL = "jdbc:h2:";
        final String DB_Driver = "org.h2.Driver";
        Class.forName(DB_Driver);
        Connection connection = DriverManager.getConnection(DB_URL + DB_DIR + DB_NAME, "admin", "123456");

        Statement stmt = connection.createStatement();
        stmt.execute("drop table if exists user");
        stmt.execute("create table user(id int primary key, name varchar(100))");
        stmt.execute("insert into user values(1, 'hello')");
        stmt.execute("insert into user values(2, 'world')");

        ResultSet rs = stmt.executeQuery("select name from user where id=1");
        rs.next();
        assertEquals("hello", rs.getString(1));

        rs = stmt.executeQuery("select name from user where id=2");
        rs.next();
        assertEquals("world", rs.getString(1));

        connection.close();
        stmt.close();

        DeleteDbFiles.execute(DB_DIR, DB_NAME, true);
    }

}

package com.johnsonautoparts;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractTestProject {
    protected static Connection connectionMock;
    protected static HttpServletResponse responseMock;
    protected static HttpServletRequest requestMock;

    @BeforeAll
    static void setup() {
        HttpSession sessionMock = Mockito.mock(HttpSession.class);
        HttpServletResponse responseMock = Mockito.mock(HttpServletResponse.class);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        connectionMock = Mockito.mock(Connection.class);
        Mockito.when(requestMock.getSession()).thenReturn(sessionMock);

        /*
        //create the Derby DB connection
        try {
            // define the JDBC driver class
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver")
                    .getDeclaredConstructor().newInstance();

            // build the connection string to the file in the webapp
            StringBuilder jdbcStrBuff = new StringBuilder();
            jdbcStrBuff.append("jdbc:derby:");
            jdbcStrBuff.append("src");
            jdbcStrBuff.append(File.separator + "main");
            jdbcStrBuff.append(File.separator + "webapp");
            jdbcStrBuff.append(File.separator + "db");

            System.out.println("Initializing Derby DB at path: " + jdbcStrBuff.toString());
            // create a connection to the db
            connection = DriverManager.getConnection(jdbcStrBuff.toString());
            System.out.println(" [+] Successfully opened connect to Derby DB");
        }
        catch(Exception e) {
            System.out.println(" [-] Failed to create connect to Derby DB: " + e.getMessage());
        }
        */
    }

/*
    @AfterAll
    static void cleanAll() {
        if (connection != null) {
            try {
                System.out.println(" [+] Closing Derby DB connection");
                connection.close();
                System.out.println(" [+] Derby DB closed successfully");
            } catch (SQLException throwable) {
                System.err.println(" [-] Failed to cleanup the database with close()");
            }
        }
    }
 */
}

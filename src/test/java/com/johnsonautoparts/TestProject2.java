package com.johnsonautoparts;

import com.johnsonautoparts.db.DB;
import com.johnsonautoparts.exception.AppException;
import com.johnsonautoparts.exception.DBException;
import org.junit.jupiter.api.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Connection;

@DisplayName("Test your Project2 fixes")
class TestProject2 {
    private static Project2 project2;
    private static Connection connection;

    @BeforeEach
    void beforeEach() {
        HttpSession sessionMock = Mockito.mock(HttpSession.class);
        HttpServletResponse responseMock = Mockito.mock(HttpServletResponse.class);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        ServletContext contextMock = Mockito.mock(ServletContext.class);

        //connectionMock = Mockito.mock(Connection.class);
        Mockito.when(requestMock.getSession()).thenReturn(sessionMock);
        Mockito.when(sessionMock.getServletContext()).thenReturn(null);
        //Mockito.when(requestMock.getContextPath()).thenReturn("src/main/webapp");
        //Mockito.when(contextMock.getContextPath()).thenReturn("src/main/webapp");

        try {
            connection = DB.getDbConnection(sessionMock);
        } catch (DBException dbe) {
            dbe.printStackTrace();
        }

        //create an instance of the Project3 class
        project2 = new Project2(connection, requestMock, responseMock);
    }


    //Project 2, Milestone 1
    @Test
    @DisplayName("Project 2, Milestone 1, Task 1")
    void TestMilestone2Task1() {
        System.out.println("Testing Project2 dbinventory()");

        //test sending special chars to dbInventory which simulates a
        //SQL injection attack. The idea is not filter these specific
        //characters but all SQL injection possibilities
        try {
            int dbReturn = project2.dbInventory("';-");
            assertTrue(true);
        }
        catch(AppException ae) {
            //Fail assertion since Exception was thrown
            assertTrue(false);
        }

    }

    @Test
    @DisplayName("Project 2, Milestone 1, Task 2")
    void TestMilestone2Task2() {
        System.out.println("Testing Project2 dbinventory()");

        //test sending special chars to dbInventory which simulates a
        //SQL injection attack. The idea is not filter these specific
        //characters but all SQL injection possibilities
        try {
            int dbReturn = project2.dbTasks("';-");
            assertTrue(true);
        }
        catch(AppException ae) {
            //Fail assertion since Exception was thrown
            assertTrue(false);
        }
    }

    @Test
    @DisplayName("Project 2, Milestone 1, Task 3")
    void TestMilestone2Task3() {
        System.out.println("Testing Project2 ()");
    }

    @Test
    @DisplayName("Project 2, Milestone 1, Task 4")
    void TestMilestone2Task4() {
        System.out.println("No Unit test for Project2, Milestone1, Task4");
    }

    @Test
    @DisplayName("Project 2, Milestone 1, Task 5")
    void TestMilestone2Task5() {
        System.out.println("No Unit test for Project2, Milestone1, Task5");
    }

    @Test
    @DisplayName("Project 2, Milestone 1, Task 6")
    void TestMilestone2Task6() {
        System.out.println("No Unit test for Project2, Milestone1, Task6");
    }

    @Test
    @DisplayName("Project 2, Milestone 1, Task 7")
    void TestMilestone2Task7() {
        System.out.println("No Unit test for Project2, Milestone1, Task7");
    }

    @Test
    @DisplayName("Project 2, Milestone 3")
    void TestMilestone3() {
        System.out.println("No Unit test for Project2, Milestone2");
    }
}

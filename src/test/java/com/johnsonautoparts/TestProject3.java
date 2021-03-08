package com.johnsonautoparts;

import org.junit.jupiter.api.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;

//@DisplayName("Test your Project3 fixes")
class TestProject3 {
    Project3 project3;
    Connection connectionMock;

    @BeforeEach
    void beforeEach() {
        HttpSession sessionMock = Mockito.mock(HttpSession.class);
        HttpServletResponse responseMock = Mockito.mock(HttpServletResponse.class);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        connectionMock = Mockito.mock(Connection.class);
        Mockito.when(requestMock.getSession()).thenReturn(sessionMock);

        //create an instance of the Project3 class
        project3 = new Project3(connectionMock, requestMock, responseMock);
    }

    @Test
    @DisplayName("Project 3, Milestone 1, Task 1")
    void TestMilestone1Task1() {
        System.out.println("Testing Project 3, Milestone 1, Task 1");
        System.out.println("No test case for Task 1 yet");
    }


    @Test
    @DisplayName("Project 3, Milestone 1, Task 2")
    void TestMilestone2Task2() {
        System.out.println("Testing Project 3, Milestone 1, Task 2");
        String fileName = "no_such_file";

        /*
         * call the dataExposure() method with a false filename
         * if it is working correctly, then it will throw and error
         * if the problem is not fixed, it will return text
         */
        assertThrows(Exception.class, () -> project3.dataExposure(fileName) );
    }


    @Test
    @DisplayName("Project 3, Milestone 1, Task 3")
    void TestMilestone1Task3() {
        System.out.println("Testing Project 3, Milestone 1, Task 3");

        /*
         * call the exceptionLoggin() method with any data
         * if it is working correctly, then it will throw an error
         * because state cannot be pulled from the HttpSession
         */
        assertThrows(Exception.class, () -> project3.exceptionLogging("test") );
    }

}

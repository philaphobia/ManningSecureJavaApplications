package com.johnsonautoparts;

import com.johnsonautoparts.exception.AppException;
import org.junit.jupiter.api.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;

//@DisplayName("Test your Project3 fixes")
class TestProject3 {
    Project3 project3;
    Connection connectionMock;
    HttpSession sessionMock;
    HttpServletRequest requestMock;
    HttpServletResponse responseMock;

    @BeforeEach
    void beforeEach() {
        sessionMock = Mockito.mock(HttpSession.class);
        responseMock = Mockito.mock(HttpServletResponse.class);
        requestMock = Mockito.mock(HttpServletRequest.class);
        connectionMock = Mockito.mock(Connection.class);
        Mockito.when(requestMock.getSession()).thenReturn(sessionMock);

        //create an instance of the Project3 class
        project3 = new Project3(connectionMock, requestMock, responseMock);
    }

    @Test
    @DisplayName("Project 3, Milestone 1, Task 1")
    void TestMilestone1Task1() throws Exception {
        System.out.println("No Unit test for Milestone1, Task1");
    }


    @Test
    @DisplayName("Project 3, Milestone 1, Task 2")
    void TestMilestone1Task2() {
        System.out.println("Testing Project3 dataExposure()");
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
        System.out.println("Testing Project3 exceptionLogging()");

        /*
         * call the exceptionLogging() method with any data
         * if it is working correctly, then it will throw an error
         * because state cannot be pulled from the HttpSession
         */
        assertThrows(Exception.class, () -> project3.exceptionLogging("test") );
    }

    @Test
    @DisplayName("Project 3, Milestone 2, Task 1")
    void TestMilestone2Task1() {
        System.out.println("No Unit test for Milestone 2, Task 1");
    }

    @Test
    @DisplayName("Project 3, Milestone 2, Task 2")
    void TestMilestone2Task2() {
        System.out.println("No Unit test for Milestone 2, Task 2");
    }

    @Test
    @DisplayName("Project 3, Milestone 2, Task 3")
    void TestMilestone2Task3() {
        System.out.println("Testing Project3 runtimeException()");

        /*
         * call the runtimeException() with special characters
         * and verify that if an exception is thrown it is
         * not RuntimeException
         */
        try {
            project3.runtimeException("_");
            assertTrue(true);
        } catch(Exception e) {
            assertTrue(e instanceof AppException);
        }
    }

    @Test
    @DisplayName("Project 3, Milestone 2, Task 4")
    void TestMilestone2Task4() {
        System.out.println("Testing Project3 testNull()");

        /*
         * call the testNull() method with null value
         * and verify NullPointerException is not thrown
         */
        try {
            project3.testNull(null);
        } catch (NullPointerException npe) {
            //fail test if NPE thrown
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("Project 3, Milestone 3, Task 1")
    void TestMilestone3Task1() {
        System.out.println("No Unit test for Milestone 3, Task 1");
    }

    @Test
    @DisplayName("Project 3, Milestone 3, Task 2")
    void TestMilestone3Task2() {
        System.out.println("Testing Project3 manipulateString()");

        /*
         * call the manipulateString() method with null value
         * and verify NullPointerException is not thrown
         */
        try {
            project3.manipulateString(null);
        } catch (NullPointerException npe) {
            //fail test if NPE thrown
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("Project 3, Milestone 3, Task 3")
    void TestMilestone3Task3() {
        System.out.println("No Unit test for Milestone 3, Task 3");
    }

    @Test
    @DisplayName("Project 3, Milestone 3, Task 4")
    void TestMilestone3Task4() {
        System.out.println("No Unit test for Milestone 3, Task 4");
    }
}

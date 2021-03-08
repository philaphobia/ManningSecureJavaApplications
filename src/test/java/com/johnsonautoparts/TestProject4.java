package com.johnsonautoparts;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import java.sql.Connection;

import static org.mockito.Mockito.*;

@DisplayName("Test your Project4 fixes")
class TestProject4 {
    private static Project4 project4;
    private static Connection connectionMock;

    @BeforeEach
    void beforeEach() {
        HttpSession sessionMock = Mockito.mock(HttpSession.class);
        HttpServletResponse responseMock = Mockito.mock(HttpServletResponse.class);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        connectionMock = Mockito.mock(Connection.class);
        Mockito.when(requestMock.getSession()).thenReturn(sessionMock);

        //create an instance of the Project3 class
        project4 = new Project4(connectionMock, requestMock, responseMock);
    }

    @AfterEach
    void tearDown() {
        //reset stderr since some tests redirect
        System.setErr(System.err);
    }

    @Test
    @DisplayName("Project 4, Milestone 1, Task 1")
    void TestMilestone1Task1() {
        System.out.println("No test cases for Project4 yet");
    }

}

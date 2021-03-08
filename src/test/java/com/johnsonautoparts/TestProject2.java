package com.johnsonautoparts;

import org.junit.jupiter.api.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Connection;

@DisplayName("Test your Project2 fixes")
class TestProject2 {
    private static Project2 project2;
    private static Connection connectionMock;

    @BeforeEach
    void beforeEach() {
        HttpSession sessionMock = Mockito.mock(HttpSession.class);
        HttpServletResponse responseMock = Mockito.mock(HttpServletResponse.class);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        connectionMock = Mockito.mock(Connection.class);
        Mockito.when(requestMock.getSession()).thenReturn(sessionMock);

        //create an instance of the Project3 class
        project2 = new Project2(connectionMock, requestMock, responseMock);
    }

    @Test
    @DisplayName("Project 2, Milestone 1, Task 1")
    void TestMilestone1Task1() {
        System.out.println("No test cases for Project2 yet");
    }

}

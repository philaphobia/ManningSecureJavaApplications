package com.johnsonautoparts;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import java.sql.Connection;

import static org.mockito.Mockito.*;

@DisplayName("Test your Project1 fixes")
class TestProject1 extends AbstractTestProject {
    private static Project1 project1;
    private static Connection connectionMock;

    @BeforeEach
    void beforeEach() {
        HttpSession sessionMock = Mockito.mock(HttpSession.class);
        HttpServletResponse responseMock = Mockito.mock(HttpServletResponse.class);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        connectionMock = Mockito.mock(Connection.class);
        Mockito.when(requestMock.getSession()).thenReturn(sessionMock);

        //create an instance of the Project3 class
        project1 = new Project1(connectionMock, requestMock, responseMock);
    }

    @Test
    @DisplayName("Project 1, Milestone 1, Task 1")
    void TestMilestone1Task1() {
        System.out.println("No test cases for Project1 yet");
    }

}

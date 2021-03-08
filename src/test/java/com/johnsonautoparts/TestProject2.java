package com.johnsonautoparts;

import org.junit.jupiter.api.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import static org.mockito.Mockito.*;
import org.mockito.Mock;

@DisplayName("Test your Project2 fixes")
class TestProject2 extends AbstractTestProject {
    private static Project2 project2;

    @BeforeEach
    void beforeEach() {
        //
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        //recreate an instance of the Project2 class
        project2 = new Project2(connection, request, response);
    }

    @AfterEach
    void tearDown() {
        //reset stderr since some tests redirect
        System.setErr(System.err);
    }

    @Test
    @DisplayName("Project 2, Milestone 1, Task 1")
    void TestMilestone1Task1() {
        System.out.println("No test for Project2 yet");
    }

}

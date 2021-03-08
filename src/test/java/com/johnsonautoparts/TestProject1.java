package com.johnsonautoparts;

import org.junit.jupiter.api.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import static org.mockito.Mockito.*;

@DisplayName("Test your Project1 fixes")
class TestProject1 extends AbstractTestProject {
    private static Project1 project1;

    @BeforeEach
    void beforeEach() {
        //
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        //recreate an instance of the Project1 class
        project1 = new Project1(connection, request, response);
    }

    @AfterEach
    void tearDown() {
        //reset stderr since some tests redirect
        System.setErr(System.err);
    }

    @Test
    @DisplayName("Project 1, Milestone 1, Task 1")
    void TestMilestone1Task1() {
        System.out.println("No test for Project1 yet");
    }

}

package com.johnsonautoparts;

import org.junit.jupiter.api.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import static org.mockito.Mockito.*;

@DisplayName("Test your Project4 fixes")
class TestProject4 extends AbstractTestProject {
    private static Project4 project4;

    @BeforeEach
    void beforeEach() {
        //
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        //recreate an instance of the Project4 class
        project4 = new Project4(connection, request, response);
    }

    @AfterEach
    void tearDown() {
        //reset stderr since some tests redirect
        System.setErr(System.err);
    }

    @Test
    @DisplayName("Project 4, Milestone 1, Task 1")
    void TestMilestone1Task1() {
        System.out.println("No test for Project1 yet");
    }

}

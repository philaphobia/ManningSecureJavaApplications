package com.johnsonautoparts;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;

@DisplayName("Test your Project3 fixes")
class TestProject3 extends AbstractTestProject {
    private static Project3 project3;

    @BeforeEach
    void beforeEach() {
        //
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        //recreate an instance of the Project3 class
        project3 = new Project3(connection, request, response);
    }

    @AfterEach
    void tearDown() {
        //reset stderr since some tests redirect
        System.setErr(System.err);
    }


    @Test
    @DisplayName("Project 3, Milestone 1, Task 1")
    void TestMilestone1Task1() {
        final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.out.println("Testing Project 3, Milestone 1, Task 1");
        System.out.println(" - No tests for this task\n");

        /*
        System.setErr( new PrintStream( errContent ) );

        try {
            project3.suppressException("");
            System.out.println("Nothing happened");
        }
        catch(AppException e) {
            assertNull(errContent);
        }
        catch(NullPointerException npe) {
            assertNull(errContent);
        }
        */
    }

    @Test
    @DisplayName("Project 3, Milestone 1, Task 2")
    void TestMilestone1Task2() {
        System.out.println("Testing Project 3, Milestone 1, Task 2");

        String fileName = "filename_does_not_exist";

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
         * call the dataExposure() method with a false filename
         * if it is working correctly, then it will throw and error
         * if the problem is not fixed, it will return text
         */
        assertThrows(Exception.class, () -> project3.exceptionLogging("") );
    }

}

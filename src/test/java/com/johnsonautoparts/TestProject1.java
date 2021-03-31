package com.johnsonautoparts;

import com.johnsonautoparts.exception.AppException;
import com.johnsonautoparts.logger.AppLogger;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import java.sql.Connection;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@DisplayName("Test your Project1 fixes")
class TestProject1 extends AbstractTestProject {
    private static Project1 project1;
    private static Connection connectionMock;
    private static HttpSession sessionMock;
    private static HttpServletResponse responseMock;
    private static HttpServletRequest requestMock;

    @BeforeEach
    void beforeEach() {
        sessionMock = Mockito.mock(HttpSession.class);
        responseMock = Mockito.mock(HttpServletResponse.class);
        requestMock = Mockito.mock(HttpServletRequest.class);
        connectionMock = Mockito.mock(Connection.class);
        Mockito.when(requestMock.getSession()).thenReturn(sessionMock);

        //create an instance of the Project3 class
        project1 = new Project1(connectionMock, requestMock, responseMock);
    }


    //Project 1, Milestone 1
    @Test
    @DisplayName("Project 1, Milestone 1, Task 1")
    void TestMilestone1Task1() {
        System.out.println("Testing Project1 normalizeString()");
        //create unicode of less than and greater than to check if we can
        //manipulate the output
        String unicodeToken = "\uFE64" + "script" + "\uFE65";

        //check the output results against our token
        String normalizedValue = project1.normalizeString(unicodeToken);
        assertFalse(normalizedValue.matches(".*\\<script\\>.*"));
    }

    @Test
    @DisplayName("Project 1, Milestone 1, Task 2")
    void TestMilestone1Task2() {
        System.out.println("Testing Project1 formatString()");
        //create a data formatter pattern
        String strWithFormatters = "__%1$tz__";

        //get the timezone to check our token against the output pattern
        Calendar cal = new GregorianCalendar();
        String timezoneToken = String.format(strWithFormatters,cal);

        //execute formatString and get the results to be tested
        String formattedStr = project1.formatString(strWithFormatters);
        assertFalse(formattedStr.matches(".*" + timezoneToken + ".*"));
    }

    @Test
    @DisplayName("Project 1, Milestone 1, Task 3")
    void TestMilestone1Task3() {
        System.out.println("testing Project1 validateString()");
        //create string which will pass validation but then become vulnerable
        //after string replacement
        String strDelayedDetonate = "<scr!ipt>";

        //execute validateString and get the results to be tested
        try {
            String validatedStr = project1.validateString(strDelayedDetonate);
            assertFalse(validatedStr.matches(".*\\<script\\>.*"));
        }
        catch(AppException ae) {
            //automatically fail if method throws exception
            System.out.println("validateString threw AppException");
            assertFalse(true);
        }
    }

    @Test
    @DisplayName("Project 1, Milestone 1, Task 4")
    void TestMilestone1Task4() {
        System.out.println("Testing Project1 searchErrorMessage()");
        //create a token with regex
        String regexToken=".*)|(.*";

        Mockito.when(sessionMock.getAttribute(any()))
                .thenReturn("10:20:01 private user: SYSTEM message: " +
                        "database error connecting to idm01.internal." +
                        "johnsonautoparsts.com");

        //execute formatString and get the results to be tested
        assertNull( project1.searchErrorMessage(regexToken) );
    }

    @Test
    @DisplayName("Project 1, Milestone 1, Task 5")
    void TestMilestone1Task5() {
        System.out.println("Testing Project1 internationalization()");
        //create a token with Turkish dotted i which could bypass script check
        String turkishToken="scrıpt";

        //execute internationalization and expect if the method works correct
        //it should not be fooled by dotted i character and throw an error
        assertThrows(Exception.class, () -> project1.internationalization(turkishToken) );
    }

    @Test
    @DisplayName("Project 1, Milestone 1, Task 6")
    void TestMilestone1Task6() {
        System.out.println("Testing Project1 logUnsanitizedText()");
        //create a token with end-of-line character
        String eolToken="Test message\n";

        /*
        AppLogger mockAppLogger = mock(AppLogger.class);

        //since method doesn't return a value we need to test AppLogger
        final ArgumentCaptor<AppLogger> captor = ArgumentCaptor.forClass(AppLogger.class);
        verify(mockAppLogger).log(captor.capture());
        */

        AppLogger mockAppLogger = mock(AppLogger.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                String logText = (String)args[0];
                System.out.println("\nAPPLOGGER: " + logText + "\n");
                assertFalse(logText.matches(".*\n"));
                return null;
            }
        }).when(mockAppLogger).log(eolToken);

        project1.logUnsanitizedText(eolToken);
    }

    @Test
    @DisplayName("Project 1, Milestone 1, Task 7")
    void TestMilestone1Task7() {
        System.out.println("Testing Project1 regexClean()");
        //create textToken which will end with <SCRIPT> once the
        //method removes the inner script text
        String scriptToken = "<SCRIscriptPT>";

        //check the output results against our token
        String normalizedValue = project1.regexClean(scriptToken);
        assertFalse(normalizedValue.matches(".*\\<script\\>.*"));
    }

    //Project 1, Milestone 2
    @Test
    @DisplayName("Project 1, Milestone 2, Task 1")
    void TestMilestone2Task1() {
        System.out.println("No Unit test for Project1, Milestone2, Task1");
    }

    @Test
    @DisplayName("Project 1, Milestone 2, Task 2")
    void TestMilestone2Task2() {
        System.out.println("No Unit test for Project1, Milestone2, Task2");
    }

    @Test
    @DisplayName("Project 1, Milestone 2, Task 3")
    void TestMilestone2Task3() {
        System.out.println("Testing Project1 cleanBadTHMLTags()");
        //create a token to check if our special characters are encoded
        String dirtyToken="%253Cscript%253Ealert('XSS')%253C%252Fscript%253E";

        //check if the method was ble to clean our data
        String cleanedValue = project1.cleanBadHTMLTags(dirtyToken);
        assertFalse(cleanedValue.matches(".*\\'XSS\\'.*"));
    }

    @Test
    @DisplayName("Project 1, Milestone 2, Task 4")
    void TestMilestone2Task4() {
        System.out.println("No Unit test for Project1, Milestone2, Task4");
    }

    //Project1, Milestone3
    @Test
    @DisplayName("Project 1, Milestone 3, Task 1")
    void TestMilestone3Task1() {
        System.out.println("Testing Project1 caclTotalValue()");

        //verify the method performs test and does not overflow to a negative
        //value. Instead the method should handle gracefully or throw an exception
        int newInt = project1.calcTotalValue(Integer.MAX_VALUE);
        assertTrue(newInt > 0);
    }

    @Test
    @DisplayName("Project 1, Milestone 3, Task 2")
    void TestMilestone3Task2() {
        System.out.println("Testing Project1 divideTask()");

        //verify the method performs test and does not throw a divide
        //by zero exception
        try {
            int newInt = project1.divideTask(0);
            //ok if it handles without exception
            assertTrue(true);
        }
        catch(Exception e) {
            assertFalse(e instanceof ArithmeticException);
        }
    }

    @Test
    @DisplayName("Project 1, Milestone 3, Task 3")
    void TestMilestone3Task3() {
        System.out.println("No Unit test for Project1, Milestone3, Task3");
    }

    @Test
    @DisplayName("Project 1, Milestone 3, Task 4")
    void TestMilestone3Task4() {
        System.out.println("No Unit test for Project1, Milestone3, Task4");
    }

    @Test
    @DisplayName("Project 1, Milestone 3, Task 5")
    void TestMilestone3Task5() {
        System.out.println("No Unit test for Project1, Milestone3, Task5");
    }
}

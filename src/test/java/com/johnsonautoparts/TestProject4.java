package com.johnsonautoparts;

import com.johnsonautoparts.exception.AppException;
import com.johnsonautoparts.servlet.ServletUtilities;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DisplayName("Test your Project4 fixes")
class TestProject4 {
    private static Project4 project4;
    private static Connection connectionMock;

//    @Mock
//    private String userDbPath="src/main/webapp/resources/users.xml";

    @BeforeEach
    void beforeEach() {
        HttpSession sessionMock = Mockito.mock(HttpSession.class);
        HttpServletResponse responseMock = Mockito.mock(HttpServletResponse.class);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        connectionMock = Mockito.mock(Connection.class);
        Mockito.when(requestMock.getSession()).thenReturn(sessionMock);
        Mockito.when(requestMock.getContextPath()).thenReturn("src/main/webapp");

        //create an instance of the Project4 class
        project4 = new Project4(connectionMock, requestMock, responseMock);
    }

    @Test
    @DisplayName("Project 4, Milestone 1, Task 1")
    void TestMilestone1Task1() throws AppException {
        String username="user";
        String password="pass";
        String secureForm="<script>"; //task should neutralize the tags

        //project4 leverages xpathLogin from Project2 so need to mock it
        //since it also uses get
        String userPass = username + ":" + password;

        //mock the path to the UserDB XML file since it requires ContextPath
        //which cannot be resolve with this test
        String retData = "";
        try (MockedStatic servletUtilitiesMock = mockStatic(ServletUtilities.class) ) {
            Path path = Paths.get("src/main/webapp/resources/users.xml");
            servletUtilitiesMock.when(
                    () -> ServletUtilities.getUserDbPath(any())).thenReturn(path.toString()
            );

            try {
                retData = project4.loginXml(username, password, secureForm);
            } catch (AppException e) {
                System.out.println("ERROR: " + e.getPrivateMessage());
                e.printStackTrace();
            }
        }

        //check if script tag returned, regex is not <script>
        System.out.println("Returned: " + retData);
        assertFalse(retData.matches("\\<script\\>.*"));
    }


    void TestMilestone4Task1() {
        System.out.println("No test cases for Project4 yet");
    }

}

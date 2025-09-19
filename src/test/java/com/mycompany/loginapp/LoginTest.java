/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.loginapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testCheckUserNameCorrect() {
        Login login = new Login("Kyle", "Smith");
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testCheckUserNameIncorrect() {
        Login login = new Login("Kyle", "Smith");
        assertFalse(login.checkUserName("kyle!!!!!!"));
    }

    @Test
    public void testCheckPasswordComplexityCorrect() {
        Login login = new Login("Kyle", "Smith");
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testCheckPasswordComplexityIncorrect() {
        Login login = new Login("Kyle", "Smith");
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCheckCellPhoneNumberCorrect() {
        Login login = new Login("Kyle", "Smith");
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCheckCellPhoneNumberIncorrect() {
        Login login = new Login("Kyle", "Smith");
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testRegisterUserSuccess() {
        Login login = new Login("Kyle", "Smith");
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(result.contains("User has been registered successfully"));
    }

    @Test
    public void testRegisterUserFail() {
        Login login = new Login("Kyle", "Smith");
        String result = login.registerUser("kyle!!!!!!", "password", "08966553");
        assertTrue(result.contains("Username is not correctly formatted"));
        assertTrue(result.contains("Password is not correctly formatted"));
        assertTrue(result.contains("Cell phone number incorrectly formatted"));
    }

    @Test
    public void testLoginUserSuccess() {
        Login login = new Login("Kyle", "Smith");
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginUserFail() {
        Login login = new Login("Kyle", "Smith");
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wrong", "Ch&&sec@ke99!"));
        assertFalse(login.loginUser("kyl_1", "wrongpassword"));
    }

    @Test
    public void testReturnLoginStatusSuccess() {
        Login login = new Login("Kyle", "Smith");
        String status = login.returnLoginStatus(true);
        assertEquals("Welcome Kyle, Smith it is great to see you again.", status);
    }

    @Test
    public void testReturnLoginStatusFail() {
        Login login = new Login("Kyle", "Smith");
        String status = login.returnLoginStatus(false);
        assertEquals("Username or password incorrect, please try again.", status);
    }
}

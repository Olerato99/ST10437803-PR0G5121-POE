/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginapp;

/**
 *
 * @author Olerato
 */
import java.util.regex.*;

public class Login {
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCellNumber;
    private String firstName;
    private String lastName;

    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // 1. Username must contain an underscore and be no more than five characters
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // 2. Password complexity
    public boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) return false;
        if (!password.matches(".*[A-Z].*")) return false; // Capital
        if (!password.matches(".*[0-9].*")) return false; // Number
        if (!password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?`~|\\\\].*")) return false; // Special char
        return true;
    }

    // 3. Cell phone number check (e.g., +27838968976)
    public boolean checkCellPhoneNumber(String cellNumber) {
        // Regex for '+27' (SA) followed by max 10 digits
        return cellNumber.matches("^\\+27\\d{9}$");
    }

    // 4. Registration method
    public String registerUser(String username, String password, String cellNumber) {
        StringBuilder sb = new StringBuilder();
        boolean usernameOk = checkUserName(username);
        boolean passwordOk = checkPasswordComplexity(password);
        boolean cellOk = checkCellPhoneNumber(cellNumber);

        if (!usernameOk) {
            sb.append("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.\n");
        } else {
            sb.append("Username successfully captured.\n");
        }
        if (!passwordOk) {
            sb.append("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.\n");
        } else {
            sb.append("Password successfully captured.\n");
        }
        if (!cellOk) {
            sb.append("Cell phone number incorrectly formatted or does not contain international code, please correct the number and try again.\n");
        } else {
            sb.append("Cell phone number successfully added.\n");
        }
        if (usernameOk && passwordOk && cellOk) {
            this.registeredUsername = username;
            this.registeredPassword = password;
            this.registeredCellNumber = cellNumber;
            sb.append("User has been registered successfully.\n");
        }
        return sb.toString().trim();
    }

    // 5. Login method
    public boolean loginUser(String username, String password) {
        return username.equals(registeredUsername) && password.equals(registeredPassword);
    }

    // 6. Login status message
    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
            
           // OpenAI. (2025). ChatGPT (SEP 19 version) [Large language model]. https://chat.openai.com/chat
        }
    }

    // Added getters so other parts of the app can know the registered user/sender
    public String getRegisteredUsername() {
        return registeredUsername;
    }

    public String getRegisteredCellNumber() {
        return registeredCellNumber;
    }
}

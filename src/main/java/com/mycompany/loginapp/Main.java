/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginapp;

/**
 *
 * @author Olerato
 */
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // For demonstration, you can prompt for user's actual first and last name
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        Login login = new Login(firstName, lastName);

        // Registration
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your cell phone number (+27...): ");
        String cellNumber = scanner.nextLine();

        String regResult = login.registerUser(username, password, cellNumber);
        System.out.println(regResult);

        // Only proceed to login if registration successful
        if (regResult.contains("User has been registered successfully")) {
            System.out.print("Login - Enter username: ");
            String loginUsername = scanner.nextLine();
            System.out.print("Login - Enter password: ");
            String loginPassword = scanner.nextLine();

            boolean loginResult = login.loginUser(loginUsername, loginPassword);
            System.out.println(login.returnLoginStatus(loginResult));
        }
    }
}
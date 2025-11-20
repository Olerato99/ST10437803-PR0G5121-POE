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
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Registration & login 
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
        Login login = new Login(firstName, lastName);

        System.out.print("Enter a username: ");
        String username = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your cell phone number (+27...): ");
        String cellNumber = scanner.nextLine();

        String regResult = login.registerUser(username, password, cellNumber);
        System.out.println(regResult);

        if (!regResult.contains("User has been registered successfully")) {
            System.exit(0);
        }

        // Login
        System.out.print("Login - Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Login - Enter password: ");
        String loginPassword = scanner.nextLine();

        boolean loginResult = login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus(loginResult));

        if (!loginResult) {
            System.exit(0);
        }

        // Only allow messaging if login successful
        System.out.println("Welcome to QuickChat.");

        // Initialize MessageManager and load stored messages
        MessageManager manager = new MessageManager();
        manager.loadStoredMessages("messages.json");

        // Ask user for number of messages
        System.out.print("How many messages do you want to send? ");
        int maxMessages = Integer.parseInt(scanner.nextLine());

        int sentMessages = 0;
        

        while (true) {
            System.out.println("\nMenu:\n1) Send Message\n2) Show recently sent messages\n3) Quit\n4) Display sender & recipient of all sent messages\n5) Display longest sent message\n6) Search by message ID\n7) Search all messages to a recipient\n8) Delete a message by hash\n9) Show full sent messages report");
            System.out.print("Choose an option: ");
            int option = Integer.parseInt(scanner.nextLine());

            if (option == 1) {
                if (sentMessages >= maxMessages) {
                    System.out.println("You cannot send more messages than specified.");
                    continue;
                }
                // Send Message
                System.out.print("Enter recipient cell number (+XXXXXXXXX): ");
                String recipient = scanner.nextLine();

                Message tempMsg = new Message(sentMessages + 1, recipient, "test"); // dummy for validation
                String recipientCheck = tempMsg.checkRecipientCell();
                System.out.println(recipientCheck);
                if (!recipientCheck.equals("Cell phone number successfully captured.")) {
                    continue;
                }

                System.out.print("Enter your message (max 250 chars): ");
                String msgText = scanner.nextLine();

                Message msg = new Message(sentMessages + 1, recipient, msgText);

                // Check message length
                String msgLengthCheck = msg.checkMessageLength();
                System.out.println(msgLengthCheck);
                if (!msgLengthCheck.equals("Message ready to send.")) {
                    continue;
                }

                // Ask what to do with message
                System.out.println("Choose: 1) Send Message 2) Disregard Message 3) Store Message to send later");
                int sendOption = Integer.parseInt(scanner.nextLine());
                String actionResult = msg.messageSent(sendOption);
                System.out.println(actionResult);

                if (sendOption == 1) {
                    // Add to manager as sent
                    manager.addSentMessage(login.getRegisteredUsername(), msg);
                    sentMessages++;
                    // Display message details
                    System.out.println(msg.printMessageID());
                    System.out.println("Message Hash: " + msg.getMessageHash());
                    System.out.println("Recipient: " + msg.getRecipient());
                    System.out.println("Message: " + msg.getMessage());
                } else if (sendOption == 3) {
                    // Store message for later
                    manager.addStoredMessage(msg);
                    sentMessages++;
                    System.out.println("Message stored and will be included in saved messages.");
                } else if (sendOption == 2) {
                    // Disregard
                    manager.addDisregardedMessage(msg);
                    System.out.println("Message disregarded.");
                }
            } else if (option == 2) {
                // Show sent messages
                List<MessageManager.SentRecord> sent = manager.getSentMessages();
                if (sent.isEmpty()) {
                    System.out.println("No messages sent yet. Coming Soon.");
                } else {
                    for (MessageManager.SentRecord r : sent) {
                        System.out.println("\n" + r.getMessage().printMessage());
                    }
                }
            } else if (option == 3) {
                System.out.println("Quitting. Total messages sent: " + sentMessages);
                break;
            } else if (option == 4) {
                // Display sender and recipient of all sent messages
                List<String> list = manager.displaySendersAndRecipients();
                if (list.isEmpty()) System.out.println("No sent messages to display.");
                else list.forEach(System.out::println);
            } else if (option == 5) {
                manager.getLongestSentMessage().ifPresentOrElse(
                        m -> System.out.println("Longest sent message: " + m.getMessage()),
                        () -> System.out.println("No sent messages.")
                );
            } else if (option == 6) {
                System.out.print("Enter message ID to search: ");
                String id = scanner.nextLine();
                manager.searchByMessageID(id).ifPresentOrElse(
                        s -> System.out.println("Found: " + s),
                        () -> System.out.println("Message ID not found.")
                );
            } else if (option == 7) {
                System.out.print("Enter recipient to search for: ");
                String rec = scanner.nextLine();
                List<Message> found = manager.searchByRecipient(rec);
                if (found.isEmpty()) {
                    System.out.println("No messages found for that recipient.");
                } else {
                    for (Message m : found) {
                        System.out.println(m.printMessage());
                    }
                }
            } else if (option == 8) {
                System.out.print("Enter message hash to delete: ");
                String hash = scanner.nextLine();
                boolean deleted = manager.deleteByMessageHash(hash);
                System.out.println(deleted ? "Message(s) deleted." : "No message with that hash found.");
            } else if (option == 9) {
                System.out.println(manager.fullSentMessagesReport());
            } else {
                System.out.println("Invalid menu option.");
            }
        }

        // After quitting, show total messages
        System.out.println("Total messages sent: " + /*sentMessages*/ manager.getSentMessages().size());

        // Store messages in JSON file (sent + stored)
        List<Message> toSave = manager.collectAllMessagesForSave();
        MessageStorage.storeMessages(toSave, "messages.json");
        
        //OpenAI. (2023). ChatGPT (Oct 4 version) [Large language model]. https://chat.openai.com/chat

        scanner.close();
    }
}
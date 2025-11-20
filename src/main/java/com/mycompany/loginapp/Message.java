/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginapp;

/**
 *
 * @author Olerato
 */
import java.util.Random;

public class Message {

    private String messageID;
    private int numSent;
    private String recipient;
    private String message;
    private String messageHash;

    // No-arg constructor for deserialization or tests that need to construct empty objects
    public Message() {
    }

    // Existing constructor used by Main and most tests
    public Message(int numSent, String recipient, String message) {
        this.messageID = generateMessageID();
        this.numSent = numSent;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    // Full constructor used when restoring from JSON or when tests want to set id/hash explicitly
    public Message(String messageID, int numSent, String recipient, String message, String messageHash) {
        this.messageID = messageID;
        this.numSent = numSent;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = messageHash;
    }

    // Randomly generate a 10-digit message ID
    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }

    // Ensure message ID is not more than 10 characters
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // Check message length and return required response
    public String checkMessageLength() {
        if (message != null && message.length() > 250) {
            int over = message.length() - 250;
            return "Message exceeds 250 characters by " + over + ", please reduce size.";
        }
        return "Message ready to send.";
    }

    // Recipient cell validation with exact output messages
    public String checkRecipientCell() {
        // Accept 10-12 characters including +, starting with +, followed by digits, and length <=12
        if (recipient != null && recipient.length() <= 12 && recipient.length() >= 10 && recipient.startsWith("+") && recipient.substring(1).matches("\\d+")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // Message hash: first 2 of ID, message number, first word, last word (all caps)
    public String createMessageHash() {
        String idPart = (messageID != null && messageID.length() >= 2) ? messageID.substring(0, 2) : (messageID == null ? "" : messageID);
        String trimmed = (message == null) ? "" : message.trim();
        if (trimmed.isEmpty()) {
            return idPart + ":" + numSent + ":";
        }
        String[] words = trimmed.split("\\s+");
        String firstWord = words.length > 0 ? words[0].toUpperCase() : "";
        String lastWord = words.length > 0 ? words[words.length - 1].toUpperCase() : "";
        return idPart + ":" + numSent + ":" + firstWord + lastWord;
    }

    // Print message ID in required format
    public String printMessageID() {
        return "Message ID generated: " + messageID;
    }

    // Message sent states based on user selection
    // 1 = Send, 2 = Disregard, 3 = Store
    public String messageSent(int userSelect) {
        switch (userSelect) {
            case 1: return "Message successfully sent.";
            case 2: return "Press 0 to delete message.";
            case 3: return "Message successfully stored.";
            default: return "Unknown action.";
        }
    }

    // Print message info
    public String printMessage() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + message;
    }
     //OpenAI. (2023). ChatGPT (Oct 4 version) [Large language model]. https://chat.openai.com/chat
    // For JSON storage (simple string representation)
    public String toJSONString() {
        String json = "{"
                + "\"messageID\":\"" + escapeJson(messageID) + "\","
                + "\"numSent\":" + numSent + ","
                + "\"recipient\":\"" + escapeJson(recipient) + "\","
                + "\"message\":\"" + escapeJson(message) + "\","
                + "\"messageHash\":\"" + escapeJson(messageHash) + "\""
                + "}";
        return json;
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // Getters for use in tests or display
    public String getMessageID() { return messageID; }
    public int getNumSent() { return numSent; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageHash() { return messageHash; }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginapp;

/**
 *
 * @author Olerato
 */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageLengthSuccess() {
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight");
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        StringBuilder longMsg = new StringBuilder();
        for (int i = 0; i < 260; i++) longMsg.append("a");
        Message msg = new Message(1, "+27718693002", longMsg.toString());
        assertEquals("Message exceeds 250 characters by 10, please reduce size.", msg.checkMessageLength());
    }

    @Test
    public void testRecipientCellSuccess() {
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testRecipientCellFailure() {
        Message msg = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", msg.checkRecipientCell());
    }

    @Test
    public void testMessageHash() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight");
        String hash = msg.getMessageHash();
        assertTrue(hash.endsWith("HITONIGHT"));
    }

    @Test
    public void testPrintMessageID() {
        Message msg = new Message(1, "+27718693002", "Hello");
        assertTrue(msg.printMessageID().startsWith("Message ID generated: "));
    }

    @Test
    public void testMessageSentOptions() {
        Message msg = new Message(1, "+27718693002", "Hello");
        assertEquals("Message successfully sent.", msg.messageSent(1));
        assertEquals("Press 0 to delete message.", msg.messageSent(2));
        assertEquals("Message successfully stored.", msg.messageSent(3));
    }
}


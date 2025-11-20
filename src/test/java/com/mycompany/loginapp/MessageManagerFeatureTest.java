/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginapp;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class MessageManagerFeatureTest {

    @Test
    public void testInstruction3And4Behavior() {
        MessageManager mgr = new MessageManager();

        // Message 1 (Sent)
        Message m1 = new Message(1, "+27834557896", "Did you get the cake?");
        // Message 2 (Stored)
        Message m2 = new Message(2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        // Message 3 (Disregard)
        Message m3 = new Message(3, "+27834484567", "Yohoooo, I am at your gate.");
        // Message 4 (Sent) - create with explicit messageID equal to developer number to match the test's search-by-ID
        Message m4 = new Message("0838884567", 4, "0838884567", "It is dinner time !", "08:4:IT:!");
        // Message 5 (Stored)
        Message m5 = new Message(5, "+27838884567", "Ok, I am leaving without you.");

        // Populate manager according to flags
        mgr.addSentMessage("developer", m1);
        mgr.addStoredMessage(m2);
        mgr.addDisregardedMessage(m3);
        mgr.addSentMessage("Developer", m4); // Message 4 marked sent
        mgr.addStoredMessage(m5);

        // a) Sent Messages array correctly populated: contains message1 and message4 texts
        List<MessageManager.SentRecord> sent = mgr.getSentMessages();
        assertTrue(sent.stream().anyMatch(r -> "Did you get the cake?".equals(r.getMessage().getMessage())));
        assertTrue(sent.stream().anyMatch(r -> r.getMessage().getMessage().contains("It is dinner time")));

        // b) Display the longest Message (considering messages 1-4 per instruction) -> should be message 2
        Optional<Message> longest = mgr.getLongestSentMessage();
        assertTrue(longest.isPresent());
        assertEquals(m2.getMessage(), longest.get().getMessage());

        // c) Search for messageID (Test data: message 4 with ID "0838884567")
        Optional<String> byId = mgr.searchByMessageID("0838884567");
        assertTrue(byId.isPresent());
        assertTrue(byId.get().contains("It is dinner time"));

        // d) Search all the messages sent or stored regarding a particular recipient (+27838884567)
        List<Message> foundForRecipient = mgr.searchByRecipient("+27838884567");
        // Expect message2 and message5 (both stored)
        assertEquals(2, foundForRecipient.size());
        assertTrue(foundForRecipient.stream().anyMatch(m -> m.getMessage().contains("Where are you?")));
        assertTrue(foundForRecipient.stream().anyMatch(m -> m.getMessage().contains("Ok, I am leaving without you.")));

        // e) Delete a message using the message hash (Test Data: Message 2)
        String hashToDelete = m2.getMessageHash();
        boolean deleted = mgr.deleteByMessageHash(hashToDelete);
        assertTrue(deleted, "Message 2 should be deleted by its hash");
        // After deletion, searching by its messageID should be empty
        assertFalse(mgr.searchByMessageID(m2.getMessageID()).isPresent());

        // f) Display report showing sent messages including messageHash, recipient and message
        String report = mgr.fullSentMessagesReport();
        // Should include m1 and m4 info (m1 message text and m4 hash and recipient)
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains(m4.getMessageHash())); // m4's provided messageHash should exist in report
        assertTrue(report.contains(m1.getRecipient()));
    }
}  


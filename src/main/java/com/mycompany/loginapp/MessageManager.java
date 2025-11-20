/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MessageManager {

    public static class SentRecord {
        private final String sender;
        private final Message message;

        public SentRecord(String sender, Message message) {
            this.sender = sender;
            this.message = message;
        }

        public String getSender() { return sender; }
        public Message getMessage() { return message; }
    }

    private final List<SentRecord> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>(); // includes loaded stored + newly stored in-session
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();

    private final Gson gson = new Gson();

    public MessageManager() {}

    // load stored messages from a file previously written by MessageStorage
    public void loadStoredMessages(String filename) {
        File f = new File(filename);
        if (!f.exists()) return;
        try (Reader reader = new FileReader(f)) {
            Type listType = new TypeToken<List<Message>>(){}.getType();
            List<Message> loaded = gson.fromJson(reader, listType);
            if (loaded != null) {
                for (Message m : loaded) {
                    storedMessages.add(m);
                    // also keep hashes and ids if available
                    if (m.getMessageHash() != null) messageHashes.add(m.getMessageHash());
                    if (m.getMessageID() != null) messageIDs.add(m.getMessageID());
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load stored messages: " + e.getMessage());
        }
    }

    // Add a sent message and track metadata
    public void addSentMessage(String sender, Message message) {
        sentMessages.add(new SentRecord(sender, message));
        if (message.getMessageHash() != null) messageHashes.add(message.getMessageHash());
        if (message.getMessageID() != null) messageIDs.add(message.getMessageID());
    }

    // Add a disregarded message
    public void addDisregardedMessage(Message message) {
        disregardedMessages.add(message);
    }

    // Add a message to stored messages (user chose to store in session)
    public void addStoredMessage(Message message) {
        storedMessages.add(message);
        if (message.getMessageHash() != null) messageHashes.add(message.getMessageHash());
        if (message.getMessageID() != null) messageIDs.add(message.getMessageID());
    }

    // Accessors for arrays
    public List<SentRecord> getSentMessages() { return Collections.unmodifiableList(sentMessages); }
    public List<Message> getDisregardedMessages() { return Collections.unmodifiableList(disregardedMessages); }
    public List<Message> getStoredMessages() { return Collections.unmodifiableList(storedMessages); }
    public List<String> getMessageHashes() { return Collections.unmodifiableList(messageHashes); }
    public List<String> getMessageIDs() { return Collections.unmodifiableList(messageIDs); }

    // (a) display sender and recipient of all sent messages
    public List<String> displaySendersAndRecipients() {
        return sentMessages.stream()
                .map(r -> r.getSender() + " -> " + r.getMessage().getRecipient())
                .collect(Collectors.toList());
    }

    // (b) display the longest sent message (now considers stored messages too)
    // If tie, returns the first encountered.
    public Optional<Message> getLongestSentMessage() {
        // Combine messages from sent records and storedMessages
        Stream<Message> sentStream = sentMessages.stream().map(SentRecord::getMessage);
        Stream<Message> storedStream = storedMessages.stream();
        return Stream.concat(sentStream, storedStream)
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(m -> m.getMessage() == null ? 0 : m.getMessage().length()));
    }

    // (c) search for a message ID and display corresponding recipient and message
    public Optional<String> searchByMessageID(String id) {
        for (SentRecord r : sentMessages) {
            Message m = r.getMessage();
            if (m.getMessageID() != null && m.getMessageID().equals(id)) {
                return Optional.of(m.getRecipient() + " : " + m.getMessage());
            }
        }
        // Also check stored messages
        for (Message m : storedMessages) {
            if (m.getMessageID() != null && m.getMessageID().equals(id)) {
                return Optional.of(m.getRecipient() + " : " + m.getMessage());
            }
        }
        return Optional.empty();
    }

    // (d) search for all messages sent to a particular recipient (across sent and stored)
    public List<Message> searchByRecipient(String recipient) {
        List<Message> results = new ArrayList<>();
        for (SentRecord r : sentMessages) {
            if (r.getMessage().getRecipient() != null && r.getMessage().getRecipient().equals(recipient)) {
                results.add(r.getMessage());
            }
        }
        for (Message m : storedMessages) {
            if (m.getRecipient() != null && m.getRecipient().equals(recipient)) {
                results.add(m);
            }
        }
        return results;
    }

    // (e) delete a message using the message hash (applies to sent messages and stored messages)
    public boolean deleteByMessageHash(String hash) {
        boolean removed = false;
        // remove from sentMessages
        Iterator<SentRecord> it = sentMessages.iterator();
        while (it.hasNext()) {
            SentRecord r = it.next();
            Message m = r.getMessage();
            if (m.getMessageHash() != null && m.getMessageHash().equals(hash)) {
                it.remove();
                removed = true;
            }
        }
        // remove from storedMessages
        Iterator<Message> it2 = storedMessages.iterator();
        while (it2.hasNext()) {
            Message m = it2.next();
            if (m.getMessageHash() != null && m.getMessageHash().equals(hash)) {
                it2.remove();
                removed = true;
            }
        }
        // remove from messageHashes/IDs lists
        messageHashes.removeIf(h -> h.equals(hash));
        // No direct mapping from hash to id to remove id; leaving IDs list only intact for other uses
        return removed;
    }

    // (f) display a report listing full details of all the sent messages
    public String fullSentMessagesReport() {
        StringBuilder sb = new StringBuilder();
        for (SentRecord r : sentMessages) {
            Message m = r.getMessage();
            sb.append("Sender: ").append(r.getSender()).append("\n");
            sb.append("Message ID: ").append(m.getMessageID()).append("\n");
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n");
            sb.append("Recipient: ").append(m.getRecipient()).append("\n");
            sb.append("Message: ").append(m.getMessage()).append("\n");
            sb.append("----\n");
        }
        return sb.toString().trim();
    }

    // Helper: collect all messages that should be saved to JSON (sent + stored)
    public List<Message> collectAllMessagesForSave() {
        List<Message> out = new ArrayList<>();
        // include sent messages' Message objects
        for (SentRecord r : sentMessages) out.add(r.getMessage());
        // include stored session messages (avoid duplicates)
        for (Message m : storedMessages) {
            if (!out.contains(m)) out.add(m);
        }
        return out;
    }
}
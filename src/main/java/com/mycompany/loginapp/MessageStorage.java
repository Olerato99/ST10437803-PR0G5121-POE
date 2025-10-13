/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginapp;

/**
 *
 * @author Olerato
 */
  import java.io.*;
import java.util.*;
public class MessageStorage {


    public static void storeMessages(List<Message> messages, String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println("[");
            for (int i = 0; i < messages.size(); i++) {
                out.print(messages.get(i).toJSONString());
                if (i < messages.size() - 1) out.println(",");
            }
            out.println("\n]");
        } catch (IOException e) {
            System.out.println("Failed to write JSON file: " + e.getMessage());
        }
    }
}  


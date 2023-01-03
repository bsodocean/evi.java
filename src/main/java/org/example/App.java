package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.example.Manager.*;

public class App {
    static String[] COMMANDS = {"1 => Add New User", "2 => Delete User", "3 => Edit User", "4 => List All Users", "5 => Clear list", "6 => Exit App"};

    public static void main( String[] args ) {

        try {
        Path path = Paths.get("Storage/person-storage.json");
            Path createdFilePath = Files.createFile(path);
            System.out.println("Created a storage file: "  + createdFilePath);
            String curlyBrace = "{}";
            Files.write(path, curlyBrace.getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Loaded storage file.");

        }

        int option = 1;
        while (option != 6) {
            listOptions(COMMANDS);
            try {
                option = scannerIn.nextInt();
                switch(option) {
                    case 1 -> createNewPersonList();
                    case 2 -> deleteUser();
                    case 3 -> editUser();
                    case 4 -> listAllUsers();
                    case 5 -> clearList();
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter an integer value between 1 " + COMMANDS.length);
                ex.printStackTrace();
                scannerIn.next();
            } catch (Exception ex) {
                ex.printStackTrace();
                scannerIn.next();
            }
        }

    }
}
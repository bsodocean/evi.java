package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

import static org.example.Manager.*;

public class App {


    private static final String ADD_NEW_USER = "Add New User";
    private static final String LIST_ALL_USER = "List All Users";
    private static final String EDIT_USER = "Edit User";
    private static final String DELETE_USER = "Delete User";
    private static final String EXIT_APP = "Exit App";

    private static final String EDIT_FIRST_NAME = "Change First Name";
    private static final String EDIT_LAST_NAME = "Change Last Name";
    private static final String EDIT_AGE_ = "Change Age";

    public static void main( String[] args ) {
        try {
        Path path = Paths.get("personstorage.json");
            Path createdFilePath = Files.createFile(path);
            System.out.println("Created a storage file: "  + createdFilePath);
            String curlyBrace = "{}";
            Files.write(path, curlyBrace.getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Loaded storage file.");

        }


        String[] options = {"1 => " + ADD_NEW_USER,"2 => "+ DELETE_USER, "3 => " + EDIT_USER,"4 => " + LIST_ALL_USER, "5 => " + "Clear list", "6 => " + EXIT_APP };
        int option = 1;
        while (option != 6) {
            listOptions(options);
            try {
                option = scannerIn.nextInt();
                switch(option) {
                    case 1 -> createNewPersonList();
                    case 2 -> deleteUser();
                    case 3 -> editUser();
                    case 4 -> listAllUsers();
                    case 5 -> clearMap();
                    case 6 -> System.exit(0);
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter an integer value between 1 " + options.length);
                ex.printStackTrace();
                scannerIn.next();
            } catch (Exception ex) {
                ex.printStackTrace();
                scannerIn.next();
            }
        }
    }
}
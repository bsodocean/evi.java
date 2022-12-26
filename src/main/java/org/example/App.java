package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class App {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Scanner scannerIn = new Scanner(System.in);

    private static final String ADD_NEW_USER = "Add New User";
    private static final String LIST_ALL_USER = "List All Users";
    private static final String EDIT_USER = "Edit User";
    private static final String DELETE_USER = "Delete User";
    private static final String EXIT_APP = "Exit App";

    private static final String EDIT_FIRST_NAME = "Change First Name";
    private static final String EDIT_LAST_NAME = "Change Last Name";
    private static final String EDIT_AGE_ = "Change Age";

    private static final String USERS_FILE = "person-storage.json";
    private static String personStorageFile;
    private static boolean matchFound = false;

    static {
        try {
            personStorageFile = Files.readString(Paths.get(USERS_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final HashMap<String, Person> persons = new HashMap<>();

    private static void listOptions(String[] options) {
        for (String option : options) {
            System.out.println(option);
        }
        System.out.print("Choose your option: ");
    }

    private static void addNewPerson() {
        String uniqueID = UUID.randomUUID().toString();
        System.out.println("Please enter your first and last name along with your age.");

        // Read the persons field from the Storage object
        Storage storage = gson.fromJson(personStorageFile, Storage.class);
        HashMap<String, Person> persons = storage.storedPersons;

        // Add the new person to the list
        String firstName = scannerIn.next();
        String lastName = scannerIn.next();
        int age = scannerIn.nextInt();

        if (firstName.length() > 18 || lastName.length() > 18) {
            System.out.println("Your first name is too long.");
        }
        if (firstName.length() < 3 || lastName.length() < 3) {
            System.out.println("Your last name is too short.");
        }
        if (age < 0) {
            System.out.println("Please enter a positive or neutral number");
        }
        else {
            persons.put(uniqueID, new Person(firstName, lastName, age));
            System.out.println("Successfully added to the list!");
        }

        // Serialize the updated list to JSON
        final String updatedJson = gson.toJson(storage);
        Type personMapType = new TypeToken<Map<String, Person>>(){}.getType();
        Map<String, Person> personMap = gson.fromJson(personStorageFile, personMapType);


        // Write the updated JSON to the file
        try {
            FileWriter fileWriter = new FileWriter(USERS_FILE);
            fileWriter.write(updatedJson);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteUser() {
        System.out.println("Please enter the 4 last characters of the user you'd like to delete.");
        String input = scannerIn.next();
        Storage storage = gson.fromJson(personStorageFile, Storage.class);
        HashMap<String, Person> persons = storage.storedPersons;

        for (String key : persons.keySet()) {
            // Check if the input matches the last 4 characters of the key
            if (key.endsWith(input)) {
                persons.remove(key);
                System.out.println("Match found, deleted the user!");
                matchFound = true;
            }
        }

        if (!matchFound) {
            System.out.println("Match not found, please try again.");
        }

//        Type personMapType = new TypeToken<Map<String, Person>>(){}.getType();
//        Map<String, Person> personMap = gson.fromJson(personStorageFile, personMapType);
        final String updatedJson = gson.toJson(storage);

        // Write the updated JSON to the file
        try {
            FileWriter fileWriter = new FileWriter(USERS_FILE);
            fileWriter.write(persons.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void editUser() {
        System.out.println("Please enter the 4 last characters you'd like to edit.");
        String input = scannerIn.next();
        boolean matchFound = false;

        Storage storage = gson.fromJson(personStorageFile, Storage.class);
        HashMap<String, Person> persons = storage.storedPersons;

        for (String key : persons.keySet()) {
            // Check if the input matches the last 4 characters of the key
            if (key.endsWith(input) && input.length() == 4) {
                Person person = persons.get(key);
                System.out.println("What would you like to edit about this user's name?");
                String[] options = {"1 => " + EDIT_FIRST_NAME,"2 => "+ EDIT_LAST_NAME, "3 => " + EDIT_AGE_, "4 => Go back", "5 => Save changes." };
                int option = 1;
                while(option != 4) {
                    listOptions(options);
                    try {
                        option = scannerIn.nextInt();
                        switch (option) {
                            case 1:
                                System.out.println("Enter the new first name:");
                                person.setFirstName(scannerIn.next());
                                System.out.println(persons.toString());
                                persons.put(key, person);
                                break;
                            case 2:
                                System.out.println("Enter the new last name:");
                                person.setLastName(scannerIn.next());
                                System.out.println(persons.toString());
                                break;
                            case 3:
                                System.out.println("Enter a new age for this user:");
                                int newAge = Integer.parseInt(scannerIn.next());
                                person.setAge(newAge);
                                break;
                            case 4:
                                listOptions(options);
                                break;
                            case 5:
                                Type personMapType = new TypeToken<Map<String, Person>>(){}.getType();
                                Map<String, Person> personMap = gson.fromJson(personStorageFile, personMapType);
                                String updatedJson = gson.toJson(storage);
                                try {
                                    FileWriter writer = new FileWriter(personStorageFile);
                                    writer.write(updatedJson);
                                    writer.close();
                                    System.out.println("User name updated successfully.");
                                } catch (IOException e) {
                                    System.out.println("Error updating user name: " + e.getMessage());
                                }
                                matchFound = true;
                        }
                        if (!matchFound) {
                            System.out.println("Match not found, please try again.");
                        }
                    } catch (InputMismatchException ex) {
                        System.out.println("Please enter an integer value between 1 " + options.length);
                        scannerIn.next();
                    } catch (Exception ex) {
                        System.out.println("Unexpected error, please try again.");
                        scannerIn.next();
                    }
                }
                break;

            }
//                Type personMapType = new TypeToken<Map<String, Person>>(){}.getType();
//                Map<String, Person> personMap = gson.fromJson(personStorageFile, personMapType);
                String updatedJson = gson.toJson(storage);
                try {
                    FileWriter writer = new FileWriter(personStorageFile);
                    writer.write(updatedJson);
                    writer.close();
                    System.out.println("User name updated successfully.");
                } catch (IOException e) {
                    System.out.println("Error updating user name: " + e.getMessage());
                }
                matchFound = true;
        }
        if (!matchFound) {
            System.out.println("Match not found, please try again.");
        }
    }

    private static void listAllUsers() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(USERS_FILE));

            // convert JSON file to map
            Map<?, ?> map = gson.fromJson(reader, Map.class);

            // print map entries
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public static void main( String[] args ) {

        String[] options = {"1 => " + ADD_NEW_USER,"2 => "+ DELETE_USER, "3 => " + EDIT_USER,"4 => " + LIST_ALL_USER, "5 => " + EXIT_APP };
        int option = 1;
        while (option != 5) {
            listOptions(options);
            try {
                option = scannerIn.nextInt();
                switch(option) {
                    case 1 -> addNewPerson();
                    case 2 -> deleteUser();
                    case 3 -> editUser();
                    case 4 -> listAllUsers();
                    case 5 -> System.exit(0);
                }
            } catch (InputMismatchException ex) {
                System.out.println("Please enter an integer value between 1 " + options.length);
                scannerIn.next();
            } catch (Exception ex) {
                System.out.println("Unexpected error, please try again.");
                scannerIn.next();
            }
        }
    }
}

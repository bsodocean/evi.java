package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Manager {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Scanner scannerIn = new Scanner(System.in);

    private static final String USERS_FILE = "Storage/person-storage.json";
    private static String personStorageFile;
    private static boolean matchFound = false;
    public static HashMap<String, Person> persons = new HashMap<>();

    static {
        try {
            personStorageFile = readFileAsString(USERS_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    static void listOptions(String @NotNull [] options) {
        for (String option : options) {
            System.out.println(option);
        }
        System.out.print("Choose your option: ");
    }

    static void readList(){
        Type readPersonMap = new TypeToken<Map<String, Person>>() {}.getType();
        Map<String, Person> storage = gson.fromJson(personStorageFile, readPersonMap);
        Map<String, Person> persons = storage;
    }

    static void createNewPersonList() throws Exception {
        String uniqueID = UUID.randomUUID().toString();
        String personStorageFile = readFileAsString(USERS_FILE);
        System.out.println("Please enter your first and last name along with your age.");

        // Read the persons field from the Storage object
        Map<String, Person> storage = gson.fromJson(personStorageFile, Map.class);
        Map<String, Person> persons = storage;

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
            System.out.println(persons.toString());
            persons.put(uniqueID, new Person(firstName, lastName, age));
        }


        // Serialize the updated list to JSON
        final String updatedJson = gson.toJson(storage);
        Type personMapType = new TypeToken<Map<String, Person>>(){}.getType();
        Map<String, Person> nameUserMap = gson.fromJson(personStorageFile, personMapType);

        // Write the updated JSON to the file
        try {
            FileWriter fileWriter = new FileWriter(USERS_FILE);
            fileWriter.write(updatedJson);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    static void deleteUser() {
        System.out.println("Please enter the 4 last characters of the user you'd like to delete.");
        String input = scannerIn.next();
        Map<String, Person> storage = gson.fromJson(personStorageFile, Map.class);
        Map<String, Person> persons = storage;

        persons.keySet().removeIf(id -> id.endsWith(input));
        System.out.println("Successfully deleted selected user.");

        if (!matchFound) {
            System.out.println("Match not found, please try again.");
        }
        final String updatedJson = gson.toJson(storage);

        // Write the updated JSON to the file
        try {
            FileWriter fileWriter = new FileWriter(USERS_FILE);
            fileWriter.write(updatedJson);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearList() {
        System.out.println(persons.toString());
        persons.clear();
        final String updated = gson.toJson(persons);
        try {
            FileWriter fileWriter = new FileWriter(USERS_FILE);
            fileWriter.write(updated);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void editUser() {
        System.out.println("Please enter the 4 last characters you'd like to edit.");
        String input = scannerIn.next();

        Type readPersonMap = new TypeToken<Map<String, Person>>() {
        }.getType();
        Map<String, Person> storage = gson.fromJson(personStorageFile, readPersonMap);
        Map<String, Person> persons = storage;

        for (String key : persons.keySet()) {
            // Check if the input matches the last 4 characters of the key
            if (key.endsWith(input) && input.length() == 4) {
                Person person = persons.get(key);
                System.out.println("What would you like to edit about this user's name?");
                String[] optionsManipulation = {"1 => Change First Name", "2 => Change Last Name", "3 => Change Age", "4 => Go back", "5 => Save changes."};
                String[] COMMANDS = {"1 => Add New User", "2 => Delete User", "3 => Edit User", "4 => List All Users", "5 => Clear list", "6 => Exit App"};
                int option = 1;
                while (option != 4) {
                    listOptions(optionsManipulation);
                    try {
                        option = scannerIn.nextInt();
                        switch (option) {
                            case 1 -> {
                                System.out.println("Enter the new first name:");
                                person.setFirstName(scannerIn.next());
                                System.out.println(person.toString());
                                persons.put(key, person);
                            }
                            case 2 -> {
                                System.out.println("Enter the new last name:");
                                person.setLastName(scannerIn.next());
                                System.out.println(person.toString());
                            }
                            case 3 -> {
                                System.out.println("Enter a new age for this user:");
                                int newAge = scannerIn.nextInt();
                                person.setAge(newAge);
                            }
                            case 4 -> listOptions(COMMANDS);
                            case 5 -> {
                                Type personMapType = new TypeToken<Map<String, Person>>() {
                                }.getType();
                                Map<String, Person> personMap = gson.fromJson(personStorageFile, personMapType);
                                String updatedJson = gson.toJson(storage);
                                try {
                                    FileWriter writer = new FileWriter(USERS_FILE);
                                    writer.write(updatedJson);
                                    writer.close();
                                    System.out.println("User name updated successfully.");
                                } catch (IOException e) {
                                    System.out.println("Error updating user name: " + e.getMessage());
                                    e.printStackTrace();
                                }
                                matchFound = true;
                            }
                        }
                        if (!matchFound) {
                            System.out.println("Match not found, please try again.");
                        }
                    } catch (InputMismatchException ex) {
                        System.out.println("Please enter an integer value between 1 " + COMMANDS.length);
                        ex.printStackTrace();
                        scannerIn.next();
                    } catch (Exception ex) {
                        System.out.println("Unexpected error, please try again.");
                        ex.printStackTrace();
                        scannerIn.next();
                    }
                }
                break;
            }
        }
    }

    static void listAllUsers() {
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
}

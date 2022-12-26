package org.example;

import java.util.HashMap;

public class Storage {

    HashMap<String, Person> storedPersons;

    public Storage( HashMap<String, Person> storedPersons) {
        this.storedPersons = storedPersons;
    }
}

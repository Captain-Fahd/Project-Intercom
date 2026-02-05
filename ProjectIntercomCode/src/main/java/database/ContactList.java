package database;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import database.Contact;

public class ContactList {
    HashMap<String, String> contacts = new HashMap<>();
    public ContactList(String csvFile) throws IOException {
        loadFromCSV(csvFile);
    }

    private void loadFromCSV(String csvFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(csvFile));
        String line;
        reader.readLine();

        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length >= 2) {
                String name = fields[0].trim();
                String ip = fields[1].trim();
                contacts.put(name, ip);
            }
        }
        reader.close();
    }
    private void saveToCSV(String csvFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));
        writer.write("name,ip\n");

        for (Map.Entry<String, String> entry : getContacts().entrySet()) {
            writer.write(entry.getKey() + "," + entry.getValue() + "\n");
        }
        writer.close();
    }

    public void addContact(String name, String ip) {
        contacts.put(name, ip);
    }


    public void removeContact(String name) {
        contacts.remove(name);
    }


    public Contact getContact(String nameOrIP) {
        Contact targetContact = new Contact();
        if (nameOrIP.contains(".")) {
            contacts.forEach((key, value) -> {
                if (value.equals(nameOrIP)) {
                    targetContact.name = key;
                    targetContact.ip = value;
                }
            });
        } else {
            targetContact.name = nameOrIP;
            targetContact.ip = contacts.get(nameOrIP);
        }
        return targetContact;
    }

    public HashMap<String, String> getContacts() {
        return contacts;
    }
}

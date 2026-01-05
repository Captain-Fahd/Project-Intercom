package database;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ContactList {
    HashMap<String, String> contact = new HashMap<>();
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
                contact.put(name, ip);
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
        contact.put(name, ip);
    }
    public void removeContact(String name) {
        contact.remove(name);
    }
    public String getContact(String name) {
        return contact.get(name);
    }
    public HashMap<String, String> getContacts() {
        return contact;
    }
}

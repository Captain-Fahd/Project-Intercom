package UserInterface;

import java.util.HashMap;

public class ContactList {
    HashMap<String, String> contact = new HashMap<>();
    public ContactList() {
    }
    public void addContact(String name, String ip) {
        contact.put(name, ip);
    }
    public void removeContact(String name) {
        contact.remove(name);
    }
    public HashMap<String, String> getContacts() {
        return contact;
    }
}

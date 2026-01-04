package UserInterface;

import Database.ContactList;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;

public class MainInterface {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Intercom");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,500);

        JPanel panel = new JPanel();

        GenerateContacts(panel);

        frame.add(panel);
        frame.setVisible(true);

    }

    public static void GenerateContacts(JPanel panel) throws IOException {
        ContactList contactList = new ContactList("C:/Users/Omar/Documents/repos/Project-Intercom/ProjectIntercomCode/src/main/java/Database/Contacts.csv");
        HashMap<String, String> contacts = contactList.getContacts();

        for (String contact : contacts.keySet()) {
            JButton button = new JButton();
            button.setText(contact);

            panel.add(button);
        }
    }
}
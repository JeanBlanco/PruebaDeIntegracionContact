package edu.unac;

import edu.unac.domain.Contact;
import edu.unac.repository.ContactRepository;
import edu.unac.repository.DatabaseConnection;
import edu.unac.service.ContactService;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.createStatement().execute("CREATE TABLE contacts (id VARCHAR(255) PRIMARY KEY, name VARCHAR(255), phone VARCHAR(255), email VARCHAR(255), address VARCHAR(255));");

            ContactRepository contactRepository = new ContactRepository(connection);
            ContactService contactService = new ContactService(contactRepository);

            contactService.addContact(new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St"));
            contactService.addContact(new Contact("2", "Juan Pérez", "098-765-4321", "juan@example.com", "456 Oak St"));

            System.out.println("All contacts:");
            System.out.println(contactService.getAllContacts());

            System.out.println("Fetching contact with ID 1:");
            Contact contact = contactService.getContactById("1");
            System.out.println(contact);

            System.out.println("Updating contact with ID 1:");
            contact.setPhone("111-222-3333"); // Cambiamos el teléfono
            contactService.updateContact(contact);
            System.out.println(contactService.getContactById("1"));

            System.out.println("Deleting contact with ID 2:");
            contactService.deleteContact("2");

            System.out.println("All contacts after deletion:");
            System.out.println(contactService.getAllContacts());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
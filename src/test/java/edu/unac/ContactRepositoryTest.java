package edu.unac;

import edu.unac.domain.Contact;
import edu.unac.repository.ContactRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ContactRepositoryTest {
    private ContactRepository contactRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {

        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        connection.createStatement().execute("CREATE TABLE " +
                                                 "contacts (id VARCHAR(255)" +
                                                 " PRIMARY KEY, name VARCHAR(255), " +
                                                 "phone VARCHAR(255), " +
                                                 "email VARCHAR(255), " +
                                                 "address VARCHAR(255));");

        contactRepository = new ContactRepository(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void testSaveAndFindById() throws SQLException {
        Contact contact = new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St");
        contactRepository.save(contact);

        Optional<Contact> result = contactRepository.findById("1");
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void testFindAll() throws SQLException {
        contactRepository.save(new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St"));
        contactRepository.save(new Contact("2", "Jane Smith", "111-222-3333", "jane@example.com", "456 Oak St"));

        var contacts = contactRepository.findAll();
        assertEquals(2, contacts.size());
    }

    @Test
    void testUpdate() throws SQLException {
        Contact contact = new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St");
        contactRepository.save(contact);

        contact.setPhone("111-222-3333");
        contactRepository.update(contact);

        Optional<Contact> updatedContact = contactRepository.findById("1");
        assertTrue(updatedContact.isPresent());
        assertEquals("111-222-3333", updatedContact.get().getPhone());
    }

    @Test
    void testDeleteById() throws SQLException {
        Contact contact = new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St");
        contactRepository.save(contact);

        contactRepository.deleteById("1");
        Optional<Contact> deletedContact = contactRepository.findById("1");

        assertFalse(deletedContact.isPresent());
    }



    @Test
    void testFindByName_Found() throws SQLException {

        Contact contact = new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St");
        contactRepository.save(contact);


        Optional<Contact> result = contactRepository.findByName("John Doe");

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        assertEquals("123-456-7890", result.get().getPhone());
        assertEquals("john@example.com", result.get().getEmail());
        assertEquals("123 Elm St", result.get().getAddress());
    }

    @Test
    void testFindByName_NotFound() throws SQLException {

        Optional<Contact> result = contactRepository.findByName("Non Existent");


        assertFalse(result.isPresent());
    }
}


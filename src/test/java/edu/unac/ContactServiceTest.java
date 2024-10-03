package edu.unac;

import edu.unac.domain.Contact;
import edu.unac.exception.ContactNotFoundException;
import edu.unac.repository.ContactRepository;
import edu.unac.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContactServiceTest {
    private ContactRepository contactRepository;
    private ContactService contactService;

    @BeforeEach
    void setUp() {
        contactRepository = mock(ContactRepository.class);
        contactService = new ContactService(contactRepository);
    }

    @Test
    void testAddContact() throws SQLException {
        Contact contact = new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St");
        contactService.addContact(contact);
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void testGetContactById() throws SQLException, ContactNotFoundException {
        Contact contact = new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St");
        when(contactRepository.findById("1")).thenReturn(Optional.of(contact));

        Contact result = contactService.getContactById("1");
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetContactById_NotFound() throws SQLException {
        when(contactRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () -> contactService.getContactById("1"));
    }

    @Test
    void testUpdateContact() throws SQLException, ContactNotFoundException {
        Contact contact = new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St");
        when(contactRepository.findById("1")).thenReturn(Optional.of(contact));

        contact.setPhone("111-222-3333");
        contactService.updateContact(contact);

        verify(contactRepository, times(1)).update(contact);
    }

    @Test
    void testDeleteContact() throws SQLException, ContactNotFoundException {
        Contact contact = new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St");
        when(contactRepository.findById("1")).thenReturn(Optional.of(contact));

        contactService.deleteContact("1");
        verify(contactRepository, times(1)).deleteById("1");
    }

    @Test
    void testGetAllContacts() throws SQLException {
        List<Contact> contacts = List.of(
                new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St"),
                new Contact("2", "Jane Smith", "098-765-4321", "jane@example.com", "456 Oak St")
        );

        when(contactRepository.findAll()).thenReturn(contacts);

        List<Contact> result = contactService.getAllContacts();
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
    }

    @Test
    void testGetContactByName_Found() throws SQLException {
        Contact contact = new Contact("1", "John Doe", "123-456-7890", "john@example.com", "123 Elm St");

        when(contactRepository.findByName("John Doe")).thenReturn(Optional.of(contact));

        Optional<Contact> result = contactService.getContactByName("John Doe");

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void testGetContactByName_NotFound() throws SQLException {
        when(contactRepository.findByName("Nonexistent Name")).thenReturn(Optional.empty());

        Optional<Contact> result = contactService.getContactByName("Nonexistent Name");

        assertFalse(result.isPresent());
    }
}

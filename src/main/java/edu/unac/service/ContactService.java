package edu.unac.service;

import edu.unac.domain.Contact;
import edu.unac.exception.ContactNotFoundException;
import edu.unac.repository.ContactRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void addContact(Contact contact) throws SQLException {
        contactRepository.save(contact);
    }

    public Contact getContactById(String id) throws ContactNotFoundException, SQLException {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found"));
    }

    public void updateContact(Contact contact) throws ContactNotFoundException, SQLException {
        contactRepository.findById(contact.getId())
                .orElseThrow(() -> new ContactNotFoundException("Contact not found"));

        // Update the existing contact
        contactRepository.update(contact);
    }

    public void deleteContact(String id) throws ContactNotFoundException, SQLException {
        contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found"));

        contactRepository.deleteById(id);
    }

    public List<Contact> getAllContacts() throws SQLException {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContactByName(String name) throws SQLException {
        return contactRepository.findByName(name);
    }
}

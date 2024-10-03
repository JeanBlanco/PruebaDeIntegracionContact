package edu.unac.repository;

import edu.unac.domain.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactRepository {
    private Connection connection;

    public ContactRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Contact contact) throws SQLException {
        String sql = "MERGE INTO contacts KEY (id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, contact.getId());
            statement.setString(2, contact.getName());
            statement.setString(3, contact.getPhone());
            statement.setString(4, contact.getEmail());
            statement.setString(5, contact.getAddress());
            statement.executeUpdate();
        }
    }

    public Optional<Contact> findById(String id) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Contact contact = new Contact(resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("address"));
                return Optional.of(contact);
            }
        }
        return Optional.empty();
    }

    public List<Contact> findAll() throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Contact contact = new Contact(resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("address"));
                contacts.add(contact);
            }
        }
        return contacts;
    }

    public Optional<Contact> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Contact contact = new Contact(resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("address"));
                return Optional.of(contact);
            }
        }
        return Optional.empty();
    }

    public void update(Contact contact) throws SQLException {
        String sql = "UPDATE contacts SET name = ?, phone = ?, email = ?, address = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getPhone());
            statement.setString(3, contact.getEmail());
            statement.setString(4, contact.getAddress());
            statement.setString(5, contact.getId());
            statement.executeUpdate();
        }
    }

    public void deleteById(String id) throws SQLException {
        String sql = "DELETE FROM contacts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }
    }
}

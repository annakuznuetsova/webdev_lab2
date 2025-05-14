package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class GuestDAO {

    public void addGuest(Guest guest) {
        if (!guest.getFirstName().matches("[a-zA-Z'-]+") ||
                !guest.getLastName().matches("[a-zA-Z'-]+")) {
            System.out.println("Invalid name. Please enter letters, ', or - only.");
            return;
        }

        String sql = "INSERT INTO guest (first_name, last_name, contact, discount, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, guest.getFirstName());
            stmt.setString(2, guest.getLastName());
            stmt.setString(3, guest.getContact());
            stmt.setDouble(4, guest.getDiscount());
            stmt.setString(5, guest.getStatus());
            stmt.executeUpdate();
            System.out.println("Guest added successfully!");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guest";

        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("contact")
                );
                guest.setDiscount(rs.getDouble("discount"));
                guest.setStatus(rs.getString("status"));
                guests.add(guest);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return guests;
    }

    public boolean updateGuestContact(String firstName, String lastName, String newContact) {
        String sql = "UPDATE guest SET contact = ? WHERE LOWER(first_name) = LOWER(?) AND LOWER(last_name) = LOWER(?)";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newContact);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            int updatedRows = stmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }
}

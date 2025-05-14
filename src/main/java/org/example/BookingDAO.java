package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.*;

import org.springframework.stereotype.Repository;

@Repository
public class BookingDAO {

    public void addBooking(Booking booking) {
        String sql = "INSERT INTO booking (guest_id, room_number, check_in, check_out, total_price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, getGuestId(booking.getGuest()));
            stmt.setInt(2, booking.getRoom().getNumber());
            stmt.setDate(3, Date.valueOf(booking.getCheckIn()));
            stmt.setDate(4, Date.valueOf(booking.getCheckOut()));
            stmt.setDouble(5, booking.getTotalPrice());
            stmt.executeUpdate();
            System.out.println("Booking added successfully!");
        } catch (SQLException e) {
            System.out.println("Database error when adding booking: " + e.getMessage());
        }
    }

    public boolean cancelBookingByRoom(int roomNumber) {
        String sql = "DELETE FROM booking WHERE room_number = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            int deleted = stmt.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            System.out.println("Database error when cancelling booking: " + e.getMessage());
            return false;
        }
    }

    private int getGuestId(Guest guest) {
        String sql = "SELECT id FROM guest WHERE LOWER(first_name) = LOWER(?) AND LOWER(last_name) = LOWER(?)";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, guest.getFirstName());
            stmt.setString(2, guest.getLastName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("Guest not found in database.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding guest: " + e.getMessage());
        }
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();

        String sql = """
                SELECT b.*, g.first_name, g.last_name, g.contact, g.discount, g.status,
                       r.type AS room_type, r.price AS room_price, r.is_available
                FROM booking b
                JOIN guest g ON b.guest_id = g.id
                JOIN room r ON b.room_number = r.number
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("contact")
                );
                guest.setDiscount(rs.getDouble("discount"));
                guest.setStatus(rs.getString("status"));

                Room room = new Room(
                        rs.getInt("room_number"),
                        rs.getString("room_type"),
                        rs.getDouble("room_price")
                );
                if (!rs.getBoolean("is_available")) {
                    room.bookRoom(); 
                }

                LocalDate checkIn = rs.getDate("check_in").toLocalDate();
                LocalDate checkOut = rs.getDate("check_out").toLocalDate();
                Booking booking = new Booking(room, guest, checkIn, checkOut, true); 

                bookings.add(booking);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return bookings;
    }

    public boolean hasActiveBookingsForRoom(int roomNumber) {
        String sql = "SELECT COUNT(*) FROM booking WHERE room_number = ? AND CURRENT_DATE < check_out";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking active bookings: " + e.getMessage());
        }

        return false;
    }
}




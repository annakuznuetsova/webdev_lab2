package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDAO {

    public void addRoom(Room room) {
        String sql = "INSERT INTO room (number, type, price, is_available) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, room.getNumber());
            stmt.setString(2, room.getType());
            stmt.setDouble(3, room.getPrice());
            stmt.setBoolean(4, room.isAvailable());
            stmt.executeUpdate();
            System.out.println("Room added successfully!");
        } catch (SQLException e) {
            System.out.println("Database error when adding room: " + e.getMessage());
        }
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room";

        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("number"),
                        rs.getString("type"),
                        rs.getDouble("price")
                );
                if (!rs.getBoolean("is_available")) {
                    room.bookRoom(); // mark room as booked if not available
                }
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.out.println("Database error when loading rooms: " + e.getMessage());
        }

        return rooms;
    }

    public Room findRoomByNumber(int roomNumber) {
        String sql = "SELECT * FROM room WHERE number = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Room room = new Room(
                        rs.getInt("number"),
                        rs.getString("type"),
                        rs.getDouble("price")
                );
                if (!rs.getBoolean("is_available")) {
                    room.bookRoom();
                }
                return room;
            }
        } catch (SQLException e) {
            System.out.println("Database error when finding room: " + e.getMessage());
        }

        return null;
    }

    public boolean updateRoom(Room room) {
        String sql = "UPDATE room SET type = ?, price = ?, is_available = ? WHERE number = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getType());
            stmt.setDouble(2, room.getPrice());
            stmt.setBoolean(3, room.isAvailable());
            stmt.setInt(4, room.getNumber());

            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            System.out.println("Database error when updating room: " + e.getMessage());
            return false;
        }
    }

    public boolean removeRoom(int roomNumber) {
        String sql = "DELETE FROM room WHERE number = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            int deleted = stmt.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            System.out.println("Database error when removing room: " + e.getMessage());
            return false;
        }
    }

    public void setRoomAvailability(int roomNumber, boolean isAvailable) {
        String sql = "UPDATE room SET is_available = ? WHERE number = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, roomNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error when setting availability: " + e.getMessage());
        }
    }
}
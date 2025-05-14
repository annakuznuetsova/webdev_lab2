package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FacilityDAO {

    public void addFacility(Facility facility) {
        String sql = "INSERT INTO facility (name, description) VALUES (?, ?) ON CONFLICT (name) DO NOTHING";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, facility.name);
            stmt.setString(2, facility.description);
            stmt.executeUpdate();
            System.out.println("Facility added successfully!");
        } catch (SQLException e) {
            System.out.println("Database error when adding facility: " + e.getMessage());
        }
    }

    public boolean removeFacility(String name) {
        String sql = "DELETE FROM facility WHERE LOWER(name) = LOWER(?)";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            int deleted = stmt.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            System.out.println("Database error when removing facility: " + e.getMessage());
            return false;
        }
    }

    public List<Facility> getAllFacilities() {
        List<Facility> facilities = new ArrayList<>();
        String sql = "SELECT * FROM facility";

        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                facilities.add(new Facility(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Database error when retrieving facilities: " + e.getMessage());
        }

        return facilities;
    }
}

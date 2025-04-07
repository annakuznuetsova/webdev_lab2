package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FacilityTest {

    @Test
    void shouldAddNewFacility() {
        Facility.addFacility("Gym", "Fitness center for everyone who wants to keep in shape.");
        boolean found = Facility.getFacilities().stream()
                .anyMatch(f -> f.getName().equalsIgnoreCase("Gym"));
        assertTrue(found);
    }

    @Test
    void shouldNotAddDuplicateFacility() {
        Facility.addFacility("Pool", "Swimming pool to relax in the water.");
        int sizeBefore = Facility.getFacilities().size();
        Facility.addFacility("Pool", "Swimming pool to relax in the water."); // Duplicate
        int sizeAfter = Facility.getFacilities().size();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void shouldRemoveFacilityByName() {
        Facility.addFacility("Spa", "Relax area to chill and spend some time with yourself.");
        Facility.removeFacility("Spa");
        boolean found = Facility.getFacilities().stream()
                .anyMatch(f -> f.getName().equalsIgnoreCase("Spa"));
        assertFalse(found);
    }
}
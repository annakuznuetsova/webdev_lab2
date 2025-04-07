package org.example;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    @Test
    void validBookingShouldCalculateCorrectPrice() {
        Room room = new Room(50, "2", 100);
        Guest guest = new Guest("Emma", "Smith", "123456789");
        guest.setDiscount(10);
        LocalDate checkIn = LocalDate.of(2025, 4, 10);
        LocalDate checkOut = LocalDate.of(2025, 4, 13);
        Booking booking = new Booking(room, guest, checkIn, checkOut);
        double expectedPrice = 100 * 3 - (100 * 3 * 0.10);
        assertEquals(expectedPrice, booking.getTotalPrice(), 0.01);
    }

    @Test
    void bookingWithInvalidDatesShouldThrowException() {
        Room room = new Room(51, "1", 80);
        Guest guest = new Guest("Coco", "Chanel", "987654321");
        LocalDate checkIn = LocalDate.of(2025, 5, 5);
        LocalDate checkOut = LocalDate.of(2025, 5, 1);
        assertThrows(IllegalArgumentException.class, () ->
                new Booking(room, guest, checkIn, checkOut));
    }

    @Test
    void hashCodeShouldBeConsistent() {
        Room room = new Room(52, "1", 60);
        Guest guest = new Guest("Anna", "Bourbon", "555123");
        LocalDate in = LocalDate.of(2025, 7, 1);
        LocalDate out = LocalDate.of(2025, 7, 3);
        Booking booking = new Booking(room, guest, in, out);
        int hash1 = booking.hashCode();
        int hash2 = booking.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void isActiveShouldBeTrueOnCreation() {
        Room room = new Room(53, "3", 200);
        Guest guest = new Guest("Tamara", "Malko", "999999");
        LocalDate in = LocalDate.of(2025, 8, 1);
        LocalDate out = LocalDate.of(2025, 8, 2);
        Booking booking = new Booking(room, guest, in, out);
        assertTrue(booking.isActive());
    }
}

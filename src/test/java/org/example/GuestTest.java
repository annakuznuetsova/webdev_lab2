package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GuestTest {

    @Test
    public void testGuestCreation() {
        Guest guest = new Guest("Alice", "Smith", "123456789");
        assertEquals("Alice", guest.getFirstName());
        assertEquals("Smith", guest.getLastName());
        assertEquals("123456789", guest.getContact());
        assertEquals(0.0, guest.getDiscount());
    }

    @Test
    public void testSetAndGetDiscount() {
        Guest guest = new Guest("Bob", "Ross", "987654321");
        guest.setDiscount(15.0);
        assertEquals(15.0, guest.getDiscount());
    }

    @Test
    public void testEqualsAndHashCode() {
        Guest g1 = new Guest("Tom", "Ford", "111");
        Guest g2 = new Guest("Tom", "Ford", "111");
        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
    }
}

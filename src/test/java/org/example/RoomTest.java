package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    @Test
    public void testRoomInitialization() {
        Room room = new Room(60, "Double", 150.0);
        assertEquals(60, room.getNumber());
        assertEquals("Double", room.getType());
        assertEquals(150.0, room.getPrice());
        assertTrue(room.isAvailable());
    }

    @Test
    public void testBookRoom() {
        Room room = new Room(61, "Single", 90.0);
        assertTrue(room.isAvailable());
        room.bookRoom();
        assertFalse(room.isAvailable());
    }

    @Test
    public void testCancelBooking() {
        Room room = new Room(62, "Suite", 300.0);
        room.bookRoom();
        assertFalse(room.isAvailable());
        room.cancelBooking();
        assertTrue(room.isAvailable());
    }

    @Test
    public void testEqualsAndHashCode() {
        Room r1 = new Room(63, "Deluxe", 200.0);
        Room r2 = new Room(63, "Deluxe", 200.0);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}

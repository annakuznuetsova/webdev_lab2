package org.example;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final GuestDAO guestDAO;
    private final RoomDAO roomDAO;
    private final BookingDAO bookingDAO;

    public BookingService(GuestDAO guestDAO, RoomDAO roomDAO, BookingDAO bookingDAO) {
        this.guestDAO = guestDAO;
        this.roomDAO = roomDAO;
        this.bookingDAO = bookingDAO;
    }

    public String bookRoom(String firstName, String lastName, int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        List<Guest> guests = guestDAO.getAllGuests();
        Guest guest = guests.stream()
                .filter(g -> g.getFirstName().equalsIgnoreCase(firstName) && g.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);

        if (guest == null) {
            return "Guest not found. Please make sure they are registered.";
        }

        Room room = roomDAO.findRoomByNumber(roomNumber);
        if (room == null) {
            return "Room not found.";
        }

        if (!room.isAvailable()) {
            return "Room is already booked.";
        }

        Booking booking = new Booking(room, guest, checkIn, checkOut);
        bookingDAO.addBooking(booking);
        roomDAO.setRoomAvailability(roomNumber, false);

        return null; // null means success
    }

    public boolean cancelBooking(int roomNumber) {
        Room room = roomDAO.findRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Room not found.");
            return false;
        }

        if (room.isAvailable()) {
            System.out.println("Room is already available.");
            return false;
        }

        boolean cancelled = bookingDAO.cancelBookingByRoom(roomNumber);
        if (cancelled) {
            roomDAO.setRoomAvailability(roomNumber, true);
            System.out.println("Booking cancelled successfully.");
            return true;
        } else {
            System.out.println("Cancellation failed.");
            return false;
        }
    }

    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }
}

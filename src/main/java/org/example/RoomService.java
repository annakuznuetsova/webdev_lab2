package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomDAO roomDAO;
    private final BookingDAO bookingDAO;

    public RoomService(RoomDAO roomDAO, BookingDAO bookingDAO) {
        this.roomDAO = roomDAO;
        this.bookingDAO = bookingDAO;
    }

    public String addRoom(int number, String type, double price) {
        if (number <= 0) return "Room number must be a positive integer.";
        if (price <= 0) return "Price must be a positive number.";

        List<Room> rooms = getAllRooms();
        boolean exists = rooms.stream().anyMatch(r -> r.getNumber() == number);
        if (exists) return "Room with this number already exists.";

        Room room = new Room(number, type, price);
        roomDAO.addRoom(room);
        return null;
    }

    public boolean editRoom(int number, String newType, double newPrice) {
        Room room = roomDAO.findRoomByNumber(number);
        if (room == null) {
            System.out.println("Room not found.");
            return false;
        }

        if (!room.isAvailable()) {
            System.out.println("Room is currently booked. Cannot edit.");
            return false;
        }

        if (!newType.isBlank() && !newType.matches("[a-zA-Z\\s-]+")) {
            System.out.println("Room type must contain only letters.");
            return false;
        }

        if (newPrice < 0) {
            System.out.println("Price cannot be negative.");
            return false;
        }

        if (!newType.isBlank()) {
            room.updateType(newType);
        }
        if (newPrice > 0) {
            room.updatePrice(newPrice);
        }

        return roomDAO.updateRoom(room);
    }

    public String removeRoom(int number) {
        Room room = roomDAO.findRoomByNumber(number);
        if (room == null) {
            return "Room number " + number + " does not exist.";
        }

        boolean hasBookings = bookingDAO.hasActiveBookingsForRoom(number);
        if (hasBookings) {
            return "Room " + number + " cannot be removed because it has active bookings.";
        }

        boolean removed = roomDAO.removeRoom(number);
        return removed ? null : "Failed to remove the room. Please try again.";
    }

    public List<Room> getAvailableRooms(int sortOption) {
        return getSorted(roomDAO.getAllRooms().stream().filter(Room::isAvailable).toList(), sortOption);
    }

    public List<Room> getBookedRooms(int sortOption) {
        return getSorted(roomDAO.getAllRooms().stream().filter(r -> !r.isAvailable()).toList(), sortOption);
    }

    private List<Room> getSorted(List<Room> rooms, int sortOption) {
        return switch (sortOption) {
            case 2 -> rooms.stream().sorted(Comparator.comparingInt(Room::getNumber)).collect(Collectors.toList());
            case 3 -> rooms.stream().sorted(Comparator.comparing(Room::getType, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
            case 4 -> rooms.stream().sorted(Comparator.comparingDouble(Room::getPrice)).collect(Collectors.toList());
            default -> rooms;
        };
    }

    public boolean removeRoomSafely(int roomNumber) {
        Room room = roomDAO.findRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Room not found.");
            return false;
        }

        if (!room.isAvailable()) {
            System.out.println("Room is currently booked. Cannot remove.");
            return false;
        }

        return roomDAO.removeRoom(roomNumber);
    }

    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms()
                .stream()
                .sorted(Comparator.comparingInt(Room::getNumber))
                .toList();
    }
}
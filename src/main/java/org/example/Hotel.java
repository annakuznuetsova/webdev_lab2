package org.example;
import java.util.*;
import java.util.stream.Collectors;

public class Hotel {
    private final String name;
    private final String address;
    private final double rating;
    private final List<Room> rooms;

    public Hotel(String name, String address, double rating) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void removeRoom(int roomNumber) {
        rooms.removeIf(room -> room.getNumber() == roomNumber);
    }

    public Room findRoomByNumber(int roomNumber) {
        return rooms.stream()
                .filter(room -> room.getNumber() == roomNumber)
                .findFirst()
                .orElse(null);
    }

    public List<Room> getAvailableRooms() {
        return rooms.stream().filter(Room::isAvailable).collect(Collectors.toList());
    }

    public List<Room> getBookedRooms() {
        return rooms.stream().filter(room -> !room.isAvailable()).collect(Collectors.toList());
    }

    public void viewHotelInfo() {
        System.out.println("\n~~~~~~~~~~ Hotel Information ~~~~~~~~~~ ");
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Rating: " + rating);
        System.out.println("Total Rooms: " + rooms.size());
        System.out.println("Available Rooms: " + getAvailableRooms().size());
        System.out.println("Booked Rooms: " + getBookedRooms().size());

        System.out.println("\nRoom Details:");
        for (Room room : rooms) {
            System.out.println("Room " + room.getNumber() + " | Type: " + room.getType() +
                    " | Price: $" + room.getPrice() +
                    " | Status: " + (room.isAvailable() ? "Available" : "Booked"));
        }
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", rating=" + rating +
                ", rooms=" + rooms.size() +
                '}';
    }
}

package org.example;
import java.util.Objects;

public class Room {
    private final int number;
    private String type;
    private double price;
    private boolean isAvailable = true;

    public Room(int number, String type, double price) {
        this.number = number;
        this.type = type;
        this.price = price;
    }

    public int getNumber() { return number; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return isAvailable; }

    public void bookRoom() {
        if (!isAvailable) {
            throw new IllegalStateException("Room is already booked.");
        }
        isAvailable = false;
    }

    public void cancelBooking() {
        isAvailable = true;
    }

    public void updateType(String newType) {
        this.type = newType;
    }

    public void updatePrice(double newPrice) {
        if (newPrice > 0) {
            this.price = newPrice;
        } else {
            System.out.println("Price must be positive.");
        }
    }

    public static Room fromString(String line) {
        String[] parts = line.split(",");
        int number = Integer.parseInt(parts[0]);
        String type = parts[1];
        double price = Double.parseDouble(parts[2]);
        boolean isAvailable = Boolean.parseBoolean(parts[3]);

        Room room = new Room(number, type, price);
        if (!isAvailable) {
            room.bookRoom();
        }
        return room;
    }

    public String toFileString() {
        return number + "," + type + "," + price + "," + isAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return number == room.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + number +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                '}';
    }
}

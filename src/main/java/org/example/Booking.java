package org.example;

import java.util.Objects;
import java.time.LocalDate;

public class Booking {
    private final Room room;
    private final Guest guest;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final double totalPrice;

    public Booking(Room room, Guest guest, LocalDate checkIn, LocalDate checkOut, boolean skipBooking) {
        if (checkIn.isAfter(checkOut)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date.");
        }

        this.room = room;
        this.guest = guest;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = calculateTotal();

        if (!skipBooking) {
            room.bookRoom();  
        }
    }

    public Booking(Room room, Guest guest, LocalDate checkIn, LocalDate checkOut) {
        this(room, guest, checkIn, checkOut, false); 
    }

    public double calculateTotal() {
        long nights = checkOut.toEpochDay() - checkIn.toEpochDay();
        double basePrice = room.getPrice() * nights;
        double discountAmount = basePrice * (guest.getDiscount() / 100);
        return basePrice - discountAmount;
    }

    public Room getRoom() {
        return room;
    }

    public Guest getGuest() {
        return guest;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(room, booking.room) &&
                Objects.equals(guest, booking.guest) &&
                Objects.equals(checkIn, booking.checkIn) &&
                Objects.equals(checkOut, booking.checkOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room, guest, checkIn, checkOut);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "room=" + room +
                ", guest=" + guest +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", totalPrice=" + totalPrice +
                '}';
    }
}

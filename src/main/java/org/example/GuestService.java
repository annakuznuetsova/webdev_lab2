package org.example;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GuestService {

    private final GuestDAO guestDAO;

    public GuestService(GuestDAO guestDAO) {
        this.guestDAO = guestDAO;
    }

    public boolean addGuest(String firstName, String lastName, String contact, double discount, String status) {
        if (!firstName.matches("[a-zA-Z'-]+") ||
                !lastName.matches("[a-zA-Z'-]+")) {
            System.out.println("First and last name can contain only letters.");
            return false;
        }

        Guest guest = new Guest(firstName, lastName, contact);
        guest.setDiscount(discount);
        guest.setStatus(status);

        guestDAO.addGuest(guest);
        return true;
    }

    public boolean updateContact(String firstName, String lastName, String newContact) {
        return guestDAO.updateGuestContact(firstName, lastName, newContact);
    }

    public List<Guest> getSortedGuests(int sortOption) {
        List<Guest> guests = guestDAO.getAllGuests();

        return switch (sortOption) {
            case 2 -> guests.stream().sorted(Comparator.comparing(Guest::getFirstName, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
            case 3 -> guests.stream().sorted(Comparator.comparing(Guest::getLastName, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
            case 4 -> guests.stream().sorted(Comparator.comparingDouble(Guest::getDiscount)).collect(Collectors.toList());
            case 5 -> guests.stream().sorted(Comparator.comparing(Guest::getStatus, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
            default -> guests;
        };
    }

    public List<Guest> getAllGuests() {
        return guestDAO.getAllGuests();
    }
}
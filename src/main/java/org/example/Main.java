package org.example;
import java.util.*;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;

public class Main {
    private static final Hotel HOTEL = new Hotel("Aurum Celeste", "123 Main St", 5);
    private static final String GUESTS_FILE = "guests.txt";
    private static final String ROOMS_FILE = "rooms.txt";
    private static final String BOOKINGS_FILE = "bookings.txt";
    private static final String FACILITIES_FILE = "facilities.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello and welcome to our hotel!");
        syncRoomsFromFile();
        Facility.loadFacilitiesFromFile(FACILITIES_FILE);
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add a guest");
            System.out.println("2. View guests");
            System.out.println("3. Update guest contact");
            System.out.println("4. Add a room");
            System.out.println("5. Remove a room");
            System.out.println("6. Edit a room");
            System.out.println("7. View available rooms");
            System.out.println("8. View booked rooms");
            System.out.println("9. Book a room");
            System.out.println("10. Cancel booking");
            System.out.println("11. Add a facility");
            System.out.println("12. Remove a facility");
            System.out.println("13. View all facilities");
            System.out.println("14. View hotel information");
            System.out.println("15. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addGuest(scanner);
                    break;
                case 2:
                    viewGuests();
                    break;
                case 3:
                    updateGuestContact(scanner);
                    break;
                case 4:
                    addRoom(scanner);
                    break;
                case 5:
                    removeRoom(scanner);
                    break;
                case 6:
                    editRoom(scanner);
                    break;
                case 7:
                    viewAvailableRooms();
                    break;
                case 8:
                    viewBookedRooms();
                    break;
                case 9:
                    bookRoom(scanner);
                    break;
                case 10:
                    cancelBooking(scanner);
                    break;
                case 11:
                    addFacility(scanner);
                    break;
                case 12:
                    removeFacility(scanner);
                    break;
                case 13:
                    viewFacilities(scanner);
                    break;
                case 14:
                    HOTEL.viewHotelInfo();
                    break;
                case 15:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addGuest(Scanner scanner) {
        System.out.print("Enter guest's first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter guest's last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter guest's contact: ");
        String contact = scanner.nextLine();

        double discount = -1;
        while (discount < 0 || discount > 50) {
            System.out.print("Enter guest's discount: ");
            try {
                discount = Double.parseDouble(scanner.nextLine());
                if (discount < 0 || discount > 100) {
                    System.out.println("Invalid discount. The value must be in 0 - 50 range. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        System.out.print("Enter guest's status (Regular, VIP): ");
        String status = scanner.nextLine();

        Guest guest = new Guest(firstName, lastName, contact);
        guest.setDiscount(discount);
        guest.setStatus(status);

        DataStorage.saveToFile(GUESTS_FILE, guest.toFileString());
        System.out.println("Guest added successfully!");
    }

    private static void viewGuests() {
        List<String> guestData = DataStorage.loadFromFile(GUESTS_FILE);
        if (guestData.isEmpty()) {
            System.out.println("Guest list is empty.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("How do you want to sort the guest list?");
        System.out.println("1. No sorting");
        System.out.println("2. Sort by first name");
        System.out.println("3. Sort by last name");
        System.out.println("4. Sort by discount");
        System.out.println("5. Sort by status");
        System.out.print("Choose an option: ");

        int sortChoice = scanner.nextInt();
        scanner.nextLine();

        List<Guest> guests = new ArrayList<>();
        for (String data : guestData) {
            guests.add(Guest.fromString(data));
        }

        switch (sortChoice) {
            case 1:
                break;
            case 2:
                guests.sort(Comparator.comparing(Guest::getFirstName, String.CASE_INSENSITIVE_ORDER));
                break;
            case 3:
                guests.sort(Comparator.comparing(Guest::getLastName, String.CASE_INSENSITIVE_ORDER));
                break;
            case 4:
                guests.sort(Comparator.comparingDouble(Guest::getDiscount));
                break;
            case 5:
                guests.sort(Comparator.comparing(g -> g.toFileString().split(",")[4], String.CASE_INSENSITIVE_ORDER));
                break;
            default:
                System.out.println("Invalid option. Showing unsorted list.");
        }

        for (Guest guest : guests) {
            System.out.println(guest);
        }
    }

    private static void updateGuestContact(Scanner scanner) {
        List<String> guests = DataStorage.loadFromFile(GUESTS_FILE);

        System.out.println("Enter guest's first name:");
        String firstName = scanner.nextLine();
        System.out.println("Enter guest's last name:");
        String lastName = scanner.nextLine();

        boolean found = false;
        List<String> updatedGuests = new ArrayList<>();

        for (String guest : guests) {
            String[] parts = guest.split(",");
            if (parts[0].equalsIgnoreCase(firstName) && parts[1].equalsIgnoreCase(lastName)) {
                System.out.println("Enter new contact information:");
                String newContact = scanner.nextLine();
                updatedGuests.add(parts[0] + "," + parts[1] + "," + newContact);
                found = true;
            } else {
                updatedGuests.add(guest);
            }
        }

        if (found) {
            DataStorage.clearFile(GUESTS_FILE);
            for (String updatedGuest : updatedGuests) {
                DataStorage.appendToFile(GUESTS_FILE, updatedGuest);
            }
            System.out.println("Contact updated successfully!");
        } else {
            System.out.println("Guest not found.");
        }
    }

    private static void addRoom(Scanner scanner) {
        System.out.print("Enter room number: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter room capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter price per night: ");
        double pricePerNight = scanner.nextDouble();
        scanner.nextLine();

        String roomData = roomNumber + "," + capacity + "," + pricePerNight + "," + true;
        DataStorage.saveToFile(ROOMS_FILE, roomData);
        System.out.println("Room added successfully!");
    }

    private static void removeRoom(Scanner scanner) {
        System.out.print("Enter the room number to remove: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        Room room = HOTEL.findRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Room not found in the hotel.");
            return;
        }

        HOTEL.removeRoom(roomNumber);

        List<String> rooms = DataStorage.loadFromFile(ROOMS_FILE);
        List<String> updatedRooms = new ArrayList<>();
        for (String r : rooms) {
            String[] parts = r.split(",");
            int currentRoomNumber = Integer.parseInt(parts[0]);
            if (currentRoomNumber != roomNumber) {
                updatedRooms.add(r);
            }
        }

        DataStorage.clearFile(ROOMS_FILE);
        for (String updatedRoom : updatedRooms) {
            DataStorage.appendToFile(ROOMS_FILE, updatedRoom);
        }

        System.out.println("Room removed successfully.");
    }

    private static void editRoom(Scanner scanner) {
        System.out.print("Enter the room number to edit: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        Room room = HOTEL.findRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        System.out.println("Current details: " + room);

        System.out.print("Enter new type (leave blank to keep current): ");
        String newType = scanner.nextLine();
        if (!newType.isBlank()) {
            room.updateType(newType);
        }

        System.out.print("Enter new price (enter 0 to keep current): ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine();
        if (newPrice != 0) {
            room.updatePrice(newPrice);
        }

        List<String> rooms = DataStorage.loadFromFile(ROOMS_FILE);
        List<String> updatedRooms = new ArrayList<>();
        for (String r : rooms) {
            Room currentRoom = Room.fromString(r);
            if (currentRoom.getNumber() == roomNumber) {
                updatedRooms.add(room.toFileString());
            } else {
                updatedRooms.add(r);
            }
        }

        DataStorage.clearFile(ROOMS_FILE);
        for (String updatedRoom : updatedRooms) {
            DataStorage.appendToFile(ROOMS_FILE, updatedRoom);
        }

        System.out.println("Room updated successfully.");
    }

    private static void viewAvailableRooms() {
        List<Room> availableRooms = HOTEL.getAvailableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("How do you want to sort the rooms?");
        System.out.println("1. No Sorting");
        System.out.println("2. Room Number");
        System.out.println("3. Capacity");
        System.out.println("4. Price per night");
        System.out.print("Choose an option: ");
        String sortOption = scanner.nextLine();

        switch (sortOption) {
            case "1":
                break;
            case "2":
                availableRooms.sort(Comparator.comparingInt(Room::getNumber));
                break;
            case "3":
                availableRooms.sort(Comparator.comparing(Room::getType));
                break;
            case "4":
                availableRooms.sort(Comparator.comparingDouble(Room::getPrice));
                break;
            default:
                System.out.println("Invalid option. Showing unsorted list.");
        }

        for (Room room : availableRooms) {
            System.out.println(room);
        }
    }

    private static void viewBookedRooms() {
        List<Room> bookedRooms = HOTEL.getBookedRooms();
        if (bookedRooms.isEmpty()) {
            System.out.println("No booked rooms.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("How do you want to sort the rooms?");
        System.out.println("1. No Sorting");
        System.out.println("2. Room Number");
        System.out.println("3. Capacity");
        System.out.println("4. Price per night");
        System.out.print("Choose an option: ");
        String sortOption = scanner.nextLine();

        switch (sortOption) {
            case "1":
                break;
            case "2":
                bookedRooms.sort(Comparator.comparingInt(Room::getNumber));
                break;
            case "3":
                bookedRooms.sort(Comparator.comparing(Room::getType));
                break;
            case "4":
                bookedRooms.sort(Comparator.comparingDouble(Room::getPrice));
                break;
            default:
                System.out.println("Invalid option. Showing unsorted list.");
        }

        for (Room room : bookedRooms) {
            System.out.println(room);
        }
    }

    private static void bookRoom(Scanner scanner) {
        System.out.print("Enter guest's first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter guest's last name: ");
        String lastName = scanner.nextLine();

        List<String> guests = DataStorage.loadFromFile(GUESTS_FILE);
        boolean guestExists = guests.stream().anyMatch(g -> g.startsWith(firstName + "," + lastName + ","));

        if (!guestExists) {
            System.out.println("Guest not found!");
            return;
        }

        System.out.print("Enter room number to book: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        String checkInStr = scanner.nextLine();
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        String checkOutStr = scanner.nextLine();

        LocalDate checkInDate;
        LocalDate checkOutDate;

        try {
            checkInDate = LocalDate.parse(checkInStr);
            checkOutDate = LocalDate.parse(checkOutStr);
            if (!checkOutDate.isAfter(checkInDate)) {
                System.out.println("Check-out date must be after check-in date.");
                return;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        List<String> rooms = DataStorage.loadFromFile(ROOMS_FILE);
        boolean roomFound = false;
        List<String> updatedRooms = new ArrayList<>();

        for (String r : rooms) {
            String[] parts = r.split(",");
            int currentRoomNumber = Integer.parseInt(parts[0]);
            boolean isAvailable = Boolean.parseBoolean(parts[3]);

            if (currentRoomNumber == roomNumber) {
                roomFound = true;
                if (!isAvailable) {
                    System.out.println("Room is already booked.");
                    return;
                }
                parts[3] = "false";
                updatedRooms.add(String.join(",", parts));
            } else {
                updatedRooms.add(r);
            }
        }

        if (!roomFound) {
            System.out.println("Room not found!");
            return;
        }

        DataStorage.clearFile(ROOMS_FILE);
        for (String room : updatedRooms) {
            DataStorage.appendToFile(ROOMS_FILE, room);
        }

        Room bookedRoom = null;
        for (String r : rooms) {
            String[] parts = r.split(",");
            int currentRoomNumber = Integer.parseInt(parts[0]);
            if (currentRoomNumber == roomNumber) {
                double price = Double.parseDouble(parts[2]);
                bookedRoom = new Room(currentRoomNumber, parts[1], price);
                break;
            }
        }

        Guest bookingGuest = null;
        for (String g : guests) {
            String[] parts = g.split(",");
            if (parts[0].equals(firstName) && parts[1].equals(lastName)) {
                bookingGuest = new Guest(parts[0], parts[1], parts[2]);
                break;
            }
        }

        if (bookedRoom != null && bookingGuest != null) {
            Booking booking = new Booking(bookedRoom, bookingGuest, checkInDate, checkOutDate);
            double total = booking.calculateTotal();
            System.out.printf("Total cost for the stay: %.2f%n", total);
        }

        String bookingData = String.join(",", firstName, lastName, String.valueOf(roomNumber), checkInDate.toString(), checkOutDate.toString());
        DataStorage.saveToFile(BOOKINGS_FILE, bookingData);
        System.out.println("Booking successfully added!");
    }

    private static void cancelBooking(Scanner scanner) {
        List<String> bookingLines = DataStorage.loadFromFile("bookings.txt");

        if (bookingLines.isEmpty()) {
            System.out.println("No bookings to cancel.");
            return;
        }

        System.out.print("Enter the room number to cancel booking for: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        List<String> updatedLines = new ArrayList<>();
        boolean cancelled = false;

        for (String line : bookingLines) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                int bookedRoom = Integer.parseInt(parts[2]);

                if (bookedRoom == roomNumber && !cancelled) {
                    cancelled = true;
                    System.out.println("Booking for room " + roomNumber + " cancelled.");
                } else {
                    updatedLines.add(line);
                }
            }
        }

        if (!cancelled) {
            System.out.println("No active booking found for room " + roomNumber + ".");
        } else {
            DataStorage.clearFile("bookings.txt");
            for (String updatedLine : updatedLines) {
                DataStorage.appendToFile("bookings.txt", updatedLine);
            }
        }

        List<String> rooms = DataStorage.loadFromFile(ROOMS_FILE);
        List<String> updatedRooms = new ArrayList<>();

        for (String room : rooms) {
            String[] parts = room.split(",");
            int roomNum = Integer.parseInt(parts[0]);

            if (roomNum == roomNumber) {
                parts[3] = "true";
                updatedRooms.add(String.join(",", parts));
            } else {
                updatedRooms.add(room);
            }
        }

        DataStorage.clearFile(ROOMS_FILE);
        for (String updatedRoom : updatedRooms) {
            DataStorage.appendToFile(ROOMS_FILE, updatedRoom);
        }
    }

    private static void addFacility(Scanner scanner) {
        System.out.print("Enter facility name: ");
        String name = scanner.nextLine();
        System.out.print("Enter facility description: ");
        String description = scanner.nextLine();

        Facility.addFacility(name, description);
        Facility.saveFacilitiesToFile(FACILITIES_FILE);
        System.out.println("Facility added successfully.");
    }

    private static void removeFacility(Scanner scanner) {
        System.out.print("Enter facility name to remove: ");
        String name = scanner.nextLine();

        Facility.removeFacility(name);
        Facility.saveFacilitiesToFile(FACILITIES_FILE);
        System.out.println("Facility removed successfully.");
    }

    private static void viewFacilities(Scanner scanner) {
        List<Facility> facilities = new ArrayList<>(Facility.getFacilities());
        if (facilities.isEmpty()) {
            System.out.println("No facilities available.");
            return;
        }

        System.out.println("Choose sorting option:");
        System.out.println("1. No Sorting");
        System.out.println("2. Sort by Name");
        System.out.println("3. Sort by Description");
        System.out.print("Enter option: ");
        String sortOption = scanner.nextLine();

        switch (sortOption) {
            case "1":
                break;
            case "2":
                facilities.sort(Comparator.comparing(Facility::getName, String.CASE_INSENSITIVE_ORDER));
                break;
            case "3":
                facilities.sort(Comparator.comparing(Facility::getDescription, String.CASE_INSENSITIVE_ORDER));
                break;
            default:
                System.out.println("Invalid sort option. Showing unsorted list.");
        }

        for (Facility facility : facilities) {
            System.out.println(facility);
        }
    }

    private static void syncRoomsFromFile() {
        List<String> lines = DataStorage.loadFromFile(ROOMS_FILE);
        for (String line : lines) {
            Room room = Room.fromString(line);
            HOTEL.addRoom(room);
        }
    }
}
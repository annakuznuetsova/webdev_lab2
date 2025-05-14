package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private RoomService roomService;

    @GetMapping("/book-room")
    public String showBookingForm(Model model) {
        model.addAttribute("guests", guestService.getAllGuests());
        model.addAttribute("rooms", roomService.getAvailableRooms(1));
        return "book-room";
    }

    @PostMapping("/book-room")
    public String bookRoom(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam int roomNumber,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                           Model model) {

        if (!checkOut.isAfter(checkIn)) {
            model.addAttribute("error", "Check-out date must be after check-in date.");
            model.addAttribute("guests", guestService.getAllGuests());
            model.addAttribute("rooms", roomService.getAvailableRooms(1));
            return "book-room";
        }

        String error = bookingService.bookRoom(firstName, lastName, roomNumber, checkIn, checkOut);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("guests", guestService.getAllGuests());
            model.addAttribute("rooms", roomService.getAvailableRooms(1));
            return "book-room";
        }

        return "redirect:/";
    }

    @GetMapping("/bookings/cancel")
    public String showCancelForm() {
        return "cancel-booking";
    }

    @PostMapping("/bookings/cancel")
    public String cancelBooking(@RequestParam int roomNumber, Model model) {
        boolean success = bookingService.cancelBooking(roomNumber);

        if (success) {
            model.addAttribute("message", "Booking cancelled successfully.");
        } else {
            model.addAttribute("error", "Failed to cancel booking. Room may not be booked.");
        }

        return "cancel-booking";
    }

    @GetMapping("/bookings")
    public String viewAllBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "bookings";
    }
}

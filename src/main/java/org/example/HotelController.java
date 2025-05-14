package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HotelController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private GuestService guestService;

    @GetMapping("/hotel-info")
    public String showHotelInfo(Model model) {
        model.addAttribute("roomCount", roomService.getAllRooms().size());
        model.addAttribute("guestCount", guestService.getAllGuests().size());
        model.addAttribute("bookedCount", roomService.getBookedRooms(1).size()); // sorted by room number

        return "hotel-info";
    }
}

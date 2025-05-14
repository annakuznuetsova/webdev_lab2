package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired private RoomService roomService;

    @GetMapping
    public String viewAllRooms(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "view-rooms";     
    }

    @GetMapping("/add")
    public String showAddForm() {
        return "add-room";
    }

    @PostMapping("/add")
    public String addRoom(@RequestParam int number,
                          @RequestParam String type,
                          @RequestParam double price,
                          RedirectAttributes redirectAttributes) {

        String error = roomService.addRoom(number, type, price);
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/rooms/add";
        }

        return "redirect:/rooms";
    }

    @GetMapping("/edit-lookup")
    public String showEditLookupPage() {
        return "edit-room-lookup";
    }

    @GetMapping("/edit")
    public String showRoomEditSelector() {
        return "edit-room-number";
    }

    @PostMapping("/edit")
    public String editRoom(@RequestParam int number,
                           @RequestParam String type,
                           @RequestParam double price,
                           Model model) {
        boolean ok = roomService.editRoom(number, type, price);
        if (!ok) {
            model.addAttribute("error", "Cannot edit room. It may be booked or input is invalid.");
            model.addAttribute("room", new Room(number, type, price));
            return "edit-room";
        }
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{number}")
    public String showEditForm(@PathVariable int number, Model model, RedirectAttributes redirectAttributes) {
        Room room = roomService.getAllRooms()
                .stream()
                .filter(r -> r.getNumber() == number)
                .findFirst()
                .orElse(null);

        if (room == null) {
            redirectAttributes.addFlashAttribute("error", "Room not found.");
            return "redirect:/rooms/edit-room-form";
        }

        model.addAttribute("room", room);
        return "edit-room"; 
    }

    @PostMapping("/check-edit")
    public String checkRoomToEdit(@RequestParam int number, RedirectAttributes redirectAttributes) {
        Room room = roomService.getAllRooms()
                .stream()
                .filter(r -> r.getNumber() == number)
                .findFirst()
                .orElse(null);

        if (room == null) {
            redirectAttributes.addFlashAttribute("error", "Room number " + number + " not found.");
            return "redirect:/rooms/edit";
        }

        return "redirect:/rooms/edit/" + number;
    }

    @GetMapping("/edit-room-form")
    public String showEditLookupForm() {
        return "edit-room-lookup"; 
    }

    @PostMapping("/edit-room-form")
    public String redirectToEdit(@RequestParam int number) {
        return "redirect:/rooms/edit/" + number;
    }

    @PostMapping("/remove/{number}")
    public String removeRoom(@PathVariable int number, RedirectAttributes redirectAttributes) {
        String result = roomService.removeRoom(number);

        if (result != null) {
            redirectAttributes.addFlashAttribute("error", result);
        } else {
            redirectAttributes.addFlashAttribute("message", "Room removed successfully.");
        }

        return "redirect:/rooms";
    }

    @GetMapping("/remove")
    public String showRemoveForm() {
        return "remove-room"; 
    }

    @PostMapping("/remove")
    public String removeRoomViaForm(@RequestParam int number, RedirectAttributes redirectAttributes) {
        if (number < 1) {
            redirectAttributes.addFlashAttribute("error", "Room number must be positive.");
            return "redirect:/rooms/remove";
        }

        String result = roomService.removeRoom(number);
        if (result != null) {
            redirectAttributes.addFlashAttribute("error", result);
        } else {
            redirectAttributes.addFlashAttribute("message", "Room removed successfully.");
        }

        return "redirect:/rooms/remove";
    }
}

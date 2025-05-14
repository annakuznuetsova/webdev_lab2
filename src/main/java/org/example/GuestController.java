package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class GuestController {

    @Autowired
    private GuestService guestService;

    @GetMapping("/guests")
    public String viewGuests(Model model) {
        List<Guest> guests = guestService.getAllGuests();
        model.addAttribute("guests", guests);
        return "guests"; 
    }

    @GetMapping("/guests/add")
    public String showAddGuestForm() {
        return "add-guest";
    }

    @PostMapping("/guests/add")
    public String addGuest(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String contact,
                           @RequestParam double discount,
                           @RequestParam String status,
                           Model model) {

        boolean success = guestService.addGuest(firstName, lastName, contact, discount, status);
        if (!success) {
            model.addAttribute("error", "Invalid guest data.");
            return "add-guest";
        }
        return "redirect:/guests";
    }

    @Autowired
    private GuestDAO guestDAO;

    @GetMapping("/guests/update-contact")
    public String showUpdateContactForm() {
        return "update-contact"; 
    }

    @PostMapping("/guests/update-contact")
    public String updateContact(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String newContact,
            RedirectAttributes redirectAttributes) {

        boolean updated = guestDAO.updateGuestContact(firstName, lastName, newContact);

        if (updated) {
            redirectAttributes.addFlashAttribute("message", "Contact updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Guest not found.");
        }

        return "redirect:/guests/update-contact";
    }
}

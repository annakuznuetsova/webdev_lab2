package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping("/facilities")
    public String viewFacilities(Model model) {
        model.addAttribute("facilities", facilityService.getAllFacilities());
        model.addAttribute("facilityForm", new FacilityForm());
        return "facilities";
    }

    @GetMapping("/facilities/add")
    public String showAddFacilityForm(Model model) {
        model.addAttribute("facilityForm", new Facility("", ""));
        return "add-facility";
    }

    @PostMapping("/facilities/add")
    public String submitAddForm(@ModelAttribute("facilityForm") @Valid FacilityForm form,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add-facility";
        }

        boolean added = facilityService.addFacility(form.getName(), form.getDescription());
        if (added) {
            redirectAttributes.addFlashAttribute("success", "Facility added successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Facility already exists.");
        }

        return "redirect:/facilities";
    }

    @GetMapping("/facilities/remove")
    public String showRemoveFacilityPage(Model model) {
        List<Facility> facilities = facilityService.getAllFacilities();
        model.addAttribute("facilities", facilities);
        return "remove-facility";
    }

    @PostMapping("/facilities/remove")
    public String removeFacility(@RequestParam int id, RedirectAttributes redirectAttributes) {
        boolean removed = facilityService.removeFacility(id);

        if (!removed) {
            redirectAttributes.addFlashAttribute("error", "Could not remove facility. It may not exist.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Facility removed successfully.");
        }

        return "redirect:/facilities/remove";
    }
}

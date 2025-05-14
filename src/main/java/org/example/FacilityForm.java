package org.example;

import jakarta.validation.constraints.NotBlank;

public class FacilityForm {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    public FacilityForm() {
    }

    public FacilityForm(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Facility toFacility() {
        return new Facility(name, description);
    }
}

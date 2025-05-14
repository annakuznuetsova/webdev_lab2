package org.example;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FacilityService {

    private final FacilityDAO facilityDAO;

    public FacilityService(FacilityDAO facilityDAO) {
        this.facilityDAO = facilityDAO;
    }

    public boolean addFacility(String name, String description) {
        List<Facility> existing = facilityDAO.getAllFacilities();
        boolean alreadyExists = existing.stream()
                .anyMatch(f -> f.getName().equalsIgnoreCase(name));

        if (alreadyExists) {
            System.out.println("Facility already exists.");
            return false;
        }

        Facility facility = new Facility(name, description);
        facilityDAO.addFacility(facility);
        return true;
    }

    public boolean removeFacility(String name) {
        return facilityDAO.removeFacility(name);
    }

    public List<Facility> getAllSorted(int sortOption) {
        List<Facility> facilities = facilityDAO.getAllFacilities();

        return switch (sortOption) {
            case 2 -> facilities.stream().sorted(Comparator.comparing(Facility::getName, String.CASE_INSENSITIVE_ORDER)).toList();
            case 3 -> facilities.stream().sorted(Comparator.comparing(Facility::getDescription, String.CASE_INSENSITIVE_ORDER)).toList();
            default -> facilities;
        };
    }

    public List<Facility> getAllFacilities() {
        return facilityDAO.getAllFacilities();
    }
}
package org.example;
import java.util.ArrayList;
import java.util.List;

public class Facility {
    private final String name;
    private final String description;
    private static final List<Facility> facilities = new ArrayList<>();

    public Facility(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public static void addFacility(String name, String description) {
        Facility newFacility = new Facility(name, description);
        if (!facilities.contains(newFacility)) {
            facilities.add(newFacility);
        } else {
            System.out.println("Facility already exists.");
        }
    }

    public static List<Facility> getFacilities() {
        return facilities;
    }

    public static void removeFacility(String name) {
        boolean removed = facilities.removeIf(facility -> facility.getName().equalsIgnoreCase(name));
        if (removed) {
            System.out.println("Facility removed successfully.");
        } else {
            System.out.println("Facility not found.");
        }
    }

    public static void saveFacilitiesToFile(String filename) {
        DataStorage.clearFile(filename);
        for (Facility facility : facilities) {
            String line = facility.getName() + "," + facility.getDescription();
            DataStorage.appendToFile(filename, line);
        }
    }

    public static void loadFacilitiesFromFile(String filename) {
        facilities.clear();
        List<String> lines = DataStorage.loadFromFile(filename);
        for (String line : lines) {
            String[] parts = line.split(",", 2);
            if (parts.length == 2) {
                addFacility(parts[0], parts[1]);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Facility facility = (Facility) obj;
        return name.equalsIgnoreCase(facility.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return "Facility{" + "name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }
}

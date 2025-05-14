package org.example;
import java.util.Objects;

public class Guest {
    private final String firstName;
    private final String lastName;
    private final String contact;
    private double discount;
    private String status;

    public Guest(String firstName, String lastName, String contact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.discount = 0.0;
        this.status = "Regular";
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getContact() { return contact; }
    public double getDiscount() { return discount; }
    public String getStatus() { return status; }

    public void setDiscount(double discount) {
        if (discount >= 0 && discount <= 100) {
            this.discount = discount;
        } else {
            System.out.println("Invalid discount value. It must be between 0 and 100.");
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toFileString() {
        return firstName + "," + lastName + "," + contact + "," + discount + "," + status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Objects.equals(firstName, guest.firstName) &&
                Objects.equals(lastName, guest.lastName) &&
                Objects.equals(contact, guest.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, contact);
    }

    @Override
    public String toString() {
        return "Guest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contact='" + contact + '\'' +
                ", discount=" + discount +
                ", status=" + status +
                '}';
    }
}

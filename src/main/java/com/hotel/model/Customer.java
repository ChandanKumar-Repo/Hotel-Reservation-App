package com.hotel.model;
import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;

    // Requirements: RegEx validation for name@domain.com
    private static final String EMAIL_REGEX = "^(.+)@(.+)\\.com$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public Customer(String firstName, String lastName, String email) {
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format! Must match 'name@domain.com'");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getEmail() { return this.email; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Customer: " + firstName + " " + lastName + " (" + email + ")";
    }
}

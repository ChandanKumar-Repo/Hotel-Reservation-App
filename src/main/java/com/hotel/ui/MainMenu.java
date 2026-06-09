package com.hotel.ui;

import com.hotel.resource.HotelResource;
import com.hotel.model.Customer;
import com.hotel.model.IRoom;
import com.hotel.model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final String DATE_FORMAT = "MM/dd/yyyy";

    public static void showMainMenu() {
        Scanner scanner = new Scanner(System.in); // Fixed System.in issue here as well!
        boolean keepRunning = true;

        while (keepRunning) {
            try {
                System.out.println("\n=========================================");
                System.out.println("WELCOME TO THE HOTEL RESERVATION APP");
                System.out.println("=========================================");
                System.out.println("1. Find and book a room");
                System.out.println("2. See my reservations");
                System.out.println("3. Create an account");
                System.out.println("4. Admin Options");
                System.out.println("5. Exit Application");
                System.out.println("-----------------------------------------");
                System.out.print("Please select a number menu option: ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        findAndBookRoom(scanner);
                        break;
                    case "2":
                        seeMyReservations(scanner);
                        break;
                    case "3":
                        createAccount(scanner);
                        break;
                    case "4":
                        AdminMenu.showAdminMenu(scanner);
                        break;
                    case "5":
                        keepRunning = false;
                        System.out.println("Thank you for using the application! Goodbye.");
                        break;
                    default:
                        System.out.println("Invalid option. Please enter a valid menu number (1-5).");
                }
            } catch (Exception e) {
                System.out.println("An unhandled system error occurred: " + e.getLocalizedMessage());
            }
        }
        scanner.close();
    }

    private static void findAndBookRoom(Scanner scanner) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setLenient(false);

        try {
            System.out.print("Enter Check-In Date (MM/dd/yyyy): ");
            Date checkIn = formatter.parse(scanner.nextLine().trim());
            System.out.print("Enter Check-Out Date (MM/dd/yyyy): ");
            Date checkOut = formatter.parse(scanner.nextLine().trim());

            if (!checkOut.after(checkIn)) {
                System.out.println("Check-out date must occur after check-in date.");
                return;
            }

            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for those specific dates.");

                Calendar calIn = Calendar.getInstance();
                Calendar calOut = Calendar.getInstance();
                calIn.setTime(checkIn);
                calOut.setTime(checkOut);
                calIn.add(Calendar.DAY_OF_YEAR, 7);
                calOut.add(Calendar.DAY_OF_YEAR, 7);

                Date recCheckIn = calIn.getTime();
                Date recCheckOut = calOut.getTime();

                Collection<IRoom> recommendedRooms = hotelResource.findARoom(recCheckIn, recCheckOut);
                if (recommendedRooms.isEmpty()) {
                    System.out.println("No alternative recommended rooms found 7 days out either.");
                    return;
                } else {
                    System.out.println("We found availability on recommended alternative dates: "
                            + formatter.format(recCheckIn) + " - " + formatter.format(recCheckOut));
                    availableRooms = recommendedRooms;
                    checkIn = recCheckIn;
                    checkOut = recCheckOut;
                }
            }

            System.out.println("\nAvailable Rooms:");
            for (IRoom room : availableRooms) {
                System.out.println(room);
            }

            // --- NEW STRICT VALIDATION LOGIC FOR BOOKING ---
            boolean validBookChoice = false;
            while (!validBookChoice) {
                System.out.print("\nWould you like to book a room? (Y/N): ");
                String bookChoice = scanner.nextLine().trim();

                if (bookChoice.equalsIgnoreCase("Y") || bookChoice.equalsIgnoreCase("YES")) {
                    validBookChoice = true;

                    // --- NEW STRICT VALIDATION LOGIC FOR ACCOUNT CHECK ---
                    boolean validAccountCheck = false;
                    while (!validAccountCheck) {
                        System.out.print("Do you have an account with us? (Y/N): ");
                        String dynamicAccountCheck = scanner.nextLine().trim();

                        if (dynamicAccountCheck.equalsIgnoreCase("Y") || dynamicAccountCheck.equalsIgnoreCase("YES")) {
                            validAccountCheck = true;

                            System.out.print("Enter your email address: ");
                            String email = scanner.nextLine().trim();
                            Customer customer = hotelResource.getCustomer(email);

                            if (customer == null) {
                                System.out.println("No matching customer account found. Bookings require account creation first.");
                                return;
                            }

                            System.out.print("What room number would you like to reserve?: ");
                            String roomNumber = scanner.nextLine().trim();
                            IRoom roomToBook = hotelResource.getRoom(roomNumber);

                            if (roomToBook == null || !availableRooms.contains(roomToBook)) {
                                System.out.println("That room is unavailable or does not exist.");
                                return;
                            }

                            Reservation res = hotelResource.bookARoom(email, roomToBook, checkIn, checkOut);
                            System.out.println("\nReservation Successful!");
                            System.out.println(res);

                        } else if (dynamicAccountCheck.equalsIgnoreCase("N") || dynamicAccountCheck.equalsIgnoreCase("NO")) {
                            validAccountCheck = true;
                            System.out.println("Please create an account first using Menu Option 3.");
                            return;
                        } else {
                            System.out.println("Invalid input. Please explicitly enter Y or N.");
                        }
                    }

                } else if (bookChoice.equalsIgnoreCase("N") || bookChoice.equalsIgnoreCase("NO")) {
                    validBookChoice = true;
                    System.out.println("Returning to Main Menu...");
                } else {
                    System.out.println("Invalid input. Please explicitly enter Y or N.");
                }
            }

        } catch (ParseException e) {
            System.out.println("Invalid date format. Please explicitly use MM/dd/yyyy.");
        }
    }

    private static void seeMyReservations(Scanner scanner) {
        System.out.print("Enter your account email: ");
        String email = scanner.nextLine().trim();
        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);

        if (reservations.isEmpty()) {
            System.out.println("No history accounts or explicit records found under that customer identifier.");
        } else {
            for (Reservation res : reservations) {
                System.out.println(res);
            }
        }
    }

    private static void createAccount(Scanner scanner) {
        System.out.print("Enter your email address (name@domain.com): ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter your first name: ");
        String first = scanner.nextLine().trim();
        System.out.print("Enter your last name: ");
        String last = scanner.nextLine().trim();

        try {
            hotelResource.createACustomer(email, first, last);
            System.out.println("Account created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
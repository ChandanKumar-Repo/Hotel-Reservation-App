package com.hotel.ui;

import com.hotel.resource.AdminResource;
import com.hotel.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();

    public static void showAdminMenu(Scanner scanner) {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                System.out.println("\n-----------------------------------------");
                System.out.println("Admin Menu");
                System.out.println("-----------------------------------------");
                System.out.println("1. See all Customers");
                System.out.println("2. See all Rooms");
                System.out.println("3. See all Reservations");
                System.out.println("4. Add a Room");
                System.out.println("5. Back to Main Menu");
                System.out.print("Please select a number menu option: ");

                String input = scanner.nextLine().trim();
                switch (input) {
                    case "1":
                        displayAllCustomers();
                        break;
                    case "2":
                        displayAllRooms();
                        break;
                    case "3":
                        adminResource.displayAllReservations();
                        break;
                    case "4":
                        addRoom(scanner);
                        break;
                    case "5":
                        keepRunning = false;
                        break;
                    default:
                        System.out.println("Invalid selection. Please input a number from 1 to 5.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected structural error occurred: " + e.getLocalizedMessage());
            }
        }
    }

    private static void displayAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No registered customers found.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }

    private static void displayAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms found in the hotel registry.");
        } else {
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
    }

    private static void addRoom(Scanner scanner) {
        List<IRoom> newRooms = new ArrayList<>();
        boolean addingRooms = true;

        while (addingRooms) {
            try {
                System.out.print("Enter room number: ");
                String roomNumber = scanner.nextLine().trim();

                System.out.print("Enter price per night: ");
                double price = Double.parseDouble(scanner.nextLine().trim());

                System.out.print("Enter room type (1 for SINGLE, 2 for DOUBLE): ");
                String typeInput = scanner.nextLine().trim();
                RoomType roomType;
                if (typeInput.equals("1")) {
                    roomType = RoomType.SINGLE;
                } else if (typeInput.equals("2")) {
                    roomType = RoomType.DOUBLE;
                } else {
                    throw new IllegalArgumentException("Invalid selection choice. Please enter 1 or 2.");
                }

                IRoom room;
                if (price == 0.0) {
                    room = new FreeRoom(roomNumber, roomType);
                } else {
                    room = new Room(roomNumber, price, roomType);
                }

                newRooms.add(room);

                // --- NEW STRICT VALIDATION LOGIC ---
                boolean validChoice = false;
                while (!validChoice) {
                    System.out.print("Would you like to add another room? (Y/N): ");
                    String choice = scanner.nextLine().trim();

                    if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("YES")) {
                        validChoice = true; // Breaks this inner loop, restarts addingRooms loop
                    } else if (choice.equalsIgnoreCase("N") || choice.equalsIgnoreCase("NO")) {
                        validChoice = true;
                        addingRooms = false; // Breaks both loops
                    } else {
                        System.out.println("Invalid input. Please explicitly enter Y or N.");
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid number format provided for room price. Please try again.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        adminResource.addRoom(newRooms);
        System.out.println("Rooms added successfully!");
    }
}
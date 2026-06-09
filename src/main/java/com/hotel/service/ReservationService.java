package com.hotel.service;

import com.hotel.model.Customer;
import com.hotel.model.IRoom;
import com.hotel.model.Reservation;

import java.util.*;

public class ReservationService {
    private static final ReservationService INSTANCE = new ReservationService();

    private final Set<IRoom> rooms = new HashSet<>();
    private final Set<Reservation> reservations = new HashSet<>();

    private ReservationService() {}

    public static ReservationService getInstance() {
        return INSTANCE;
    }

    public void addRoom(IRoom room) {
        rooms.add(room);
    }

    public IRoom getARoom(String roomId) {
        for (IRoom room : rooms) {
            if (room.getRoomNumber().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    public Collection<IRoom> getAllRooms() {
        return rooms;
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    // Requirements: Avoid conflicting reservations + dynamic lookup logic
    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        List<IRoom> availableRooms = new ArrayList<>();
        for (IRoom room : rooms) {
            if (!isRoomReserved(room, checkInDate, checkOutDate)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    private boolean isRoomReserved(IRoom room, Date checkInDate, Date checkOutDate) {
        for (Reservation res : reservations) {
            if (res.getRoom().equals(room)) {
                // If the targeted date window overlaps with an active reservation window
                if (checkInDate.before(res.getCheckOutDate()) && checkOutDate.after(res.getCheckInDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        List<Reservation> customerReservations = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getCustomer().equals(customer)) {
                customerReservations.add(res);
            }
        }
        return customerReservations;
    }

    public void printAllReservation() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found in the system.");
        } else {
            for (Reservation res : reservations) {
                System.out.println(res);
            }
        }
    }
}
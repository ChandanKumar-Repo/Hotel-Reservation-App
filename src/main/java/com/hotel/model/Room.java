package com.hotel.model;
import java.util.Objects;

public class Room implements IRoom{
    // Requirements: Use final for data model class variables
    private final String roomNumber;
    private final Double price;
    private final RoomType roomType;

    public Room(String roomNumber, Double price, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() { return this.roomNumber; }

    @Override
    public Double getRoomPrice() { return this.price; }

    @Override
    public RoomType getRoomType() { return this.roomType; }

    @Override
    public boolean isFree() {
        return this.price != null && this.price == 0.0;
    }

    // Requirements: Override hashCode and equals to utilize Collection functions
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomNumber, room.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }

    // Requirements: Override toString method
    @Override
    public String toString() {
        return "Room Number: " + roomNumber + " | Type: " + roomType + " | Price: $" + price;
    }
}

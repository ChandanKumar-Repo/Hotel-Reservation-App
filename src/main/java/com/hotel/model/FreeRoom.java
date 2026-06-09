package com.hotel.model;

public class FreeRoom extends Room {
    public FreeRoom(String roomNumber, RoomType roomType) {
        super(roomNumber, 0.0, roomType);
    }

    @Override
    public boolean isFree() {
        return true;
    }

    @Override
    public String toString() {
        return "Room Number: " + getRoomNumber() + " | Type: " + getRoomType() + " | Price: Free";
    }
}

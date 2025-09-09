package com.hamlet.HamletHotel.service;

import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.response.Response;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    Response addNewRoom(RoomRequest roomRequest);
    List<String> getAllRoomTypes();
    Response getAllRoom();
    Response deleteRoom(Long roomId );
    Response updateRoom(Long roomId, RoomRequest roomRequest);
    Response getRoomById(Long roomId);
    Response getAvailableRoomByDateAndType(java.time.LocalDate checkInDate, LocalDate checkOutDate, String roomType);
    Response getAllAvailableRooms();
}

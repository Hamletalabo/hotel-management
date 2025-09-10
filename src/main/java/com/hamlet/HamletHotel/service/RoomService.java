package com.hamlet.HamletHotel.service;

import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.payload.response.Response;
import com.hamlet.HamletHotel.payload.response.RoomListResponse;
import com.hamlet.HamletHotel.payload.response.RoomResponse;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    RoomResponse addNewRoom(RoomRequest roomRequest);
    List<String> getAllRoomTypes();
    RoomListResponse getAllRooms();
    ApiResponse deleteRoom(Long roomId);
    RoomResponse updateRoom(Long roomId, RoomRequest roomRequest);
    RoomResponse getRoomById(Long roomId);
    RoomListResponse getAvailableRoomByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
    RoomListResponse getAllAvailableRooms();
}

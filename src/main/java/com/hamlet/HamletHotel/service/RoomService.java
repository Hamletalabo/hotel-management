package com.hamlet.HamletHotel.service;

import com.hamlet.HamletHotel.entity.Room;
import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import org.joda.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface RoomService {
    ApiResponse addNewRoom(RoomRequest roomRequest);
    List<Room> getAllRoom();
    ApiResponse deleteRoom(Long roomId );
    ApiResponse updateRoom(RoomRequest roomRequest);
    ApiResponse getRoomById(Long roomId);
    ApiResponse getAvailableRoomByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
    ApiResponse getAllAvailableRooms();
}

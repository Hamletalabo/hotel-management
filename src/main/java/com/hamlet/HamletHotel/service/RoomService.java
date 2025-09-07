package com.hamlet.HamletHotel.service;

import com.hamlet.HamletHotel.entity.Room;
import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    ApiResponse addNewRoom(RoomRequest roomRequest);
    List<String> getAllRoomTypes();
    ApiResponse getAllRoom();
    ApiResponse deleteRoom(Long roomId );
    ApiResponse updateRoom(Long roomId,RoomRequest roomRequest);
    ApiResponse getRoomById(Long roomId);
    ApiResponse getAvailableRoomByDateAndType(java.time.LocalDate checkInDate, LocalDate checkOutDate, String roomType);
    ApiResponse getAllAvailableRooms();
}

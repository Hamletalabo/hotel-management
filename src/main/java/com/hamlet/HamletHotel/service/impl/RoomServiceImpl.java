package com.hamlet.HamletHotel.service.impl;

import com.hamlet.HamletHotel.entity.Room;
import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    @Override
    public ApiResponse addNewRoom(RoomRequest roomRequest) {
        return null;
    }

    @Override
    public List<Room> getAllRoom() {
        return List.of();
    }

    @Override
    public ApiResponse deleteRoom(Long roomId) {
        return null;
    }

    @Override
    public ApiResponse updateRoom(RoomRequest roomRequest) {
        return null;
    }

    @Override
    public ApiResponse getRoomById(Long roomId) {
        return null;
    }

    @Override
    public ApiResponse getAvailableRoomByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return null;
    }

    @Override
    public ApiResponse getAllAvailableRooms() {
        return null;
    }
}

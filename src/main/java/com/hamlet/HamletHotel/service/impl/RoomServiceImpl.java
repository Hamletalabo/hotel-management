package com.hamlet.HamletHotel.service.impl;

import com.hamlet.HamletHotel.entity.Room;
import com.hamlet.HamletHotel.exception.NotFoundException;
import com.hamlet.HamletHotel.exception.UnableToUploadImageException;
import com.hamlet.HamletHotel.exception.UnauthenticatedException;
import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.response.*;
import com.hamlet.HamletHotel.repository.BookingRepository;
import com.hamlet.HamletHotel.repository.RoomRepository;
import com.hamlet.HamletHotel.service.AwsS3Service;
import com.hamlet.HamletHotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final AwsS3Service awsS3Service;


    @Override
    public RoomResponse addNewRoom(RoomRequest roomRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthenticatedException("User is not authenticated. Please log in first.");
        }

        String photoUrl = awsS3Service.saveImageToS3(roomRequest.getRoomPhoto());

        Room room = Room.builder()
                .roomType(roomRequest.getRoomType())
                .roomPrice(roomRequest.getRoomPrice())
                .roomDescription(roomRequest.getRoomDescription())
                .roomPhotoUrl(photoUrl)
                .build();

        Room savedRoom = roomRepository.save(room);

        return RoomResponse.builder()
                .responseCode(200)
                .responseMessage("Room created successfully")
                .roomInfo(mapToRoomInfo(savedRoom))
                .build();
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public RoomListResponse getAllRooms() {
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<RoomInfo> roomInfos = rooms.stream()
                .map(this::mapToRoomInfo)
                .toList();

        return RoomListResponse.builder()
                .responseCode(200)
                .responseMessage("Rooms retrieved successfully")
                .rooms(roomInfos)
                .build();
    }

    @Override
    public ApiResponse deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found with ID: " + roomId));

        if (room.getRoomPhotoUrl() != null) {
            awsS3Service.deleteFile(room.getRoomPhotoUrl());
        }

        roomRepository.delete(room);

        return ApiResponse.builder()
                .responseCode(200)
                .responseMessage("Room deleted successfully")
                .build();
    }

    @Override
    public RoomResponse updateRoom(Long roomId, RoomRequest roomRequest) {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        existingRoom.setRoomType(roomRequest.getRoomType());
        existingRoom.setRoomPrice(roomRequest.getRoomPrice());
        existingRoom.setRoomDescription(roomRequest.getRoomDescription());

        // Replace photo if a new one is provided
        if (roomRequest.getRoomPhoto() != null) {
            if (existingRoom.getRoomPhotoUrl() != null) {
                try {
                    awsS3Service.deleteFile(existingRoom.getRoomPhotoUrl());
                } catch (UnableToUploadImageException e) {
                    throw new UnableToUploadImageException("An error occurred while uploading photo. Please try again.");
                }
            }
            String newPhotoUrl = awsS3Service.saveImageToS3(roomRequest.getRoomPhoto());
            existingRoom.setRoomPhotoUrl(newPhotoUrl);
        }

        Room updatedRoom = roomRepository.save(existingRoom);

        return RoomResponse.builder()
                .responseCode(200)
                .responseMessage("Room updated successfully")
                .roomInfo(mapToRoomInfo(updatedRoom))
                .build();
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found with ID: " + roomId));

        return RoomResponse.builder()
                .responseCode(200)
                .responseMessage("Room retrieved successfully")
                .roomInfo(mapToRoomInfo(room))
                .build();
    }

    @Override
    public RoomListResponse getAvailableRoomByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);

        List<RoomInfo> roomInfos = availableRooms.stream()
                .map(this::mapToRoomInfo)
                .toList();

        return RoomListResponse.builder()
                .responseCode(200)
                .responseMessage("Available rooms retrieved successfully")
                .rooms(roomInfos)
                .build();
    }

    @Override
    public RoomListResponse getAllAvailableRooms() {
        List<Room> availableRooms = roomRepository.getAllAvailableRooms();

        List<RoomInfo> roomInfos = availableRooms.stream()
                .map(this::mapToRoomInfo)
                .toList();

        return RoomListResponse.builder()
                .responseCode(200)
                .responseMessage("All available rooms retrieved successfully")
                .rooms(roomInfos)
                .build();
    }

    private RoomInfo mapToRoomInfo(Room room) {
        return RoomInfo.builder()
                .id(room.getId())
                .roomType(room.getRoomType())
                .roomPrice(room.getRoomPrice())
                .roomDescription(room.getRoomDescription())
                .roomPhotoUrl(room.getRoomPhotoUrl())
                .build();
    }
}
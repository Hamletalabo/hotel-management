package com.hamlet.HamletHotel.service.impl;

import com.hamlet.HamletHotel.entity.Room;
import com.hamlet.HamletHotel.exception.NotFoundException;
import com.hamlet.HamletHotel.exception.UnableToUploadImageException;
import com.hamlet.HamletHotel.exception.UnauthenticatedException;
import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.response.Response;
import com.hamlet.HamletHotel.repository.BookingRepository;
import com.hamlet.HamletHotel.repository.RoomRepository;
import com.hamlet.HamletHotel.service.AwsS3Service;
import com.hamlet.HamletHotel.service.RoomService;
import com.hamlet.HamletHotel.utils.Utils;
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
    public Response addNewRoom(RoomRequest roomRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthenticatedException("User is not authenticated. Please log in first.");
        }

        Room room = Room.builder()
                .roomType(roomRequest.getRoomType())
                .roomPrice(roomRequest.getRoomPrice())
                .roomPhotoUrl(roomRequest.getRoomPhotoUrl())
                .roomDescription(roomRequest.getRoomDescription())
                .build();

        Room savedRoom = roomRepository.save(room);

        return Response.builder()
                .responseCode(200)
                .responseMessage("Room added successfully")
                .room(Utils.mapRoomEntityToRoomRequest(savedRoom))
                .build();
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRoom() {
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<RoomRequest> roomRequests = Utils.mapRoomListEntityToRoomListRequest(rooms);

        return Response.builder()
                .responseCode(200)
                .responseMessage("Rooms retrieved successfully")
                .roomList(roomRequests)
                .build();
    }


    @Override
    public Response deleteRoom(Long roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found with ID: " + roomId));

        if (room.getRoomPhotoUrl() != null) {
            awsS3Service.deleteFile(room.getRoomPhotoUrl());
        }

        roomRepository.delete(room);

        return Response.builder()
                .responseCode(200)
                .responseMessage("Room deleted successfully")
                .build();
    }

    @Override
    public Response updateRoom(Long roomId, RoomRequest roomRequest) {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        existingRoom.setRoomType(roomRequest.getRoomType());
        existingRoom.setRoomPrice(roomRequest.getRoomPrice());
        existingRoom.setRoomDescription(roomRequest.getRoomDescription());

        // If new photo is provided, replace the old one
        if (roomRequest.getRoomPhotoUrl() != null && !roomRequest.getRoomPhotoUrl().equals(existingRoom.getRoomPhotoUrl())) {
            if (existingRoom.getRoomPhotoUrl() != null) {
                try {
                    awsS3Service.deleteFile(existingRoom.getRoomPhotoUrl());
                } catch (UnableToUploadImageException e) {
                    throw new UnableToUploadImageException("An error occurred while uploading photo. Please try again.");
                }
            }
            existingRoom.setRoomPhotoUrl(roomRequest.getRoomPhotoUrl());
        }

        Room updatedRoom = roomRepository.save(existingRoom);

        return Response.builder()
                .responseCode(200)
                .responseMessage("Room updated successfully")
                .room(Utils.mapRoomEntityToRoomRequest(updatedRoom))
                .build();
    }

    @Override
    public Response getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found with ID: " + roomId));

        return Response.builder()
                .responseCode(200)
                .room( Utils.mapRoomEntityToRoomRequestPlusBookings(room))
                .build();
    }

    @Override
    public Response getAvailableRoomByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);

        return Response.builder()
                .responseCode(200)
                .responseMessage("All available rooms")
                .roomList(Utils.mapRoomListEntityToRoomListRequest(availableRooms))
                .build();
    }

    @Override
    public Response getAllAvailableRooms() {
        List<Room> availableRooms = roomRepository.getAllAvailableRooms();

        return Response.builder()
                .responseCode(200)
                .responseMessage("All available rooms")
                .roomList(Utils.mapRoomListEntityToRoomListRequest(availableRooms))
                .build();
    }
}

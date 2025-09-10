package com.hamlet.HamletHotel.controller;

import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.response.*;
import com.hamlet.HamletHotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping(value = "/add-room", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomResponse> addNewRoom( RoomRequest roomRequest) {
        if (roomRequest.getRoomPhoto() == null || roomRequest.getRoomPhoto().isEmpty() ||
                roomRequest.getRoomType() == null || roomRequest.getRoomType().isBlank() ||
                roomRequest.getRoomPrice() == null) {
            return ResponseEntity.badRequest().body(
                    RoomResponse.builder()
                            .responseCode(400)
                            .responseMessage("Please provide values for all fields (photo, roomType, roomPrice)")
                            .build()
            );
        }
        RoomResponse response = roomService.addNewRoom(roomRequest);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<RoomListResponse> getAllRooms() {
        RoomListResponse response = roomService.getAllRooms();
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getRoomTypes() {
        List<String> response = roomService.getAllRoomTypes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId) {
        RoomResponse response = roomService.getRoomById(roomId);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<RoomListResponse> getAvailableRooms() {
        RoomListResponse response = roomService.getAllAvailableRooms();
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<RoomListResponse> getAvailableRoomsByDateAndType(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam String roomType) {

        if (checkInDate == null || checkOutDate == null || roomType == null || roomType.isBlank()) {
            return ResponseEntity.badRequest().body(
                    RoomListResponse.builder()
                            .responseCode(400)
                            .responseMessage("Please provide values for all fields (checkInDate, checkOutDate, roomType)")
                            .build()
            );
        }

        RoomListResponse response = roomService.getAvailableRoomByDateAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @PutMapping("/update-room/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestBody RoomRequest roomRequest) {
        RoomResponse response = roomService.updateRoom(roomId, roomRequest);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteRoom(@PathVariable Long roomId) {
        ApiResponse response = roomService.deleteRoom(roomId);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }
}
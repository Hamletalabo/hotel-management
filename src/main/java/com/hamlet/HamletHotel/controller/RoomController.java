package com.hamlet.HamletHotel.controller;

import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.service.BookingService;
import com.hamlet.HamletHotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final BookingService bookingService;

    @PostMapping("/add-room")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> addNewRoom(RoomRequest roomRequest){
        if (roomRequest.getRoomPhotoUrl() ==null || roomRequest.getRoomPhotoUrl().isEmpty() || roomRequest.getRoomType() == null ||
                roomRequest.getRoomType().isEmpty() || roomRequest.getRoomPrice() == null || roomRequest.getRoomType().isBlank()){
            ApiResponse response = new ApiResponse();
            response.setResponseCode(400);
            response.setResponseMessage("please provide values for all fields(photo, roomType, roomPrice");
            return  ResponseEntity.status(response.getResponseCode()).body(response);
        }
        ApiResponse response = roomService.addNewRoom(roomRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllRooms(){
        ApiResponse response = roomService.getAllRoom();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getRoomTypes(){
        List<String> response = roomService.getAllRoomTypes();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<ApiResponse> getRoomById(@PathVariable Long roomId){
        ApiResponse response = roomService.getRoomById(roomId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<ApiResponse> getAvailableRooms(){
        ApiResponse response = roomService.getAllAvailableRooms();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<ApiResponse> getAvailableRoomsByDateAndType(
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate checkOutDate,
            @RequestParam(required = false)String roomType){
        if (checkInDate == null || roomType == null || roomType.isBlank() || checkOutDate == null){
            ApiResponse response = new ApiResponse();
            response.setResponseCode(400);
            response.setResponseMessage("Please provide value for all fields (checkInDate, checkOutDate, roomType");
            return ResponseEntity.status(response.getResponseCode()).body(response);
        }
        ApiResponse response = roomService.getAvailableRoomByDateAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.ok(response);
    }
}

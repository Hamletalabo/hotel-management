package com.hamlet.HamletHotel.controller;

import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.response.Response;
import com.hamlet.HamletHotel.service.BookingService;
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
    private final BookingService bookingService;

    @PostMapping("/add-room")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewRoom(RoomRequest roomRequest){
        if (roomRequest.getRoomPhotoUrl() ==null || roomRequest.getRoomPhotoUrl().isEmpty() || roomRequest.getRoomType() == null ||
                roomRequest.getRoomType().isEmpty() || roomRequest.getRoomPrice() == null || roomRequest.getRoomType().isBlank()){
            Response response = new Response();
            response.setResponseCode(400);
            response.setResponseMessage("please provide values for all fields(photo, roomType, roomPrice");
            return  ResponseEntity.status(response.getResponseCode()).body(response);
        }
        Response response = roomService.addNewRoom(roomRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms(){
        Response response = roomService.getAllRoom();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getRoomTypes(){
        List<String> response = roomService.getAllRoomTypes();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<Response> getRoomById(@PathVariable Long roomId){
        Response response = roomService.getRoomById(roomId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<Response> getAvailableRooms(){
        Response response = roomService.getAllAvailableRooms();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<Response> getAvailableRoomsByDateAndType(
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate checkOutDate,
            @RequestParam(required = false)String roomType){
        if (checkInDate == null || roomType == null || roomType.isBlank() || checkOutDate == null){
            Response response = new Response();
            response.setResponseCode(400);
            response.setResponseMessage("Please provide value for all fields (checkInDate, checkOutDate, roomType");
            return ResponseEntity.status(response.getResponseCode()).body(response);
        }
        Response response = roomService.getAvailableRoomByDateAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-room{roomId}")
    @PreAuthorize("hasAuthority('ADMIN)")
    public ResponseEntity<Response> updateRoom(Long roomId, RoomRequest roomRequest){
        Response response = roomService.updateRoom(roomId, roomRequest);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long roomId){
        Response response = roomService.deleteRoom(roomId);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }
}

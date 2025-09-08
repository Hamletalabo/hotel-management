package com.hamlet.HamletHotel.payload.response;

import com.hamlet.HamletHotel.payload.request.BookingRequest;
import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.request.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {

    private int responseCode;

    private String responseMessage;

    private BookingRequest booking;
    private UserRequest user;
    private RoomRequest room;
    private List<UserRequest> userLists;
    private List<RoomRequest> roomList;
    private List<BookingRequest> bookingList;
}

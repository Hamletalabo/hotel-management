package com.hamlet.HamletHotel.utils;

import com.hamlet.HamletHotel.entity.Booking;
import com.hamlet.HamletHotel.entity.Room;
import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.payload.request.BookingRequest;
import com.hamlet.HamletHotel.payload.request.RoomRequest;
import com.hamlet.HamletHotel.payload.request.UserRequest;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private static final String APHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String confirmationCode(int length){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(APHANUMERIC_STRING.length());
            char randomChar = APHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);

        }
        return stringBuilder.toString();
    }

//    public static UserRequest mapUserEntityToUserRequest(User user){
//        UserRequest userRequest = new UserRequest();
//        userRequest.setId(user.getId());
//        userRequest.setName(user.getName());
//        userRequest.setEmail(user.getEmail());
//        userRequest.setPhoneNumber(user.getPhoneNumber());
//        userRequest.setRole(user.getRoles());
//        return userRequest;
//    }
//
//    public static RoomRequest mapRoomEntityToRoomRequest(Room room){
//        RoomRequest roomRequest = new RoomRequest();
//
//        roomRequest.setRoomType(room.getRoomType());
//        roomRequest.setRoomPrice(room.getRoomPrice());
//        roomRequest.setRoomPhotoUrl(room.getRoomPhotoUrl());
//        roomRequest.setRoomDescription(room.getRoomDescription());
//
//        return roomRequest;
//    }
//
//
//    public static RoomRequest mapRoomEntityToRoomRequestPlusBookings(Room room){
//        RoomRequest roomRequest = mapRoomEntityToRoomRequest(room);
//
//        if (room.getBookings() != null){
//            roomRequest.setBookings(room.getBookings().stream().map(
//                    Utils::mapBookingsEntityBookingRequest
//            ).collect(Collectors.toList()));
//
//        }
//        return roomRequest;
//    }
//
//    public static BookingRequest mapBookingsEntityBookingRequest(Booking booking) {
//        BookingRequest bookingRequest = new BookingRequest();
//        bookingRequest.setCheckInDate(booking.getCheckInDate());
//        bookingRequest.setCheckOutDate(booking.getCheckOutDate());
//        bookingRequest.setNumberOfAdults(booking.getNumberOfAdults());
//        bookingRequest.setNumberOfChildren(booking.getNumberOfChildren());
//        bookingRequest.setTotalNumberOfGuest(booking.getTotalNumberOfGuest());
//        bookingRequest.setBookingConfirmationCode(booking.getBookingConfirmationCode());
//        return bookingRequest;
//    }
//
//
//    public static UserRequest mapUserEntityToUserRequestPlusUserBookingsAndRoom(User user){
//        UserRequest userRequest = mapUserEntityToUserRequest(user);
//
//        if (!user.getBookings().isEmpty()){
//            userRequest.setBookings(user.getBookings().stream().map(
//                    booking -> mapBookingsEntityToBookingRequestPlusBookedRooms(booking, false)
//            ).collect(Collectors.toList()));
//        }
//        return userRequest;
//    }
//
//    public static BookingRequest mapBookingsEntityToBookingRequestPlusBookedRooms(Booking booking, boolean mapUser) {
//
//        BookingRequest bookingRequest = mapBookingsEntityBookingRequest(booking);
//
//        if (mapUser){
//            bookingRequest.setUser(Utils.mapUserEntityToUserRequest(booking.getUser()));
//        }
//        if (booking.getRoom() != null){
//            RoomRequest roomRequest = new RoomRequest();
//
//            roomRequest.setRoomType(booking.getRoom().getRoomType());
//            roomRequest.setRoomPrice(booking.getRoom().getRoomPrice());
//            roomRequest.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
//            roomRequest.setRoomDescription(booking.getRoom().getRoomDescription());
//            bookingRequest.setRoom(roomRequest);
//
//        }
//        return bookingRequest;
//    }
//
//    public static List<UserRequest> mapUserListEntityToUserListRequest(List<User> userList){
//        return userList.stream().map(Utils::mapUserEntityToUserRequest).collect(Collectors.toList());
//    }
//
//    public static List<RoomRequest>mapRoomListEntityToRoomListRequest(List<Room> roomList){
//        return roomList.stream().map(Utils::mapRoomEntityToRoomRequest).collect(Collectors.toList());
//    }
//
//    public static List<BookingRequest>mapBookingListEntityToBookingListRequest(List<Booking> bookingList){
//        return bookingList.stream().map(Utils::mapBookingsEntityBookingRequest).collect(Collectors.toList());
//    }
}

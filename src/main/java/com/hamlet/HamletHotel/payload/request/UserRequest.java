package com.hamlet.HamletHotel.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hamlet.HamletHotel.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private Roles role;
    private List<BookingRequest> bookings;
}

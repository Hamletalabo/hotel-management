package com.hamlet.HamletHotel.payload.response;


import com.fasterxml.jackson.databind.JsonNode;
import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.payload.request.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponse {

    private String email;

    private UserRequest user;

    private ApiResponse response;

}

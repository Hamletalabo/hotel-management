package com.hamlet.HamletHotel.payload.response;

import com.hamlet.HamletHotel.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private ApiResponse apiResponse;

    Roles role;

    private String token;
}

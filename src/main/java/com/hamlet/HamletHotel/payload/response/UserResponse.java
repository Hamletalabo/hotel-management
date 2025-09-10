package com.hamlet.HamletHotel.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private int responseCode;
    private String responseMessage;
    private UserInfo userInfo;
}
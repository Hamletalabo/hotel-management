package com.hamlet.HamletHotel.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditUserResponse {

    private int responseCode;
    private String responseMessage;
    private UserInfo userInfo;
}

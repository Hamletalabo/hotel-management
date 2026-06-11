package com.hamlet.HamletHotel.payload.request;

import lombok.Data;

@Data
public class EditUserRequest {

    private String name;
    private String phoneNumber;
}

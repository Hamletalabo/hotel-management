package com.hamlet.HamletHotel.payload.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateRoomRequest {
    private String roomType;
    private BigDecimal roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;
}

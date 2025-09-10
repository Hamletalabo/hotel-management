package com.hamlet.HamletHotel.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomInfo {

    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private String roomDescription;
    private String roomPhotoUrl;
}

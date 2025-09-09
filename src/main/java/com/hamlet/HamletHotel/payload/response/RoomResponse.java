package com.hamlet.HamletHotel.payload.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

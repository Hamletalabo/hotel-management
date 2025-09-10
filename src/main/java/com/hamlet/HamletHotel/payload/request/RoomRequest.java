package com.hamlet.HamletHotel.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hamlet.HamletHotel.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomRequest {

    private String roomType;
    private BigDecimal roomPrice;
    private String roomDescription;
    private MultipartFile roomPhoto;
}

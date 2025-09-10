package com.hamlet.HamletHotel.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomListResponse {
    private int responseCode;
    private String responseMessage;
    private List<RoomInfo> rooms;
}

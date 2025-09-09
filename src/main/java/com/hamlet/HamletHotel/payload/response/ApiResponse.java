package com.hamlet.HamletHotel.payload.response;


import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.payload.request.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {

   private int responseCode;
   private String responseMessage;
   private UserRequest user;

}

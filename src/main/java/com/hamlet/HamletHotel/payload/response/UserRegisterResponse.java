package com.hamlet.HamletHotel.payload.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponse {

    private String email;

    private String phoneNumber;

    private String name;

    private MultipartFile photo;

    private ApiResponse response;

}

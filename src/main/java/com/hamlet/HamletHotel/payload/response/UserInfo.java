package com.hamlet.HamletHotel.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
}

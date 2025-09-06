package com.hamlet.HamletHotel.entity;

import com.hamlet.HamletHotel.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jtoken_tbl")
public class JwtToken extends BaseEntity {

    @Column(unique = true)
    public String token;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;
}

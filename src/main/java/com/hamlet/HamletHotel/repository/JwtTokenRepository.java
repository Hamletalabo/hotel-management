package com.hamlet.HamletHotel.repository;

import com.hamlet.HamletHotel.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    @Query("SELECT jt FROM JwtToken jt WHERE jt.user.id = :userId")
    List<JwtToken> findAllValidTokenByUser(Long userId);

    Optional<JwtToken> findByToken(String token);
}

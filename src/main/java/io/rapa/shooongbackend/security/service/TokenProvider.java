package io.rapa.shooongbackend.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.rapa.shooongbackend.common.constant.ErrorCode;
import io.rapa.shooongbackend.common.exception.CustomException;
import io.rapa.shooongbackend.properties.JwtProperties;
import io.rapa.shooongbackend.security.dto.KeyPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtProperties jwtProperties;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtProperties.getAppkey().getBytes());
    }

    private String issue(
            Long memberId,
            Long validTime
    ){
        return Jwts.builder()
                .subject(memberId.toString())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + validTime))
                .signWith(getSecretKey())
                .compact();
    }

    private String issueAccessToken(
            Long memberId
    ){
        return issue(memberId, jwtProperties.getValidations().getAccess());
    }

    private String issueRefreshToken(
            Long memberId
    ){
        return issue(memberId, jwtProperties.getValidations().getRefresh());
    }

    public KeyPair issueKeyPair(
            Long memberId
    ){
        return new KeyPair(
                issueAccessToken(memberId),
                issueRefreshToken(memberId)
        );
    }

    public boolean validate(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch(ExpiredJwtException e){
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        } catch(MalformedJwtException e){
            throw new CustomException(ErrorCode.ABNORMAL_TOKEN);
        } catch(JwtException e){
            throw new CustomException(ErrorCode.ERROR_FROM_TOKEN);
        }
    }
    public Jws<Claims> parseClaims(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);
    }
    public Long parseJwtToUserId(String token){
        Jws<Claims> claimsJws = parseClaims(token);
        String userId = claimsJws.getPayload().getSubject();
        return Long.parseLong(userId);
    }
}

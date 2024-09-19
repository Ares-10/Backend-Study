package YOURSSU.assignment.global.auth.provider;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidityMilliseconds;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidityMilliseconds;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String email) {
        return createToken(email, accessTokenValidityMilliseconds, "ACCESS");
    }

    public String createRefreshToken(String email) {
        return createToken(email, refreshTokenValidityMilliseconds, "REFRESH");
    }

    public String createToken(String email, long validityMilliseconds, String tokenType) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("type", tokenType);

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + validityMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(this.getSigningKey())
                .compact();
    }

    public boolean isValidToken(String token, String type) {
        try {
            Jws<Claims> claims = getClaims(token);
            Date now = new Date();
            Date expiredDate = claims.getBody().getExpiration();
            String tokenType = claims.getBody().get("type").toString();
            return expiredDate.after(now) && tokenType.equals(type);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            throw new GlobalException(GlobalErrorCode.AUTH_EXPIRED_TOKEN);
        } catch (SecurityException
                | SignatureException
                | MalformedJwtException
                | IllegalArgumentException e) {
            // 보안 예외, 서명 오류, JWT 형식 오류, 잘못된 입력 값 (null 등)
            throw new GlobalException(GlobalErrorCode.AUTH_INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 JWT 형식
            throw new GlobalException(GlobalErrorCode.UNSUPPORTED_TOKEN);
        }
    }

    public Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
    }

    public String getEmail(String token) {
        return getClaims(token).getBody().getSubject();
    }
}

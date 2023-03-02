package com.paintourcolor.odle.util.jwtutil;

import com.paintourcolor.odle.entity.UserRoleEnum;
import com.paintourcolor.odle.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserDetailsServiceImpl userDetailsService;

    public static final String AUTHORIZATION_HEADER = "Authorization"; // 액세스 토큰 Header 키
    public static final String AUTHORIZATION_REFRESH = "RefreshToken"; // 리프레시 토큰 Header 키
    public static final String AUTHORIZATION_KEY = "auth"; // UserRole
    public static final String BEARER_PREFIX = "Bearer "; // 액세스 토큰 식별자 (7자)
    public static final String REFRESH_PREFIX = "Refresh"; // 리프레시 토큰 식별자 (7자)
    public static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 1시간
    public static final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 14일

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오기
    public String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_REFRESH);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(REFRESH_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String email, Object role, long TOKEN_TIME, String prefix) {
        Date date = new Date();

        return prefix +
                Jwts.builder()
                        .setSubject(email)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    //accessToken 생성
    public String createAccessToken(String email, Object role) {
        return createToken(email, role, ACCESS_TOKEN_TIME, BEARER_PREFIX);
    }

    //refreshToken 생성
    public String createRefreshToken(String email, UserRoleEnum role) {
        return createToken(email, role, REFRESH_TOKEN_TIME, REFRESH_PREFIX);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }

        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // 토큰 만료 여부 확인
    public boolean isExpiration(String token) {
        Date expiration = getUserInfoFromToken(token).getExpiration();
        return expiration.before(new Date());
        //만료된 경우 True, 만료되지 않은 경우 False 반환
    }
}
package org.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;

    @Autowired
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        String token = extractToken(request);
//
//        // 토큰이 유효한 경우, 컨트롤러 호출
//        if (StringUtils.hasText(token) == true && validateToken(token) == true) {
//
//            Authentication authentication = getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // 다음 필터 또는 컨트롤러로 요청 전달
//            filterChain.doFilter(request, response);
//        } else {
//
//            // 토큰이 유효하지 않은 경우, 인증 실패 처리
//            handleAuthenticationFailure(request, response);
//        }

        String requestUri = request.getRequestURI();

        // 특정 URL 경우에만 토큰 유효성 검사 진행
        if(isSkipAuthentication(requestUri) == true) {

            String token = extractToken(request);

            // 토큰이 유효한 경우, 컨트롤러 호출
            if (StringUtils.hasText(token) == true && validateToken(token) == true) {

                Authentication authentication = getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 다음 필터 또는 컨트롤러로 요청 전달
                filterChain.doFilter(request, response);
            } else {

                // 토큰이 유효하지 않은 경우, 인증 실패 처리
                handleAuthenticationFailure(request, response);
            }

        } else {

            filterChain.doFilter(request, response);
        }
    }

    // 특정 URL에 대해 인증 처리를 건너뛰기 위한 로직 구현
    private boolean isSkipAuthentication(String requestUri) {
        
        // /api인 경우만 인증 토큰 인증 필요
        List<String> skipUrls = Arrays.asList("/api/");
        return skipUrls.stream().anyMatch(requestUri::startsWith);
    }

    // JWT 토큰 생성
    public String generateToken(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

        // JWT 토큰 생성
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())  // 사용자 ID
                .claim("roles", roles) // roles 사용자 권한 역할
                .setIssuedAt(new Date())  // JWT 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))    // JWT 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())  // JWT 토큰 서명
                .compact();

        return token;
    }

    // JWT 토큰 유효성 검증
    public boolean validateToken(String token) {

        try {

            // 토큰의 서명을 확인하여 변조 여부를 검사
            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token).getBody();

            // 토큰의 만료 시간을 확인하여 만료 여부를 검사
            Date expirationDate = claims.getExpiration();
            Date now = new Date();
            if(expirationDate.before(now)) {
                System.out.println("토큰이 만료됨");
                return false; // 토큰이 만료됨
            }
            // 필요한 경우 추가적인 유효성 검사를 수행
            return true; // 토큰 유효성 검증 성공
        } catch (Exception ex) {
            System.out.println("토큰 유효성 검증 실패");
            return false; // 토큰 유효성 검증 실패
        }
    }

    // JWT 토큰에서 사용자 이름(Username)을 추출
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }


    // 토큰 추출 로직 구현
    private String extractToken(HttpServletRequest request) {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring("Bearer ".length());
        } else {
            return null;
        }
    }

    // 토큰으로부터 인증 객체 생성 로직 구현
    private Authentication getAuthentication(String token) {

        Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token).getBody();

        // 토큰에서 필요한 정보 추출
        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);

        // 인증 객체 생성
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }


    // 인증 실패 처리를 수행합니다. (예: 오류 응답 반환)
    private void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("유효하지 않은 토큰 또는 만료된 토큰");
    }
}
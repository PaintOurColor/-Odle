package com.paintourcolor.odle.config;

import com.paintourcolor.odle.repository.LogoutTokenRepository;
import com.paintourcolor.odle.util.jwtutil.JwtAuthFilter;
import com.paintourcolor.odle.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;

    private final LogoutTokenRepository logoutTokenRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors()
                .configurationSource(corsConfigurationSource()); //CORS 설정 추가

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/**/admin").hasRole("ADMIN")
                .antMatchers("/users/**").permitAll()
                .antMatchers("/posts/**").permitAll()
                .antMatchers("/crawl/**").permitAll()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 프론트 CORS 오류 뜰 때
                .anyRequest().authenticated()
                // JWT 인증/인가를 사용하기 위한 설정
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil, logoutTokenRepository), UsernamePasswordAuthenticationFilter.class);

//        http.formLogin().loginPage("/api/user/login-page").permitAll();
//
//        http.exceptionHandling().accessDeniedPage("/api/user/forbidden");

        return http.build();
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")
//                .allowedOrigins("https://d35lpvakz9rz23.cloudfront.net/", "https://d35lpvakz9rz23.cloudfront.net", "")
//                .exposedHeaders("Authorization", "RefreshToken");
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://d35lpvakz9rz23.cloudfront.net"));
        //configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*", "Set-Cookie", "Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
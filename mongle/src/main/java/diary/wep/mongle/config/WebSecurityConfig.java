package diary.wep.mongle.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/signup",
                                "/login",
                                "/login-page",
                                "/main", // ✅ 반드시 추가
                                "/images/**", "/css/**", "/js/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .usernameParameter("email")
                        .loginPage("/login-page") // 여기를 변경
                        .loginProcessingUrl("/login") // POST 요청 처리용은 그대로 유지
                        .defaultSuccessUrl("/main", true)
                        .failureUrl("/login-page?error") // 실패 시도 동일하게 변경
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login-page")
                );

        return http.build();
    }
}

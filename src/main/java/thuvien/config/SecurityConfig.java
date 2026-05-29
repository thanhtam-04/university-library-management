package thuvien.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(f -> f.disable()))
            .authorizeHttpRequests(auth -> auth
                // 1. Cho phép tất cả tài nguyên tĩnh thực sự để load CSS/JS/Images
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico", "/uploads/**").permitAll()
                
                // 2. Cấu hình luồng mượn sách yêu cầu xác thực
                .requestMatchers("/books/*/borrow").authenticated()
                
                // 3. Chỉ giữ duy nhất đường dẫn "/books/detail/**" để bao quát mọi cấp độ ID
                .requestMatchers("/books", "/books/detail/**").permitAll()
                
                // Các trang CÔNG KHAI khác
             // Các trang CÔNG KHAI khác
                .requestMatchers("/", "/home", "/login", "/register", "/search/**", "/error",
                                   "/authors", "/tac-gia", "/author", "/author/**", "/contact", "/contact/**").permitAll()
                
                // 4. Phân quyền rõ ràng cho ban quản trị và tài khoản cá nhân
                // Hệ thống sẽ tự hiểu /admin/reservation/list nằm trong nhóm này
                .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_LIBRARIAN")
                
                .requestMatchers("/profile/**", "/my-loans", "/my-loans/**", "/contact/history", "/contact/send").authenticated()
                
                // Độc giả được phép truy cập /reservation/list (đã có xác thực)
                .requestMatchers("/reservation/**").authenticated()
                
                // 5. Các request còn lại mặc định phải đăng nhập
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    var roles = authentication.getAuthorities().stream()
                                              .map(r -> r.getAuthority()).toList();
                    if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_LIBRARIAN")) {
                        response.sendRedirect("/admin/dashboard");
                    } else {
                        response.sendRedirect("/");
                    }
                })
                .failureUrl("/login?error=true")
                .permitAll()
            )
            
            // ================= ĐÓNG WEB LÀ TỰ ĐĂNG XUẤT =================
            .sessionManagement(session -> session
                .maximumSessions(1) // Mỗi tài khoản chỉ đăng nhập được tối đa 1 nơi
                .expiredUrl("/login?expired=true")
            )
            // ==========================================================
            
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true) // Xóa sạch thông tin xác thực cũ
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedHandler((req, res, e) -> {
                    if (!res.isCommitted()) {
                        res.sendRedirect("/access-denied");
                    }
                })
            );

        return http.build();
    }
}
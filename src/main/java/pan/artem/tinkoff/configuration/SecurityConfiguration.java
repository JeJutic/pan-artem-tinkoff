package pan.artem.tinkoff.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import pan.artem.tinkoff.service.UserDetailsServiceImpl;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder configureGlobal(
            AuthenticationManagerBuilder auth,
            DataSource dataSource,
            UserDetailsServiceImpl userDetailsService
    ) throws Exception {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
        auth.jdbcAuthentication().dataSource(dataSource);
        return encoder;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity httpSecurity
    ) throws Exception {
        RequestMatcher isGet = request -> request.getMethod().equals("GET");
        RequestMatcher isApiPath = new AntPathRequestMatcher("/api/**");

         return httpSecurity.authorizeHttpRequests(authz -> authz
                .requestMatchers(request -> isApiPath.matches(request) && isGet.matches(request))
                        .hasAnyRole("USER", "ADMIN")
                .requestMatchers(isApiPath)
                        .hasRole("ADMIN")
                         .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                 .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}

package pan.artem.tinkoff.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pan.artem.tinkoff.properties.AppProperties;
import pan.artem.tinkoff.service.UserDetailsServiceImpl;
import pan.artem.tinkoff.service.UserService;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public DaoAuthenticationProvider authProvider(
//            UserDetailsServiceImpl userDetailsService, PasswordEncoder encoder
//    ) {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(encoder);
//        return authProvider;
//    }

    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder auth,
            DataSource dataSource,
            UserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder
    ) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        auth.jdbcAuthentication().dataSource(dataSource);
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

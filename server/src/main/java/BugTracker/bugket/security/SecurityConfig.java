package BugTracker.bugket.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtConverter jwtConverter;

    public SecurityConfig(JwtConverter jwtConverter) {
        this.jwtConverter = jwtConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        http.csrf().disable();
        http.cors();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/authenticate", "/api/create_account").permitAll()
                .antMatchers(HttpMethod.GET, "/api/reports/incomplete", "/api/messages/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/reports/add", "/api/vote/*", "/api/messages").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/vote/*").authenticated()
                .antMatchers(HttpMethod.GET, "/api/users", "/api/reports/author", "/api/reports/voted", "/api/vote/check/*").authenticated()
                .antMatchers(HttpMethod.GET, "/api/reports").hasAnyRole("DEV", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/reports/update/**").hasAnyRole("DEV", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/messages/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/update_user/**").hasRole("ADMIN")
                .antMatchers("/**").denyAll()
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(authenticationConfiguration), jwtConverter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}



package pl.bykodev.messenger_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private AuthEntryPointJwt unauthorizedHandler;
    private UserDetailsImpl userDetails;

    public SpringSecurityConfig(AuthEntryPointJwt unauthorizedHandler, UserDetailsImpl userDetails) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.userDetails = userDetails;
    }

    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder getBcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails);
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsImpl();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(AbstractHttpConfigurer::disable).cors().and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((request) ->
                        request.requestMatchers("/register", "/login", "/ws", "/file/**", "/socket/**", "/ws/**").permitAll()
                                .requestMatchers("/user/**", "/messages/**", "/users").authenticated())
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement((management) -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(HeadersConfigurer::cacheControl) /* disable page caching */
                .build();
    }
}

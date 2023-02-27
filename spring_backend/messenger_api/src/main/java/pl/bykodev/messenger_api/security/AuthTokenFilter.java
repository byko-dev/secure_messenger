package pl.bykodev.messenger_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.bykodev.messenger_api.services.UserService;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsImpl userDetails;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = parseJwt(request);
            String username = jwtUtils.extractUsername(jwt);

            if(!username.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userDetails.loadUserByUsername(username);
                if(jwtUtils.validateToken(jwt, userDetails)){

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } catch (Exception ex){
            //System.out.println("AuthTokenFilter => " + ex.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
            userService.updateUserActivity(authorizationHeader);
            return authorizationHeader.substring(7);
        }
        return "";
    }
}

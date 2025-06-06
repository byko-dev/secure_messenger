package pl.bykodev.messenger_api.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import pl.bykodev.messenger_api.database.repository.UserEntityRepository;
import pl.bykodev.messenger_api.exceptions.ResourceNotFoundException;
import pl.bykodev.messenger_api.exceptions.UnauthorizedException;
import pl.bykodev.messenger_api.security.JwtUtils;
import pl.bykodev.messenger_api.security.UserDetailsImpl;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WebSocketAuthenticationConfig implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthenticationConfig.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsImpl userDetails;

    @Autowired
    private UserEntityRepository userEntityRepository;


    public WebSocketAuthenticationConfig(JwtUtils jwtUtils, UserDetailsImpl userDetails)
    {
        this.jwtUtils = jwtUtils;
        this.userDetails = userDetails;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authorization = accessor.getNativeHeader("Authorization");
            logger.debug("Authorization: {}", authorization);

            if (authorization != null && !authorization.isEmpty()) {
                String accessToken = authorization.get(0).split(" ")[1];

                String username = this.jwtUtils.extractUsername(accessToken);
                Authentication authentication = getAuthenticationFromUsername(username);
                accessor.setUser(authentication);
            }
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()))
        {
            String username = accessor.getUser().getName();

            userEntityRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException( String.format("User not found with email : '%s'", username)));

            String usernameSubscribeChannel = extractUsernameFromPath(accessor.getDestination());
            if (!usernameSubscribeChannel.equals(username))
            {
                throw new UnauthorizedException("You do not have permission to access this resource.");
            }
        }

        return message;
    }

    private Authentication getAuthenticationFromUsername(String username)
    {
        UserDetails userDetails = this.userDetails.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public static String extractUsernameFromPath(String path) {
        Pattern pattern = Pattern.compile("/socket/([^/]+)/");
        Matcher matcher = pattern.matcher(path);

        if (!matcher.find()) {

            System.out.println(path);

            throw new IllegalArgumentException("The specified provider channel is incorrect");
        }

        return matcher.group(1);
    }
}
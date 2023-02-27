package pl.bykodev.messenger_api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bykodev.messenger_api.database.FileEntity;
import pl.bykodev.messenger_api.exceptions.ResourceNotFoundException;
import pl.bykodev.messenger_api.pojos.*;
import pl.bykodev.messenger_api.security.JwtUtils;
import pl.bykodev.messenger_api.security.UserDetailsImpl;
import pl.bykodev.messenger_api.services.FileService;
import pl.bykodev.messenger_api.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Validated
@CrossOrigin(origins = "*") /* all origins are allowed, only developed purpose */
public class WebController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private UserDetailsImpl userDetailsImpl;
    private FileService fileService;

    @PostMapping("/register")
    public ResponseEntity<Status> registerUser(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest){
        if(userService.userIsPresent(request.getUsername()))
            return ResponseEntity.badRequest().body(new Status("User already exits!", httpRequest.getServletPath()));

        userService.createUser(request);
        return ResponseEntity.ok(new Status("OK", httpRequest.getServletPath()));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsImpl.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new JwtToken(jwt));
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> getPhoto(@Valid @Size(min = 36, max = 36, message = "ID miss UUID requirements")
                                           @PathVariable("id") String id){
        Optional<FileEntity> fileOptional = fileService.getFile(id);
        if(fileOptional.isEmpty())
            throw new ResourceNotFoundException("File with id: " + id + " was not found!");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileOptional.get().getName() + "\"")
                .contentType(MediaType.valueOf(fileOptional.get().getContentType()))
                .body(fileOptional.get().getData());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserData>> getAllUsers(@Valid @Size(min=36, max=36, message = "ID miss UUID requirements")
                                                      @RequestParam String userid,
                                                      @Valid @Size(min = 4, max = 32, message = "search param requires 4-32 characters")
                                                      @RequestParam String search){
        return ResponseEntity.ok(userService.getUsers(userid, search));
    }
}

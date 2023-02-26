package pl.bykodev.messenger_api.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Validated
@CrossOrigin(origins = "*") /* all origins are allowed, only developed purpose */
public class WebController {
    
}

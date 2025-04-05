package pl.bykodev.messenger_api.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@CrossOrigin(origins = "*") /* all origins are allowed, only developed purpose */
@RequestMapping("/user")
public class AiAgentController {





}

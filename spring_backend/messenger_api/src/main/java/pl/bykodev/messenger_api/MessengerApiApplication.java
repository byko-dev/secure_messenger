package pl.bykodev.messenger_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MessengerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessengerApiApplication.class, args);
    }

}

package pl.bykodev.messenger_api.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Status {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String status;
    private String path;

    public Status(String status, String path){
        timestamp = LocalDateTime.now();
        this.status = status;
        this.path = path;
    }

}

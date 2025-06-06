package pl.bykodev.messenger_api.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

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

    public Status(LocalDateTime timestamp, String status, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.path = path;
    }

    public Status() {
    }
}

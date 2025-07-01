package pl.bykodev.messenger_api.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class File {

    private String id;
    private String contentType;
    private String name;
}
package pl.bykodev.messenger_api.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String id;
    private String content;
    private List<File> files = new ArrayList<>();
    private long date;
    private String author;
}
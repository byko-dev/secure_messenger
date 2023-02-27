package pl.bykodev.messenger_api.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Message {
    private String id;
    private String content;
    private String fileId;
    private String fileType;
    private String fileName;
    private long date;
    private String author;
}

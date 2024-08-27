package pl.bykodev.messenger_api.pojos;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private String id;
    private String content;

    private List<File> files = new ArrayList<>();
    private long date;
    private String author;

    public Message(String id, String content, List<File> files, long date, String author) {
        this.id = id;
        this.content = content;
        this.files = files;
        this.date = date;
        this.author = author;
    }

    public Message() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
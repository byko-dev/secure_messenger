package pl.bykodev.messenger_api.pojos;

public class Message {
    private String id;
    private String content;
    private String fileId;
    private String fileType;
    private String fileName;
    private long date;
    private String author;

    public Message(String id, String content, String fileId, String fileType, String fileName, long date, String author) {
        this.id = id;
        this.content = content;
        this.fileId = fileId;
        this.fileType = fileType;
        this.fileName = fileName;
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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

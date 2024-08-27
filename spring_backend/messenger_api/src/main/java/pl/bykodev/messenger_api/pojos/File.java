package pl.bykodev.messenger_api.pojos;

public class File {

    private String id;
    private String contentType;
    private String name;

    public File(String id, String contentType, String name)
    {
        this.id = id;
        this.contentType = contentType;
        this.name = name;
    }

    public  File() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
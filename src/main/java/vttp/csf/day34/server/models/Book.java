package vttp.csf.day34.server.models;

import java.io.Serializable;
import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Book implements Serializable{
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String author;
    private String thumbNail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title.toLowerCase();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author.toLowerCase();
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public static Book create(String json) {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();

        final Book book = new Book();
        book.setId(data.getString("id"));
        book.setTitle(data.getString("title"));
        book.setAuthor(data.getString("author"));
        book.setThumbNail(data.getString("thumbnail"));

        return book;

    }

    public static String getId(String json) {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();

      String id = data.getString("id");
        return id;
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
            .add("id",id)
            .add("title",title)
            .add("author",author)
            .add("thumbnail",thumbNail)
            .build();
    }
    
}

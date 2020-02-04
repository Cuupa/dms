package com.cuupa.dms.storage.document.db;

import java.time.LocalDateTime;
import java.util.List;

public class DBDocument {

    private String owner;

    private String name;

    private String sender;

    private LocalDateTime createDate;

    private List<String> tags;

    private String filename;

    public DBDocument(String filename, String name, String sender, String owner, LocalDateTime createDate, List<String> tags) {
        this.filename = filename;
        this.name = name;
        this.sender = sender;
        this.owner = owner;
        this.createDate = createDate;
        this.tags = tags;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

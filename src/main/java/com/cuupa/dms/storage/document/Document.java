package com.cuupa.dms.storage.document;


import com.cuupa.dms.storage.tag.Tag;

import java.time.LocalDateTime;
import java.util.List;

public class Document {

    private String owner;

    private String name;

    private String sender;

    private LocalDateTime createDate;

    private List<Tag> tags;

    private String filename;

    public Document(String filename, String name, String sender, String owner, LocalDateTime createDate, List<Tag> tags) {
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
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

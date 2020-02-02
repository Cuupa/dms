package com.cuupa.dms.storage.tag.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

public class Tag {

    @Transient
    public static final String SEQUENCE_NAME = "tags_sequence";

    @Id
    private long _id;

    private String name;

    private String owner;

    public Tag(String name) {
        this.name = name;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

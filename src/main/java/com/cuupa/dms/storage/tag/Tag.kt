package com.cuupa.dms.storage.tag;

import com.cuupa.dms.storage.tag.db.DBTag;

public class Tag implements Comparable<Tag> {

    private String name;

    private String owner;

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public Tag() {
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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Tag tag) {
        if (tag.name == null && this.name == null) {
            return 0;
        }

        if (this.name == null) {
            return -1;
        }

        if (tag.name == null) {
            return 1;
        }
        return this.name.compareTo(tag.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            Tag other = (Tag) obj;
            return other.getName().equals(this.name);
        }

        if (obj instanceof DBTag) {
            DBTag other = (DBTag) obj;
            return other.getName().equals(this.name);
        }
        return false;
    }
}

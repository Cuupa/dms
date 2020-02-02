package com.cuupa.dms.storage.tag;

public class Tag implements Comparable<Tag> {

    private long id;

    private String name;

    private String owner;

    public Tag(String name) {
        this.name = name;
    }

    public Tag(long id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public Tag() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

        if (this.name == null && tag.name != null) {
            return -1;
        }

        if (this.name != null && tag.name == null) {
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
        return false;
    }
}

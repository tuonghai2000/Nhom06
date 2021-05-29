package com.example.doitien;

public class Item {
    private String Name;
    private String Link;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public Item(String name, String link) {
        this.Name = name;
        this.Link = link;
    }
}

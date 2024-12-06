package com.example.pervasiveproj;

public class Item {
    private String name;
    private String description;
    private int imageResourceId;

    // Constructor
    public Item(String name, String description, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}

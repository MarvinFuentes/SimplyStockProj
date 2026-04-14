package com.example.simplystockproj;

public class Item {
    int itemId;
    String category;
    String description;
    int availability;
    String imageUri;

    public Item(int itemId, String category, String description, int availability, String imageUri){
        this.itemId = itemId;
        this.category = category;
        this.description = description;
        this.availability = availability;
        this.imageUri = imageUri;
    }

    public int getItemId(){
        return itemId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
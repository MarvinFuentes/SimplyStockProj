package com.example.simplystockproj;

import android.widget.EditText;

public class ExistingItem {
    int itemId;
    String category;
    String description;
    int availability;
    String imageUri;
    int amountToAdd;

    public ExistingItem(int itemId, String category, String description, int availability, String imageUri, int amountToAdd){
        this.itemId = itemId;
        this.category = category;
        this.description = description;
        this.availability = availability;
        this.imageUri = imageUri;
        this.amountToAdd = amountToAdd;
    }

    public int getItemId(){
        return itemId;
    }

    public String getCategory() {
        return category;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public int getAmountToAdd(){
        return amountToAdd;
    }

    public void setAmountToAdd(int amountToAdd){
        this.amountToAdd = amountToAdd;
    }
}

package com.example.simplystockproj;

import android.widget.EditText;

public class ExistingItem {
    int itemId;
    String category;
    String description;
    String url;
    int availability;
    String imageUri;
    int amountToAdd;
    int lowStock;

    public ExistingItem(int itemId, String category, String description, String url, int availability, String imageUri, int amountToAdd, int lowStock){
        this.itemId = itemId;
        this.category = category;
        this.description = description;
        this.url = url;
        this.availability = availability;
        this.imageUri = imageUri;
        this.amountToAdd = amountToAdd;
        this.lowStock = lowStock;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLowStock() {
        return lowStock;
    }

    public void setLowStock(int lowStock) {
        this.lowStock = lowStock;
    }
}

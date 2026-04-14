package com.example.simplystockproj;

public class CartItem {
    int itemId;
    String category;
    String description;
    int availability;
    String imageUri;
    int quantitySelected;

    public CartItem(int itemId, String category, String description, int availability, String imageUri, int quantitySelected){
        this.itemId = itemId;
        this.category = category;
        this.description = description;
        this.availability = availability;
        this.imageUri = imageUri;
        this.quantitySelected = quantitySelected;
    }

    public int getItemId() {
        return itemId;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getAvailability() {
        return availability;
    }

    public String getImageUri() {
        return imageUri;
    }

    public int getQuantitySelected() {
        return quantitySelected;
    }

    public void setQuantitySelected(int quantitySelected){
        this.quantitySelected = quantitySelected;
    }
}

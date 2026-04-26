package com.example.simplystockproj;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.File;

public class ItemView extends LinearLayout {
    TextView categoryTextView;
    TextView descriptionTextView;
    TextView availabilityTextView;
    ImageView imageView;
    Button addBtn;

    public ItemView(Context context){
        super(context);
        init(context);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_row, this, true);

        categoryTextView = (TextView) findViewById(R.id.itemCategoryTextView);
        descriptionTextView = (TextView) findViewById(R.id.itemDescriptionTextView);
        availabilityTextView = (TextView) findViewById(R.id.itemAvaliability);
        imageView = (ImageView) findViewById(R.id.itemImageView);
        addBtn = (Button) findViewById(R.id.itemAddBtn);
    }

    public void setCategory(String category){
        categoryTextView.setText(category);
    }

    public void setDescription(String description){
        descriptionTextView.setText(description);
    }

    public void setAvailability(int availability, int lowStock){

        if(availability == 0){
            availabilityTextView.setText("Qty: " + availability + " - Out of Stock");
            availabilityTextView.setTextColor(Color.RED);
        }
        else if(availability <= lowStock){
            availabilityTextView.setText("Qty: " + availability + " - Low Stock");
            availabilityTextView.setTextColor(Color.parseColor("#F39C12"));
        }
        else{
            availabilityTextView.setText("Qty: " + availability);
            availabilityTextView.setTextColor(Color.GREEN);
        }
    }

    public void setImage(String imagePath){
        if(imagePath != null && !imagePath.isEmpty()){
            imageView.setImageURI(Uri.fromFile(new File(imagePath)));
        }
        else{
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public Button getAddButton(){
        return addBtn;
    }
}

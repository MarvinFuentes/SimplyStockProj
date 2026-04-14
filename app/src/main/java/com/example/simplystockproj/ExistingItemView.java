package com.example.simplystockproj;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.File;

public class ExistingItemView extends LinearLayout {
    TextView categoryTextView;
    TextView descriptionTextView;
    TextView availabilityTextView;
    ImageView existingImageView;
    EditText amountAdded;

    public ExistingItemView(Context context){
        super(context);
        init(context);
    }

    public ExistingItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.existing_item_row, this, true);

        categoryTextView = (TextView) findViewById(R.id.eirCategoryTextView);
        descriptionTextView = (TextView) findViewById(R.id.eirDescriptionTextView);
        availabilityTextView = (TextView) findViewById(R.id.eirAvailabilityTextView);
        existingImageView = (ImageView) findViewById(R.id.eirImageView);
        amountAdded = (EditText) findViewById(R.id.eirAmountEnteredTextText);
    }

    public void setCategory(String category){
        categoryTextView.setText(category);
    }

    public void setDescription(String description){
        descriptionTextView.setText(description);
    }

    public void setAvailability(int availability){
        availabilityTextView.setText("Qty: " + availability);

        if(availability == 0){
            availabilityTextView.setTextColor(Color.RED);
        }
        else if(availability < 5){
            availabilityTextView.setTextColor(Color.YELLOW);
        }
        else{
            availabilityTextView.setTextColor(Color.GREEN);
        }
    }

    public void setImage(String imagePath){
        if(imagePath != null && !imagePath.isEmpty()){
            existingImageView.setImageURI(Uri.fromFile(new File(imagePath)));
        }
        else{
            existingImageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public EditText getAmountEditText(){
        return amountAdded;
    }

    public void setAmountEntered(String amount){
        amountAdded.setText(amount);
    }

    public String getAmountEntered(){
        return amountAdded.getText().toString().trim();
    }

    public void clearAmountEntered(){
        amountAdded.setText("");
    }
}

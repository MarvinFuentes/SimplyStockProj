package com.example.simplystockproj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
    TextView URLTextView;
    ImageView existingImageView;
    EditText amountAdded;
    Button editBtn, deleteBtn;

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
        URLTextView = (TextView) findViewById(R.id.eirURLTextView);
        existingImageView = (ImageView) findViewById(R.id.eirImageView);
        amountAdded = (EditText) findViewById(R.id.eirAmountEnteredTextText);
        editBtn = (Button) findViewById(R.id.eirEditItemBtn);
        deleteBtn = (Button) findViewById(R.id.eirDeleteItemBtn);
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

    public void setUrl(String url){
        if(url != null && !url.trim().isEmpty()){
            String trueUrl = url.trim();

            if(!trueUrl.startsWith("http://") && !trueUrl.startsWith("https://")){
                trueUrl = "https://" + trueUrl;
            }

            URLTextView.setText("Reorder Link");
            URLTextView.setTextColor(Color.BLUE);
            URLTextView.setPaintFlags(URLTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            String linkToOpen = trueUrl;

            URLTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkToOpen));
                    getContext().startActivity(intent);
                }
            });
        }
        else{
            URLTextView.setText("No Reorder Link");
            URLTextView.setTextColor(Color.GRAY);
            URLTextView.setPaintFlags(URLTextView.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
            URLTextView.setOnClickListener(null);
        }
    }

    public Button getEditBtn(){
        return editBtn;
    }

    public Button getDeleteBtn(){
        return deleteBtn;
    }
}

package com.example.simplystockproj;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.File;

public class CartItemView extends LinearLayout {

    TextView categoryTextView;
    TextView descriptionTextView;
    TextView quantityTextView;
    ImageView imageView;
    Button addBtn, subBtn;

    public CartItemView(Context context){
        super(context);
        init(context);
    }

    public CartItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cart_item_row, this, true);

        categoryTextView = (TextView) findViewById(R.id.cartItemCategoryTextView);
        descriptionTextView = (TextView) findViewById(R.id.cartItemDescriptionTextView);
        quantityTextView = (TextView) findViewById(R.id.cartItemQuantity);
        imageView = (ImageView) findViewById(R.id.cartItemImageView);
        addBtn = (Button) findViewById(R.id.cartItemAddBtn);
        subBtn = (Button) findViewById(R.id.cartItemSubBtn);
    }

    public void setCategory(String category){
        categoryTextView.setText(category);
    }

    public void setDescription(String description){
        descriptionTextView.setText(description);
    }

    public void setImage(String imagePath){
        if(imagePath != null && !imagePath.isEmpty()){
            imageView.setImageURI(Uri.fromFile(new File(imagePath)));
        }
        else{
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public void setQuantity(int quantity){
        quantityTextView.setText("Qty: " + quantity);
    }

    public Button getAddBtn(){
        return addBtn;
    }

    public Button getSubBtn(){
        return subBtn;
    }
}

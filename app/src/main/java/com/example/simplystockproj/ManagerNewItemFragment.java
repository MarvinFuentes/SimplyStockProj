package com.example.simplystockproj;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ManagerNewItemFragment extends Fragment {
    Database dbHelper;
    ImageView itemImageView;
    Uri selectedImageUri;
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_new_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new Database(requireContext());

        Button createNewItemBtn = view.findViewById(R.id.niCreateNewItem);

        FrameLayout newItemPhotoFrame = view.findViewById(R.id.niFrameLayout);
        itemImageView = view.findViewById(R.id.niImageView);

        EditText newItemDescriptionText = view.findViewById(R.id.niDescirptionEditText);
        EditText newItemURLText = view.findViewById(R.id.niURLEditText);
        EditText newItemCategoryText = view.findViewById(R.id.niCategoryEditText);
        EditText newItemAvailabilityText = view.findViewById(R.id.niAvailabilityEditText);
        EditText newItemLowStockText = view.findViewById(R.id.niLowStockEditText);

        //Help from Stack Overflow
        //https://stackoverflow.com/questions/77571331/how-to-open-image-gallery-in-android-14-api-34-to-pick-image
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if(uri != null){
                        selectedImageUri = uri;
                        itemImageView.setImageURI(uri);
                        itemImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }
        );

        createNewItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItemDescription = newItemDescriptionText.getText().toString().trim();
                String newItemURL = newItemURLText.getText().toString().trim();
                String newItemCategory = newItemCategoryText.getText().toString().trim();
                String newItemAvailability = newItemAvailabilityText.getText().toString().trim();
                String newItemLowStock = newItemLowStockText.getText().toString().trim();

                //Check to see if the item has a description & category
                if(newItemDescription.isEmpty()){
                    newItemDescriptionText.setError("New item requires a description");
                    return;
                }
                if(newItemCategory.isEmpty()){
                    newItemCategoryText.setError("New item requires a category");
                    return;
                }
                if(newItemAvailability.isEmpty()){
                    newItemAvailabilityText.setError("Enter a quantity");
                    return;
                }
                if(newItemLowStock.isEmpty()){
                    newItemLowStockText.setError("Enter a quantity");
                    return;
                }
                //Extra insurance that the user doesn't past text into editText
                if(!newItemAvailability.matches("\\d+")){
                    newItemAvailabilityText.setError("Only digits can be entered");
                    return;
                }
                //Extra insurance that the user doesn't past text into editText
                if(!newItemLowStock.matches("\\d+")){
                    newItemLowStockText.setError("Only digits can be entered");
                    return;
                }

                //URL isn't checked if it is empty b/c its not required, but I do want to
                //check if it is a legitimate URL
                if(!newItemURL.isEmpty() && !Patterns.WEB_URL.matcher(newItemURL).matches()){
                    newItemURLText.setError("Enter a valid URL");
                    return;
                }

                int businessId = ((ManagerActivity) requireActivity()).getBusinessId();
                String imagePath = null;

                if(selectedImageUri != null){
                    imagePath = saveImageToInternalStorage(selectedImageUri);
                }

                if(dbHelper.createNewItem(businessId, newItemDescription, newItemURL, newItemCategory, imagePath, Integer.parseInt(newItemAvailability), Integer.parseInt(newItemLowStock))){
                    Toast.makeText(requireContext(), "New item created", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
                else{
                    Toast.makeText(requireContext(), "Failed to create item", Toast.LENGTH_SHORT).show();
                }
            }
        });

        newItemPhotoFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickerLauncher.launch("image/*");
            }
        });
    }

    private String saveImageToInternalStorage(Uri uri){
        try{
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

            String fileName = "item_" + System.currentTimeMillis() + ".png";
            File file = new File(requireContext().getFilesDir(), fileName);

            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while((bytesRead = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return file.getAbsolutePath();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
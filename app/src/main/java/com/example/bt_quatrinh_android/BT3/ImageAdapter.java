package com.example.bt_quatrinh_android.BT3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt_quatrinh_android.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<Image> imagesList;
    private List<Image> selectedImages = new ArrayList<>();

    public ImageAdapter( List<Image> images) {
        this.imagesList = images;
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public List<Image> getSelectedImages(){ return selectedImages;}

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        CheckBox imageCheckBox;
        ImageView imageView;
        TextView dateView;

        public ImageViewHolder (View itemView) {
            super(itemView);
            imageCheckBox = itemView.findViewById(R.id.photoCheckBox);
            imageView = itemView.findViewById(R.id.photoView);
            dateView = itemView.findViewById(R.id.dateView);
        }
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt3_image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = imagesList.get(position);
        holder.imageView.setImageBitmap(image.getBitmap());
        holder.dateView.setText(image.getDate());

        // Xử lý CheckBox để thêm/xóa ảnh khỏi danh sách được chọn
        holder.imageCheckBox.setOnCheckedChangeListener(null);  // Xóa listener cũ trước khi gán mới
        holder.imageCheckBox.setChecked(selectedImages.contains(image));

        holder.imageCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedImages.add(image);
            } else {
                selectedImages.remove(image);
            }
        });
    }


}

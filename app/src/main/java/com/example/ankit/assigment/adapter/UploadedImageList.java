package com.example.ankit.assigment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ankit.assigment.R;
import com.example.ankit.assigment.model.ImageUploadInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class UploadedImageList extends RecyclerView.Adapter<UploadedImageList.CustomViewHolder> {
    List<ImageUploadInfo> imageUploadInfoList;
    Context context;
    UploadedImageList.OnitemClickListner onitemClickListner;

    public UploadedImageList(Context context, UploadedImageList.OnitemClickListner onitemClickListner, List<ImageUploadInfo> imageUploadInfoList) {
        this.context = context;
        this.imageUploadInfoList = imageUploadInfoList;
        this.onitemClickListner = onitemClickListner;
    }

    public interface OnitemClickListner {
        void detail(ImageUploadInfo imageUploadInfo);
    }

    @NonNull
    @Override
    public UploadedImageList.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_history, parent, false);
        UploadedImageList.CustomViewHolder customViewHolder = new UploadedImageList.CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UploadedImageList.CustomViewHolder holder, int position) {
        ImageUploadInfo imageUploadInfo = imageUploadInfoList.get(position);
        if (imageUploadInfo != null) {
                Glide.with(context).load(imageUploadInfo.getImageURL()).placeholder(R.drawable.actor).into(holder.imgProfile);
            }
        holder.txtOwnerName.setText(imageUploadInfo.getImageName());


    }


    @Override
    public int getItemCount() {
        return imageUploadInfoList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgProfile;
        private TextView txtOwnerName;
        private CardView cvImage;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View view) {
            imgProfile = (ImageView) view.findViewById(R.id.imgProfile);
            txtOwnerName = (TextView) view.findViewById(R.id.txtOwnerName);
            cvImage = (CardView) view.findViewById(R.id.cvImage);
            cvImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cvImage:
                    onitemClickListner.detail(imageUploadInfoList.get(getAdapterPosition()));
                    break;
            }


        }


    }

}

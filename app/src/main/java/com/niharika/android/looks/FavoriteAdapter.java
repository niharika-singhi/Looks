package com.niharika.android.looks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.niharika.android.looks.room.FavoritePhoto;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Context mCtx;
    private static final String ARG_PHOTO_URL = "photo_url";
    PhotoRepository mRepository;
    List<FavoritePhoto> photoList;
    Integer viewWidth, viewHeight;

    protected FavoriteAdapter(Context mCtx, PhotoRepository repository) {
        this.mCtx = mCtx;
        mRepository = repository;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_item_image, parent, false);
        return new FavoriteAdapter.FavoriteViewHolder(view);
    }

    void submitData(List<FavoritePhoto> pList) {
        photoList = pList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        FavoritePhoto item = photoList.get(position);
        if (item != null) {
            if (position == 1 || viewWidth == null) {
                ViewTreeObserver viewTreeObserver = holder.imageView.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            holder.imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            viewWidth = holder.imageView.getWidth();
                            viewHeight = holder.imageView.getHeight();
                            showImage(item.url_s, holder.imageView);
                        }
                    });
                }
            } else
                showImage(item.url_s, holder.imageView);
            holder.nameTextView.setText(item.title);
            Bundle bundle = new Bundle();
            bundle.putString(ARG_PHOTO_URL, item.url_s);
            holder.imageView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.photoFragment, bundle));
            holder.delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRepository.delFavPhoto(item);
                    //Toast.makeText(mCtx, "Added to Favorites", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(mCtx, "No Photo found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        if (photoList != null)
            return photoList.size();
        else return 0;
    }


    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTextView, descTextView;
        ImageButton mImageButton, delButton;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.makeup_img);
            mImageButton = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.name);
            descTextView = itemView.findViewById(R.id.desc);
            delButton = itemView.findViewById(R.id.favButton);
            delButton.setBackground(mCtx.getResources().getDrawable(R.drawable.ic_del));
        }
    }

    private void showImage(String url, ImageView imageView) {
        Glide.with(mCtx)
                .load(url)
                .override(viewWidth, viewHeight)
                .centerCrop()
                .into(imageView);
    }
}

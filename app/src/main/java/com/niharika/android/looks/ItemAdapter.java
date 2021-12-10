package com.niharika.android.looks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.paging.PagedListAdapter;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.niharika.android.looks.room.FavoritePhoto;
import com.niharika.android.looks.room.Photo;

public class ItemAdapter extends PagingDataAdapter<Photo, ItemAdapter.ItemViewHolder> {

    private Context mCtx;
    private static final String ARG_PHOTO_URL ="photo_url" ;
    PhotoRepository mRepository;
    Integer viewWidth,viewHeight;

    protected ItemAdapter(Context mCtx,PhotoRepository repository) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
        mRepository=repository;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_item_image, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        SearchCallback searchCallback;
        Photo item = getItem(position);
        if (item != null) {
        if(position==1 || viewWidth==null) {
            ViewTreeObserver viewTreeObserver = holder.imageView.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        holder.imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                         viewWidth = holder.imageView.getWidth();
                         viewHeight = holder.imageView.getHeight();
                         showImage(item.url_s,holder.imageView);
                    }
                });
            }
        }
        else
            showImage(item.url_s,holder.imageView);
        holder.nameTextView.setText(item.title);
            Bundle bundle = new Bundle();
            bundle.putString(ARG_PHOTO_URL, item.url_s);
            holder.favButton.setBackground(mCtx.getResources().getDrawable(R.drawable.ic_unfavorite));
            mRepository.searchFavoriteList(item, new SearchCallback() {
                @Override
                public void onComplete(Boolean result) {
                    if(result)
                        holder.favButton.setBackground(mCtx.getResources().getDrawable(R.drawable.ic_favorite));
                }
            });


            holder.imageView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.photoFragment, bundle));
            holder.favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRepository.insertFavPhoto(new FavoritePhoto(item.id,item.owner,item.title,item.url_s));
                    holder.favButton.setBackground(mCtx.getResources().getDrawable(R.drawable.ic_favorite));
                    Snackbar.make(v, "Added to Favorites", Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(mCtx, "Loading..", Toast.LENGTH_LONG).show();
        }
    }

    private void showImage(String url,ImageView imageView) {
        Glide.with(mCtx)
                .load(url)
                .override(viewWidth, viewHeight)
                .centerCrop()
                .into(imageView);
    }


    private static DiffUtil.ItemCallback<Photo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Photo>() {
                @Override
                public boolean areItemsTheSame(Photo oldItem, Photo newItem) {
                    return oldItem.id == newItem.id;
                }
                @Override
                public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
                    int i=oldItem.compareTo(newItem);
                    if(i==0)
                        return true;
                    else
                        return false;
                }
            };


    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTextView,descTextView;
        ImageButton mImageButton,favButton;
        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.makeup_img);
            mImageButton = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.name);
            descTextView = itemView.findViewById(R.id.desc);
            favButton=itemView.findViewById(R.id.favButton);
        }
    }
}




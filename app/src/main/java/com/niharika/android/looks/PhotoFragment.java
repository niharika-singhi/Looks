package com.niharika.android.looks;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {
    private static final String ARG_PHOTO_URL ="photo_url" ;
    ImageView mImageView;


    public static PhotoFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_URL, url);
        final PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_photo, container, false);
        mImageView=view.findViewById(R.id.imageView);
        String url=getArguments().getString(ARG_PHOTO_URL);
        Glide.with(getContext())
                .load(url)
                .into(mImageView);
        return view;
    }
}

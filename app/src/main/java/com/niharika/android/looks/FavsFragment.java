package com.niharika.android.looks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.niharika.android.looks.room.FavoritePhoto;
import com.niharika.android.looks.room.Photo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    ItemViewModel itemViewModel;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorites_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delAll:
                itemViewModel.mPhotoRepository.delAll();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(true);
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        final FavoriteAdapter adapter = new FavoriteAdapter(getActivity(),itemViewModel.mPhotoRepository);
        mRecyclerView.setAdapter(adapter);
       itemViewModel.getFavoriteList().observe(getViewLifecycleOwner(), new Observer<List<FavoritePhoto>>() {
           @Override
           public void onChanged(@Nullable List<FavoritePhoto> photoList) {
               adapter.submitData(photoList);
           }
       });
       return view;

    }
}

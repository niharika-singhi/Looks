package com.niharika.android.looks;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import kotlin.Unit;

public class MainFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private String query;
    private ProgressBar pBar;
    private ItemViewModel itemViewModel;
    private ItemAdapter adapter;
    private TextView errorMsg;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                onCreateDialog().show();
                return true;
            default:
                break;
        }
        return false;
    }

    //Search operation
    public Dialog onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.search, null);
        builder.setView(view)
                .setTitle(R.string.search)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText searchString = view.findViewById(R.id.searchString);
                        query = searchString.getText().toString();
                        if(query.isEmpty()) {
                            dialog.cancel();
                            return;
                        }

                        itemViewModel.createPager(query);
                        observeData();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        pBar = view.findViewById(R.id.progressBar);
        errorMsg = view.findViewById(R.id.errorMsg);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        adapter = new ItemAdapter(getActivity(), itemViewModel.mPhotoRepository);
        adapter.addLoadStateListener(loadStates -> {
            pBar.setVisibility(loadStates.getRefresh() instanceof LoadState.Loading
                    ? View.VISIBLE : View.GONE);
            errorMsg.setVisibility(loadStates.getRefresh() instanceof LoadState.Loading
                    ? View.GONE : View.VISIBLE);
            errorMsg.setVisibility(loadStates.getRefresh() instanceof LoadState.Error
                    ? View.VISIBLE : View.GONE);
            return Unit.INSTANCE;

        });
        observeData();
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    void observeData() {
        itemViewModel.liveData.observe(getViewLifecycleOwner(), pagingData -> {
            adapter.submitData(getLifecycle(), pagingData);
        });
    }
}

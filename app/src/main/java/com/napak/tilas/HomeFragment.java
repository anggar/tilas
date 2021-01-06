package com.napak.tilas;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.napak.tilas.databinding.FragmentPhotoListBinding;
import com.napak.tilas.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 */
public class HomeFragment extends Fragment {
    private FragmentPhotoListBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    @SuppressWarnings("unused")
    public static HomeFragment newInstance(int columnCount) {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPhotoListBinding.inflate(inflater, container, false);

        RecyclerView view = binding.getRoot();
        Context context = view.getContext();
        view.setLayoutManager(new LinearLayoutManager(context));
        view.setAdapter(new MyHomeRecyclerViewAdapter(DummyContent.ITEMS));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
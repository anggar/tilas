package com.napak.tilas;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.napak.tilas.api.ApiClient;
import com.napak.tilas.api.ApiInterface;
import com.napak.tilas.databinding.FragmentPhotoListBinding;
import com.napak.tilas.model.LumenResponse;
import com.napak.tilas.model.PhotoListItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class HomeFragment extends Fragment {
    private FragmentPhotoListBinding binding;
    private ApiInterface api;

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
        api = ApiClient.getClient().create(ApiInterface.class);

        RecyclerView view = binding.getRoot();
        Context context = view.getContext();
        view.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Call<LumenResponse<PhotoListItem>> call = api.listPhotos();

        call.enqueue(new Callback<LumenResponse<PhotoListItem>>() {
            @Override
            public void onResponse(@NonNull Call<LumenResponse<PhotoListItem>> call,
                                   @NonNull Response<LumenResponse<PhotoListItem>> response) {
                LumenResponse<PhotoListItem> paginatedResponse = response.body();
                assert paginatedResponse != null;
                List<PhotoListItem> data = paginatedResponse.getData();

                ((RecyclerView) view).setAdapter(new MyHomeRecyclerViewAdapter(data));
            }

            @Override
            public void onFailure(@NonNull Call<LumenResponse<PhotoListItem>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Can't get a list of photos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
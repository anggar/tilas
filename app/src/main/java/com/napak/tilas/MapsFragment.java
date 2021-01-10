package com.napak.tilas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.napak.tilas.api.ApiClient;
import com.napak.tilas.api.ApiInterface;
import com.napak.tilas.model.PhotoMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Optional;

public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    private List<PhotoMap> markers;
    private ApiInterface api;

    private OnMapReadyCallback callback = googleMap -> {
        for(PhotoMap marker: markers) {
            googleMap.addMarker(new MarkerOptions().position(marker.getLatLng()));
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(markers.get(0).getLatLng()));
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        api = ApiClient.getClient().create(ApiInterface.class);

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Call<List<PhotoMap>> call = api.listPhotoMap();

        call.enqueue(new Callback<List<PhotoMap>>() {
            @Override
            public void onResponse(Call<List<PhotoMap>> call, Response<List<PhotoMap>> response) {
                markers = response.body();
            }

            @Override
            public void onFailure(Call<List<PhotoMap>> call, Throwable t) {
                Toast.makeText(getContext(), "Can't load photos", Toast.LENGTH_SHORT).show();
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Optional<PhotoMap> mark = markers.stream().filter(it -> it.getLatLng().equals(marker.getPosition())).findFirst();

        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("id", mark.get().getId());
        startActivity(intent);

        return true;
    }
}
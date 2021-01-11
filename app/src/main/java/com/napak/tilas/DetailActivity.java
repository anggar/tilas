package com.napak.tilas;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.napak.tilas.api.ApiClient;
import com.napak.tilas.api.ApiInterface;
import com.napak.tilas.databinding.ActivityDetailBinding;
import com.napak.tilas.model.Photo;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private final static String PHOTO_ID_EXTRA = "id";

    private ActivityDetailBinding binding;
    private int photo_id;
    private ApiInterface api;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        api = ApiClient.getClient().create(ApiInterface.class);

        // get extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            photo_id = (int) extras.get(PHOTO_ID_EXTRA);
        }

        getPhoto();
    }

    private void getPhoto() {
        Call<Photo> call = api.listDetailPhoto(photo_id);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                Photo photo = response.body();
                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm", Locale.getDefault());

                assert photo != null;
                binding.detailTitle.setText(photo.getTitle());
                binding.detailCaption.setText(photo.getCaption());
                binding.detailDate.setText(format.format(photo.getCreated_at()));
                Picasso.get().load(ApiClient.baseUrl + photo.getPhoto_url()).into(binding.detailImage);

                intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("LAT", photo.getLat());
                intent.putExtra("LNG", photo.getLng());

                binding.btnLoc.setOnClickListener(view -> {
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Something error happened", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
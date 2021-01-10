package com.napak.tilas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.napak.tilas.api.ApiClient;
import com.napak.tilas.api.ApiInterface;
import com.napak.tilas.databinding.FragmentTambahBinding;
import com.napak.tilas.model.StatusMessage;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PartMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TambahFragment extends Fragment implements SensorEventListener {

    private FragmentTambahBinding binding;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SharedPreferences sharedPref;
    private File picFile;

    private ApiInterface api;
    private float luminance;
    private String user_id;

    private static final int LAUNCH_CAMERA = 558;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTambahBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    private static RequestBody toRequestBody (String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    private static RequestBody toRequestBody (float value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), Float.valueOf(value).toString());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPref = getContext().getSharedPreferences("USER_ID", Context.MODE_PRIVATE);

        api = ApiClient.getClient().create(ApiInterface.class);

        if (sharedPref.getAll().isEmpty()) {
            user_id = UUID.randomUUID().toString();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("USER_ID", user_id);
            editor.apply();
        } else {
            user_id = sharedPref.getString("USER_ID", "BACKUP");
        }


        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        } else {
            luminance = -1.0f;
        }

        binding.addPicture.setOnClickListener(view1 -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, LAUNCH_CAMERA);
        });

        binding.upload.setOnClickListener(view1 -> {
            Map<String, RequestBody> map = new HashMap<>();
            map.put("title", toRequestBody(binding.inputTitle.getText().toString()));
            map.put("caption", toRequestBody(binding.inputCaption.getText().toString()));
            map.put("lat", toRequestBody(-7.21f)); // TODO: GPS
            map.put("lng", toRequestBody(121.21f));
            map.put("user_id", toRequestBody(user_id));
            map.put("luminance", toRequestBody(luminance));

            map.put(
                "photo\"; filename=\"" + picFile.getName() + "\"",
                RequestBody.create(MediaType.parse("image/*"), picFile)
            );

            Call<StatusMessage> call = api.uploadPhoto(map);

            call.enqueue(new Callback<StatusMessage>() {
                @Override
                public void onResponse(@NonNull Call<StatusMessage> call, @NonNull Response<StatusMessage> response) {
                    Toast.makeText(getContext(), "Photo successfully uploaded", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<StatusMessage> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Photo can't be uploaded", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        picFile = new File(getContext().getCacheDir(), (new Date()).toString() + ".jpg");
        FileOutputStream fos = null;

        try {
            picFile.createNewFile();
            picFile.setWritable(true);
            fos = new FileOutputStream(picFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap;
        if ((requestCode == LAUNCH_CAMERA) && (resultCode == Activity.RESULT_OK)) {
            if (data != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
                binding.imageView.setImageBitmap(bitmap);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                sensorTriggerOnPhotoCaptured();
            }
        }
    }

    private void sensorTriggerOnPhotoCaptured() {
        binding.simpleSeekBar.setProgress(100 - ((int) (luminance/16.0) * 100));
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        luminance = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
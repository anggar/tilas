package com.napak.tilas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
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
    private double locLat;
    private double locLng;
    private String user_id;
    private String currentPhotoPath;

    private static final int LAUNCH_CAMERA = 558;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 112;

    private boolean locationPermissionGranted;
    private LocationManager locationManager;
    private MyLocationListener locationListener;

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

    private static RequestBody toRequestBody (double value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), Double.valueOf(value).toString());
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            locLat = location.getLatitude();
            locLng = location.getLongitude();
        }
    }

    private File createImageFile() throws IOException {
        File image = new File(getContext().getCacheDir(), (new Date()).toString() + ".jpg");
        currentPhotoPath = image.getAbsolutePath();
        picFile = image;
        return image;
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, LAUNCH_CAMERA);
            }
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void startLocationRequest() {
        if (!locationPermissionGranted) {
            getLocationPermission();
        }

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000, 2, locationListener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // request location permissions
        getLocationPermission();

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

        locationListener = new MyLocationListener();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        startLocationRequest();

        binding.addPicture.setOnClickListener(view1 -> {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(intent, LAUNCH_CAMERA);
            takePictureIntent();
        });

        binding.simpleSeekBar.setMax(17);
        binding.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        binding.upload.setOnClickListener(view1 -> {
            Map<String, RequestBody> map = new HashMap<>();
            map.put("title", toRequestBody(binding.inputTitle.getText().toString()));
            map.put("caption", toRequestBody(binding.inputCaption.getText().toString()));
            map.put("lat", toRequestBody(locLat));
            map.put("lng", toRequestBody(locLng));
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static Bitmap rotateImage(Bitmap src, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(src, 0, 0,
                src.getWidth(), src.getHeight(), matrix, true);
    }

    private Bitmap processBitmap(Bitmap bmp) throws IOException {
        ExifInterface ei = new ExifInterface(currentPhotoPath);
        Bitmap resbmp = null;

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                resbmp = rotateImage(bmp, 90f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                resbmp = rotateImage(bmp, 180f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                resbmp = rotateImage(bmp, 270f);
                break;

            default:
                resbmp = bmp;

        }
        return resbmp;
    }

    private void processCameraResult(Intent data) throws IOException
    {
        Bitmap bmp, rotatedBmp = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        bmp = BitmapFactory.decodeFile(currentPhotoPath, options);
        bmp = processBitmap(bmp);
        binding.imageView.setImageBitmap(bmp);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        picFile = new File(getContext().getCacheDir(), (new Date()).toString() + ".jpg");
//        FileOutputStream fos = null;
//
//        try {
//            picFile.createNewFile();
//            picFile.setWritable(true);
//            fos = new FileOutputStream(picFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Bitmap bitmap;
//        if ((requestCode == LAUNCH_CAMERA) && (resultCode == Activity.RESULT_OK)) {
//            if (data != null) {
//                bitmap = (Bitmap) data.getExtras().get("data");
//                binding.imageView.setImageBitmap(bitmap);
//
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//                sensorTriggerOnPhotoCaptured();
//            }
//        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LAUNCH_CAMERA) {
                try {
                    processCameraResult(data);
                    sensorTriggerOnPhotoCaptured();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sensorTriggerOnPhotoCaptured() {
        float capturedLuminance = luminance;
        binding.simpleSeekBar.setProgress((int) capturedLuminance);
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
        locationManager.removeUpdates(locationListener);
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
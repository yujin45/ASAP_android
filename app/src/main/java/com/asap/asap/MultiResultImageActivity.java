package com.asap.asap;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MultiResultImageActivity extends AppCompatActivity {
    private RecyclerView resultRecyclerView;
    private Button homeButton;
    private Button saveButton;
    private ArrayList<String> resultImageUrlList = new ArrayList<>();
    private ArrayList<RecyclerViewItem> RecyclerViewItemList = new ArrayList<>();
    //private ArrayList<String> resultImageUrlList;
    private MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_result_image);

        resultRecyclerView = findViewById(R.id.resultRecyclerView);
        homeButton = findViewById(R.id.homeButton);
        saveButton = findViewById(R.id.saveButton);

        // 실제 구현시 intent로 resultImageUrlList 받아오기
        //Intent intent = getIntent();
        //resultImageUrlList = (ArrayList<String>) intent.getSerializableExtra("resultImageUrlList");
        // intetn로 받아오는 것 말고 임시로 넣어보기
        resultImageUrlList.add("https://health.chosun.com/site/data/img_dir/2023/05/31/2023053102582_0.jpg");
        resultImageUrlList.add("https://pbs.twimg.com/profile_images/1245698077245067265/HRzaMXDk_400x400.jpg");
        resultImageUrlList.add("https://health.chosun.com/site/data/img_dir/2023/05/31/2023053102582_0.jpg");
        resultImageUrlList.add("https://health.chosun.com/site/data/img_dir/2023/05/31/2023053102582_0.jpg");
        resultImageUrlList.add("https://pbs.twimg.com/profile_images/1245698077245067265/HRzaMXDk_400x400.jpg");
        resultImageUrlList.add("https://pbs.twimg.com/profile_images/1245698077245067265/HRzaMXDk_400x400.jpg");
        resultImageUrlList.add("https://pbs.twimg.com/profile_images/1245698077245067265/HRzaMXDk_400x400.jpg");
        resultImageUrlList.add("https://health.chosun.com/site/data/img_dir/2023/05/31/2023053102582_0.jpg");

        // 객체 생성 후 전달
        for (String resultUrl : resultImageUrlList) {
            RecyclerViewItemList.add(new RecyclerViewItem(resultUrl, false));
        }
        
        adapter = new MyRecyclerViewAdapter(this, RecyclerViewItemList);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        resultRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2,fromDpToPx(16f)));
        resultRecyclerView.setAdapter(adapter);

        Log.d("Glide 라이브러리 사용 ", "0000000000000000000000000");

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiResultImageActivity.this, ImageUploadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    for (RecyclerViewItem recyclerViewItem : RecyclerViewItemList) {
                        if (recyclerViewItem.isSelected()){
                            //선택된 애들만 저장하도록 함
                            saveImageToGallery(recyclerViewItem.getImageUrl());
                        }
                    }
                      Toast.makeText(MultiResultImageActivity.this, "이미지가 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                      Toast.makeText(MultiResultImageActivity.this, "이미지 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void saveImageToGallery(String imageUrl) {
        GlideApp.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Save the Bitmap to the gallery
                        saveBitmapToGallery(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Do nothing
                    }
                });
    }

    private void saveBitmapToGallery(Bitmap bitmap) {
        String fileName = "ASAP_Result_" + System.currentTimeMillis() + ".png";
        File galleryFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName);

        try {
            OutputStream outputStream = new FileOutputStream(galleryFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Insert the image into the MediaStore
            MediaStore.Images.Media.insertImage(getContentResolver(), galleryFile.getAbsolutePath(), galleryFile.getName(), galleryFile.getName());

          //  Toast.makeText(MultiResultImageActivity.this, "이미지가 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
          //  Toast.makeText(MultiResultImageActivity.this, "이미지 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public static int fromDpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    //////////////////////////////////////////////////////

/*
    private void saveImageToGallery(RecyclerViewItem recyclerViewItem) {

        BitmapDrawable drawable = (BitmapDrawable) resultImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        String fileName = "ASAP_Result_" + System.currentTimeMillis() + ".png";
        File galleryFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName);

        try {
            OutputStream outputStream = new FileOutputStream(galleryFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            MediaStore.Images.Media.insertImage(getContentResolver(), galleryFile.getAbsolutePath(), galleryFile.getName(), galleryFile.getName());

            Toast.makeText(MultiResultImageActivity.this, "이미지가 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MultiResultImageActivity.this, "이미지 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap decodeBase64ToBitmap(String base64Image) {
        try {
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
          //  resultImageView.setImageBitmap(decodedBitmap);
            return decodedBitmap;
        } catch (IllegalArgumentException e) {
            Log.d(ContentValues.TAG, "잘못된 Base64 문자열이 들어옴");
            e.printStackTrace();
            return null;
        }
    }*/

}
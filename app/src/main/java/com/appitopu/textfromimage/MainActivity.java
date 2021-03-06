package com.appitopu.textfromimage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.appitopu.textfromimage.R;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.guna.ocrlibrary.OCRCapture;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private final int CAMERA_SCAN_TEXT = 0;
    private final int LOAD_IMAGE_RESULTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionCamera:
                //Scan text from camera.
                OCRCapture.Builder(this)
                        .setUseFlash(true)
                        .setAutoFocus(true)
                        .buildWithRequestCode(CAMERA_SCAN_TEXT);
                break;
            case R.id.actionPhoto:
                Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGallery, LOAD_IMAGE_RESULTS);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == CAMERA_SCAN_TEXT) {
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    textView.setText(data.getStringExtra(TextBlockObject));
                }
            } else if (requestCode == LOAD_IMAGE_RESULTS) {
                Uri pickedImage = data.getData();
                try {
                    //Extract text from image.
                    String text = OCRCapture.Builder(this).getTextFromUri(pickedImage);
                    //You can also use getTextFromBitmap(Bitmap bitmap) or getTextFromImage(String imagePath) buplic APIs from ibrary.
                    textView.setText(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
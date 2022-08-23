package com.restorapos.waiters.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.utils.SharedPref;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.util.List;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private String website;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private LinearLayout frameLayout;
    private ImageView imageView;
    ZXingScannerView mScannerView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0d8f83"));
        SharedPref.init(QrCodeActivity.this);

        Log.d("OOO", "onCreateView: "+SharedPref.read("BASEURL", ""));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.INTERNET,
                            Manifest.permission.CAMERA
                    ).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {

                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                }
            }).check();
        }

        mScannerView = new ZXingScannerView(this);
        frameLayout = findViewById(R.id.scan_container);
        TextView scan_btn = findViewById(R.id.scan_btn);
        TextView txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        //surfaceView = findViewById(R.id.surfaceView);
        imageView = findViewById(R.id.imageView3);

        scan_btn.setOnClickListener(view -> {
            imageView.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            setContentView(mScannerView);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initialiseDetectorsAndSources();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();

    }

    @Override
    public void handleResult(Result rawResult) {
        SharedPref.write("BASEURL",rawResult.getText());
        startActivity(new Intent(this,LoginActivity.class));
    }
}

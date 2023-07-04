package com.example.qr_scanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {
    Button btn_scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_scan =findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(view -> {
            scanCode();
        });
    }

    private void scanCode() {
        ScanOptions options=new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() !=null)
        {
            String url = result.getContents();
            AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Result");
            builder.setMessage(url);
            builder.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Open the URL
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Copy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Copy the URL to the clipboard
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("URL", url);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, "URL copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }

    });
}
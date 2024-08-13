package com.zza.messages;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import org.telegram.messenger.R;
import org.telegram.ui.LaunchActivity;

public class MyLauncherActivity extends Activity {
    ImageView imageView;
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_launcher);
        imageView = findViewById(R.id.loading);
        Glide.with(this).load(R.mipmap.loadings).asGif().into(imageView);
        if (!closeApp()) {
            handler.postDelayed(runnable, 3000);
            imageView.setOnLongClickListener(view -> {
                startActivity(new Intent(MyLauncherActivity.this, LaunchActivity.class));
                handler.removeCallbacks(runnable);
                finish();
                return false;
            });
        }
    }

    public boolean closeApp() {
        if (android.os.Build.VERSION.SDK_INT < 24) {
            return false;
        }
        // Get the current time
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);

        // Check if the current time is between 10:00 PM and 5:00 AM
        if (hour >= 22 || hour < 5) {
            finish();
            return true;
        }
        return false;
    }

    Runnable runnable = () -> {
        String package_name = "com.miHoYo.hkrpg";
        String activity_path = "com.mihoyo.combosdk.ComboSDKActivity";
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//可选
        ComponentName cn = new ComponentName(package_name, activity_path);
        intent.setComponent(cn);

        if (intent.resolveActivityInfo(getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "启动失败，请点击icon进入游戏", Toast.LENGTH_SHORT).show();
            finish();
        }

    };
}

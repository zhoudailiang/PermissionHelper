package cn.zhoudl.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionActivity extends AppCompatActivity {

    private static final String PERMISSION_KEY = "permission_key";
    private static final int PERMISSION_CODE = 0x1001;

    private static WeakReference<ZPermission.Callback> sWeakCallback;

    public static void startActivity(@NonNull Context context,
                                     @NonNull String[] permissions,
                                     @NonNull ZPermission.Callback callback) {
        sWeakCallback = new WeakReference<>(callback);
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(PERMISSION_KEY, permissions);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handlePermission();
    }

    private void handlePermission() {
        String[] permissions = getIntent().getStringArrayExtra(PERMISSION_KEY);
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_CODE) {
            return;
        }

        ZPermission.Callback callback = sWeakCallback.get();
        if (callback == null) {
            return;
        }

        Map<String, Boolean> permissionMap = new HashMap<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            boolean ret = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
            if (ret) {
                // 禁止后没有点不再询问
                permissionMap.put(permissions[i], true);
            } else {
                // 禁止后点了不再询问
                permissionMap.put(permissions[i], false);
            }
        }

        if (permissionMap.size() == 0) {
            callback.onAllGranted();
        } else {
            callback.onPartGranted(permissionMap);
        }
        finish();
    }
}

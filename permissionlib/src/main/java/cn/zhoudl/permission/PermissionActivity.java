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
import java.util.List;

public class PermissionActivity extends AppCompatActivity {

    private static final String PERMISSION_KEY = "permission_key";
    private static final int PERMISSION_CODE = 0x1001;

    /**
     * 权限请求回调
     */
    private static WeakReference<PermissionHelper.Callback> sWeakCallback;

    /**
     * 提供静态方法
     *
     * @param context     上下文
     * @param permissions 权限数组
     * @param callback    回调
     */
    public static void startActivity(@NonNull Context context,
                                     @NonNull String[] permissions,
                                     @NonNull PermissionHelper.Callback callback) {
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

    /**
     * 根据传入的权限去请求
     */
    private void handlePermission() {
        // 获取所有传递过来没有授权的权限
        String[] permissions = getIntent().getStringArrayExtra(PERMISSION_KEY);
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
    }

    /**
     * 请求回调
     *
     * @param requestCode  请求码
     * @param permissions  请求权限数组
     * @param grantResults 是否授权的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_CODE) {
            finish();
            return;
        }

        PermissionHelper.Callback callback = sWeakCallback.get();
        if (callback == null) {
            finish();
            return;
        }

        List<String> notGrantedList = new ArrayList<>();
        List<String> forbiddenList = new ArrayList<>();

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            if (isPermissionForbid(permissions[i])) {
                // 不再询问
                forbiddenList.add(permissions[i]);
            } else {
                // 再询问
                notGrantedList.add(permissions[i]);
            }
        }

        if (notGrantedList.size() + forbiddenList.size() == 0) {
            callback.onAllGranted();
        } else {
            callback.onNotGranted(notGrantedList, forbiddenList);
        }
        finish();
    }

    /**
     * 判断某权限是否被禁止
     *
     * @param permission 要判断的权限
     */
    private boolean isPermissionForbid(String permission) {
        return !ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
    }
}

package cn.zhoudl.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理器入口
 * <p>
 * Created by DaveZ 2018/11/15
 */
public class PermissionHelper {

    /**
     * 请求权限
     *
     * @param context     上下文
     * @param permissions 权限数组
     * @param callback    授权回调
     */
    public static void requestPermissions(@NonNull Context context,
                                          @NonNull String[] permissions,
                                          @NonNull Callback callback) {
        assetContextNotNull(context);
        assetPermissionsNotNull(permissions);
        assetCallbackNotNull(callback);

        if (isLessAndroidM()) {
            callback.onAllGranted();
            return;
        }

        List<String> notGrantedList = new ArrayList<>();
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                notGrantedList.add(permission);
            }
        }

        int notGrantedSize = notGrantedList.size();
        if (notGrantedSize == 0) {
            callback.onAllGranted();
            return;
        }

        // 没有被授权的权限通过启动透明 Activity 去进行请求
        PermissionActivity.startActivity(context, notGrantedList.toArray(new String[notGrantedSize]), callback);
    }

    /**
     * 检查是否所有权限都已授权
     *
     * @param context     上下文
     * @param permissions 权限列表
     * @return 是否所有权限都已授权
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        assetContextNotNull(context);
        assetPermissionsNotNull(permissions);

        if (isLessAndroidM()) {
            return true;
        }

        for (String permission : permissions) {
            if (!isPermissionGranted(context, permission)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 断言 context 不为空
     *
     * @param context 上下文
     */
    private static void assetContextNotNull(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null!!!");
        }
    }

    /**
     * 断言回调不为空
     *
     * @param callback 权限请求回调
     */
    private static void assetCallbackNotNull(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("permissions is null!!!");
        }
    }

    /**
     * 断言权限组不为空
     *
     * @param permissions 权限
     */
    private static void assetPermissionsNotNull(String... permissions) {
        if (permissions == null) {
            throw new IllegalArgumentException("permissions is null!!!");
        }
    }

    /**
     * 判断权限是否被授予
     *
     * @param context    上下文
     * @param permission 权限
     * @return permission 权限是否被授予
     */
    private static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否高于版本M
     */
    private static boolean isLessAndroidM() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    /**
     * 跳转到 App 的权限设置页
     *
     * @param activity    Activity
     * @param requestCode 请求码
     */
    public static void goAppSettingPage(Activity activity, int requestCode) {
        if (activity == null) {
            throw new IllegalArgumentException("activity is null!!!");
        }
        if (requestCode < 0) {
            throw new IllegalArgumentException("requestCode less than 0!!!");
        }
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 回调
     */
    public interface Callback {
        /**
         * 全部被授权
         */
        void onAllGranted();

        /**
         * 没有被授权的回调
         *
         * @param notGrantedList 没有被授权的列表
         * @param forbiddenList  被禁止不在询问的列表
         */
        void onNotGranted(List<String> notGrantedList, List<String> forbiddenList);
    }

}

package cn.zhoudl.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 权限管理器入口
 * <p>
 * Created by DaveZ 2018/11/15
 */
public class ZPermission {

    /**
     * 请求权限
     *
     * @param context        上下文
     * @param permissionList 权限列表
     * @param callback       授权回调
     */
    public static void requestPermissions(@NonNull Context context,
                                          @NonNull List<String> permissionList,
                                          @NonNull Callback callback) {
        int size = permissionList.size();
        if (size == 0) {
            callback.onAllGranted();
            return;
        }
        String[] permissions = permissionList.toArray(new String[size]);
        requestPermissions(context, permissions, callback);
    }

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
        if (permissions.length == 0 || !isOverAndroidM()) {
            callback.onAllGranted();
            return;
        }

        List<String> permissionNotGranted = new ArrayList<>();
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                permissionNotGranted.add(permission);
            }
        }

        int permissionNotGrantedSize = permissionNotGranted.size();
        if (permissionNotGrantedSize == 0) {
            callback.onAllGranted();
            return;
        }

        PermissionActivity.startActivity(context,
                permissionNotGranted.toArray(new String[permissionNotGrantedSize]), callback);
    }

    /**
     * 检查是否所有权限都已授权
     *
     * @param context     上下文
     * @param permissions 权限列表
     * @return 是否所有权限都已授权
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查某个权限是否被授权
     *
     * @param context    上下文
     * @param permission 权限
     * @return permission是否被授权
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        if (!isOverAndroidM()) {
            return true;
        }
        int ret = ContextCompat.checkSelfPermission(context, permission);
        return ret == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否高于版本M
     */
    private static boolean isOverAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
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
         * 部分被授权
         */
        void onPartGranted(Map<String, Boolean> permissionMap);
    }

}

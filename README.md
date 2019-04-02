# PermissionHelper

PermissionHelper 是 Android 平台上的动态权限申请库。

## 使用

```java
// 调用此方法请求权限
PermissionHelper.requestPermissions(Context context, String[] permissions, Callback callback);
```

然后回调里面会返回哪些权限被授权，哪些没有。

```java
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
```

拿到回调后，可以根据项目需要去控制。

一般被禁止掉掉权限，我们无法在通过系统弹框请求权限，所以在项目中提供了一个方法可以跳转到对应的设置页，让用户手动去赋予权限。

```java
PermissionHelper.goAppSettingPage(Activity activity, int requestCode);
```
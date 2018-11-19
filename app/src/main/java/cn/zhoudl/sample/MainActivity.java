package cn.zhoudl.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Map;

import cn.zhoudl.permission.ZPermission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void requestPermission(View view) {
        ZPermission.requestPermissions(this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE},
                new ZPermission.Callback() {
                    @Override
                    public void onAllGranted() {

                    }

                    @Override
                    public void onPartGranted(Map<String, Boolean> permissionMap) {

                    }
                });
    }
}

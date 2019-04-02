package cn.zhoudl.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import cn.zhoudl.permission.PermissionHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void requestPermission(View view) {
        PermissionHelper.requestPermissions(this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE},
                new PermissionHelper.Callback() {
                    @Override
                    public void onAllGranted() {

                    }

                    @Override
                    public void onNotGranted(List<String> notGrantedList, List<String> forbiddenList) {

                    }
                });
    }
}

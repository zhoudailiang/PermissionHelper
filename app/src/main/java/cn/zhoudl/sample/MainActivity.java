package cn.zhoudl.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
                        Toast.makeText(MainActivity.this, "所有权限都被授权", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNotGranted(List<String> notGrantedList, List<String> forbiddenList) {
                        TextView notGrantedTv = findViewById(R.id.not_granted_content);
                        notGrantedTv.setText(getString(notGrantedList));

                        TextView forbidTv = findViewById(R.id.forbid_content);
                        forbidTv.setText(getString(forbiddenList));
                    }
                });
    }

    private String getString(List<String> list) {
        StringBuilder sb = new StringBuilder();

        int len = list == null ? 0 : list.size();
        for (int i = 0; i < len; i++) {
            sb.append(list.get(i));
            sb.append(" ");
        }

        return sb.toString();
    }
}

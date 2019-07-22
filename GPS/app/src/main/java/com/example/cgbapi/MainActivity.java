package com.example.cgbapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button intent_btn;

    // 위치 권한 허용을 위한 설정 엑티비티의 일부분 중 하나입니다. 쉽게 설정 창의 번호라고 생각하면 편합니다. (=2002)
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 권한이 있는지 확인합니다.
        // 어플리케이션을 처음 실행하거나, 초기 권한 요청이 필요할 때, ACCESS_FINE_LOCATION에 대한 권한에 대해서 사용자에게 직접 권한 동의를 받아야 합니다.
        // 안드로이드 6.0 이후로부터는 보안 강화를 이유로 이 과정이 필수로 필요합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        // 만약에 권한이 거부되었다면, 해당 권한에 대해서 사용자에게 다시 허가 요청을 보냅니다.
        // "CGBAPI애서 내 기기 위치에 엑세스하도록 허용하시겠습니까?" 메세지가 나오면서, 거부 / 허용 여부를 물어봅니다.
        if(hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        intent_btn = findViewById(R.id.intent_btn);
        intent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SubActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.cgbapi;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * 호출되는 순서:
 * 1. onCreate 호출
 * 2. onStart 호출 후, Google API에 연결 시도
 *
 * Google API 빌더에 연결 요청 이후에 연결에 성공했다면
 * 3-1. onConnected 함수 호출
 *
 * Google API 빌더에서 Google Maps 서비스에 대해 준비가 끝나면
 * 4. onMapReady 함수 호출
 *
 * 연결에 실패했다면
 * 3-2. onConnectionFailed 함수 호출
 */
public class SubActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleMap googleMap = null;
    private GoogleApiClient googleApiClient = null;
    private Marker currentMarker = null;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        googleApiClient = new GoogleApiClient.Builder(this)
                // onConnected, onConnectionSuspended 함수를 이 클래스에서 정의해줬습니다. 따라서 this를 통해 지정해줍니다.
                .addConnectionCallbacks(this)
                // 연결에 실패하였을 때 호출되는 함수 onConnectionFailed 함수를 이 클래스에서 정의해줬습니다. 따라서 this를 통해 지정해줍니다.
                .addOnConnectionFailedListener(this)
                // 우리가 사용할 API는 위치 서비스와 관련된 것입니다. 따라서 LocationServices.API를 추가해줍니다.
                .addApi(LocationServices.API)
                .build();
        // 레이아웃으로부터 Google Map을 출력해주는 Fragment를 가져옵니다.
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        if(googleApiClient != null && !googleApiClient.isConnected()) googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(googleApiClient.isConnected()) googleApiClient.disconnect();
        super.onStop();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Google API 서비스 빌더와 관련된 함수입니다.
    // Google API 서비스 빌더는 Google API를 이용할 수 있도록 서비스를 연결해주는 녀석입니다.
    // Google 지도를 활용하기 위해서는 반드시 Google API 서비스 빌더에 Google Map과 관련한 서비스를 요청해줘야 합니다.

    /**
     * Google API 서비스 빌더에 연결되었을 때 실행되는 함수입니다.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // 위치 권한이 있는지 확인합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // ACCESS_FINE_LOCATION에 대한 권한이 없다면
        if(hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {
            Toast toast = Toast.makeText(this, "권한 거부시 위치 서비스를 이용할 수 없습니다!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        // 권한이 있다면, 위치에 대해서 접근할 수 있으므로 위치를 업데이트 하고, google Map에 위치를 업데이트 합니다.
        startLocationUpdates();
    }

    /**
     * Google API 서비스 빌더에 연결이 중지되었을 때 실행되는 함수입니다.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Toast toast = Toast.makeText(this,"구글 API 서비스 연결이 중지되었습니다.", Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Google API 서비스 빌더에 연결을 실패하였을 때 실행되는 함수입니다.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast toast = Toast.makeText(this,"구글 API 서비스 연결에 실패하였습니다. 잠시 후 다시 시도하세요.", Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 위치가 변경될 때 호출되는 이벤트 함수입니다. 사용자의 위치가 바뀔 때마다 지속적으로 호출됩니다.
     * location 변수에는 사용자의 위치 정보를 담고 있습니다.
     */
    @Override
    public void onLocationChanged(Location location) {
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        String markerTitle = this.getCurrentAddress(currentPosition);
        String markerSnippet = "위도: " + location.getLatitude() + " 경도: " + location.getLongitude();

        //현재 위치에 마커를 생성하고 카메라 관점을 현재 위치로 이동합니다.
        setCurrentLocation(location, markerTitle, markerSnippet);
    }

    /**
     * LatLng 변수로 부터 실제 주소지를 분석합니다.
     * 경도, 위도 값을 통해 주소를 추정합니다.
     */
    public String getCurrentAddress(LatLng latlng)
    {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0);
        }
    }

    /**
     * 현재 위치로 지도 마커 및 카메라 위치를 바꿉니다.
     */
    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if(currentMarker != null) currentMarker.remove();
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        currentMarker = googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        googleMap.moveCamera(cameraUpdate);
    }


    /**
     * "Google Map"에 대한 서비스 준비가 완료되었을 때 실행될 함수입니다.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.setDefaultLocation();
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    /**
     * 지도에 마커 및 카메라 위치들을 기본 위치로 설정합니다.
     */
    public void setDefaultLocation() {
        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";
        if (currentMarker != null) currentMarker.remove();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        googleMap.moveCamera(cameraUpdate);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 위치 정보를 가져오기 위해서, GPS 및 네트워크 서비스가 현재 제공되고 있는지 확인합니다.
     * 반환값이 거짓이라면, 위치 정보를 가져올 수 있는 환경이 되지 않는다는 것을 의미합니다.
     * 참이라면, 현재 위치 정보를 가져올 수 있습니다.
     */
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * GPS를 활성화할 것인지 물어봅니다.
     * 처음에 권한을 허가받았다고 해도, 개별 권한 요청이 추가로 필요합니다.
     * 예를 들어, 스마트폰에 GPS 기능을 꺼놨다면 해당 기능이 동작할 것입니다.
     */
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SubActivity.this);
        builder.setTitle("위치 서비스 비활성화됨");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n위치 설정에 대해서 수정 하시겠습니까?");

        // 취소도 가능하도록 설정합니다.
        builder.setCancelable(true);

        // 긍정 응답에 대한 버튼에 대해서 설정합니다.
        builder.setPositiveButton("설정하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // GPS를 활성화하는 설정 메뉴로 넘어갑니다.
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        // 부정 응답에 대한 버튼에 대해서 설정합니다.
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // 아무것도 안합니다.
                dialog.cancel();
            }
        });
        // 창을 띄웁니다.
        builder.create().show();
    }

    /**
     * 위치 업데이트를 시작합니다.
     */
    private void startLocationUpdates() {
        // 사용자로부터 어플리케이션에 대한 권한 부여를 받았지만 권한 허가에 대해서 물어봐야 합니다.
        // 만약에 서비스 제공이 가능한 상황이 아니라면
        if (!checkLocationServicesStatus()){
            // 사용자에게 위치 서비스를 위해서, 각각의 권한에 대해 허용할 것인지 물어봅니다.
            showDialogForLocationServiceSetting();
        }
        else {
            // 디바이스는 현재 위치 정보를 제공할 수 있는 환경을 갖추고 있습니다.
            // 하지만 만약에 어플리케이션 메니페스트(AndroidManifest.xml)에서 위치 찾기에 대한 권한이나 기지국 정보를 확인 가능한 권한이 추가되어 있지 않다면
            // 위치 정보를 받아올 수 없습니다. 따라서 실행하지 못하도록 오류 메세지를 출력시켜야 합니다.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast toast = Toast.makeText(this, "메니페스트에 대한 권한이 허가되지 않았습니다. 권한을 허가하여 주세요.",  Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            // 지속적인 위치 요청을 보냅니다.
            // 그 딜레이는 1초(1000ms)로 설정되어 있습니다.
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(1000)
                    .setFastestInterval(500);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            googleMap.setMyLocationEnabled(true);
        }
    }
}
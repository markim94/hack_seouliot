package markim.bluecadi.seouliothack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends NMapActivity implements NMapView.OnMapStateChangeListener, NMapPOIdataOverlay.OnStateChangeListener {



    // FireService fireService;


    String phoneNumber;
    private ViewGroup mapLayout;
    private NMapView mMapView;
    private NMapController mMapController;
    private NMapResourceProvider nMapResourceProvider;
    private NMapOverlayManager mapOverlayManager;


    // 네이버 맵
    NMapViewerResourceProvider mMapViewerResourceProvider = null; //NMapViewerResourceProvider 클래스 상속
    NMapOverlayManager mOverlayManager; //지도 위에 표시되는 오버레이 객체를 관리한다.
    NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = null; //POI 아이템의 선택 상태 변경 시 호출되는 콜백 인터페이스를 정의한다.
    NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener; //말풍선 오버레이 객체 생성 시 호출되는 콜백 인터페이스를 정의한다.
    NMapLocationManager mMapLocationManager; //단말기의 현재 위치 탐색 기능을 사용하기 위한 클래스이다.
    //NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener;
    NMapCompassManager mMapCompassManager; //단말기의 나침반 기능을 사용하기 위한 클래스이다.
    NMapMyLocationOverlay mMyLocationOverlay; //지도 위에 현재 위치를 표시하는 오버레이 클래스이며 NMapOverlay 클래스를 상속한다.

    double mLongitude;
    double mLatitude;

    ImageButton btnInfo;
    private OnDataProviderListener onDataProviderListener;
    private NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapLayout = findViewById(R.id.mapLayout);

        mMapView = new NMapView(this);
        mMapView.setClientId(getResources().getString(R.string.NAVER_API_KEY)); // 클라이언트 아이디 값 설정
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.setScalingFactor(1.5f);
        mMapView.requestFocus(1);

        mapLayout.addView(mMapView);

        //지도 객체로부터 컨트롤러 추출
        mMapController = mMapView.getMapController();

        //확대/축소를 위한 줌 컨트롤러 표시 옵션 활성화
        mMapView.setBuiltInZoomControls(true, null);


        // compass manager
        mMapCompassManager = new NMapCompassManager(this);


        //create resource provider(오버레이 객체)
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this); //NMapViewerResourceProvider 클래스 상속
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider); //오버레이 객체를 화면에 표시하기 위하여 NMapResourceProvider 클래스를 상속받은 resourceProvider 객체를 전달한다
        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener); //말풍선 오버레이 객체 생성 시 호출되는 콜백 인터페이스를 설정한다.

        int markerId = NMapPOIflagType.PIN;
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider); //전체 POI 아이템의 개수와 NMapResourceProvider를 상속받은 클래스를 인자로 전달한다.
        poiData.beginPOIdata(2); //POI 아이템 추가를 시작한다.


        // 2개의 마커 설정
        NMapPOIitem item1 = poiData.addPOIitem(126.896091, 37.480655, "서울IoT센터", markerId, 0); //POI아이템 설정
        item1.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW); //마커 선택 시 표시되는 말풍선의 오른쪽 아이콘을 설정한다.
        item1.hasRightAccessory(); //마커 선택 시 표시되는 말풍선의 오른쪽 아이콘 설정 여부를 반환한다.
        item1.setRightButton(true); //마커 선택 시 표시되는 말풍선의 오른쪽 버튼을 설정한다.
        item1.showRightButton(); //마커 선택 시 표시되는 말풍선의 오른쪽 버튼 설정 여부를 반환한다.

        NMapPOIitem item2 = poiData.addPOIitem(126.887369, 37.478016, "가산마리오아울렛", markerId, 0);
        item2.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
        item2.hasRightAccessory();
        item2.setRightButton(true);
        item2.showRightButton();

        poiData.endPOIdata(); //POI 아이템 추가를 종료한다.
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null); //POI 데이터를 인자로 전달하여 NMapPOIdataOverlay 객체를 생성한다.
        poiDataOverlay.showAllPOIdata(10); //POI 데이터가 모두 화면에 표시되도록 지도 축척 레벨 및 지도 중심을 변경한다. zoomLevel이 0이 아니면 지정한 지도 축척 레벨에서 지도 중심만 변경한다.
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener); //POI 아이템의 선택 상태 변경 시 호출되는 콜백 인터페이스를 설정한다.

        // set data provider listener
        super.setMapDataProviderListener(onDataProviderListener); //지도 라이브러리에서 제공하는 서버 API 호출 시 응답에 대한 콜백 인터페이스를 설정한다.

        //locationManager(위치매니저)
        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        // compass manager(나침반 매니저)
        mMapCompassManager = new NMapCompassManager(this);

        // create my location overlay(내 위치 오버레이)
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);


        ImageButton btnyes = (ImageButton) findViewById(R.id.btnyes);
        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "해당 건물로 적용됐습니다", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton btnno = (ImageButton) findViewById(R.id.btnno);
        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "새로고침 버튼 혹은 위치정보를 켜주세요", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton btnself = (ImageButton) findViewById(R.id.btnself);
        btnself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "직접 건물을 선택해주세요", Toast.LENGTH_SHORT).show();
            }
        });


        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("이용을 위해 사용자 연락처와 위치정보가 필요합니다")
                .setDeniedMessage("[설정]>[권한] 에서 연락처와 위치 권한 설정을 허용해주세요")
                .setPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)
                .check();


        btnInfo = (ImageButton) findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });


        /**
         * 서비스 connection

        ServiceConnection conn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                // 서비스와 연결되었을 때 호출되는 메서드
                // 서비스 객체를 전역변수로 저장
                FireService.LocalBinder mb = (FireService.LocalBinder) service;
                fireService = mb.getService(); // 서비스가 제공하는 메소드 호출하여
                // 서비스쪽 객체를 전달받을수 있슴
                isService = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                // 서비스와 연결이 끊겼을 때 호출되는 메서드
                isService = false;
                Toast.makeText(getApplicationContext(),
                        "서비스 연결 해제",
                        Toast.LENGTH_LONG).show();
            }
        };
         */


        /**
         * phonenum 획득
         TelephonyManager telManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
         if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
         return;
         }
         phoneNumber = telManager.getLine1Number();

         if (phoneNumber.startsWith("+82")) {
         phoneNumber = phoneNumber.replace("+82", "0");
         }

         // phonenum 확인
         Log.i("phonenum", phoneNumber);

         */

    }


    /**
     * Ted Permission
     */
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허가된 이용자입니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }

    };

    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {

    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {


    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    @Override
    public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

    }

    @Override
    public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

    }

    /**
     * AsyncTask


    // asynctask onprogressupdate 미사용으로 void
    public class PhoneNumberTask extends AsyncTask<String, Void, String> {
        String msg = "";

        // 백그라운드 작업이 시작되기 전
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        //pre실행되고 나서 동작
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];

            if (type.compareTo("1") == 0) {
                TelephonyManager telManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }

                phonenumber = telManager.getLine1Number();

                if (phonenumber.startsWith("+82")) {
                    phonenumber = phonenumber.replace("+82", "0");
                }

                // 쌍으로 키와 값을 저장하는 HashMap
                Map<String, String> map = new HashMap<>();
                // 키 id에 id를 put(입력)
                map.put("phonenumber", phonenumber);

                // urls "users"에 phonenumber 데이터 보냄. 요청방식은 get
                JSONObject json = SC.Json_Messenger("users", SC.GET, map);

                try {
                    // 데이터 긁어오기
                    firefighter = json.get("firefighter").toString();
                    return "1";


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "1";
            }
            return null;
        }


        // 백그라운드 작업 완료후 결과값을 얻음.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // 1출력시 정상접속. 이전에 받아온 firefighter가 0과 1일경우 분류해서 인텐트 전환
            if (result.compareTo("1") == 0) {
                //로그인 성공
                if (firefighter.equals("0")) { //일반 사용자
                    // 서비스 시작
                    Intent intent = new Intent(context, FireService.class);
                    intent.putExtra("firefighter",firefighter);
                    bindService(intent, // intent 객체
                            conn, // 서비스와 연결에 대한 정의
                            Context.BIND_AUTO_CREATE);
                    startService(intent);
                } else if (firefighter.equals("1")) { //소방관
                    // 서비스 시작
                    Intent intent = new Intent(context, FireService.class);
                    intent.putExtra("firefighter",firefighter);
                    bindService(intent, // intent 객체
                            conn, // 서비스와 연결에 대한 정의
                            Context.BIND_AUTO_CREATE);
                    startService(intent);
                }

            }
        }

    }

     */



//    @Override
//    public void onMapViewInitialized(MapView mapView) {
//
//        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
//        mapView.setMapTileMode(MapView.MapTileMode.Standard);
//
//    }
//
//    @Override
//    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
//
//    }
//
//    @Override
//    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {
//
//    }

}


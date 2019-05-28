package org.androidtown.hotplace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private BackPressCloseHandler backPressCloseHandler;
    private DrawerLayout drawer;

    NavigationView navigationView;
    GoogleMap mMap;

    LocationManager locationManager;
    double mLatitude; //위도
    double mLongitude; //경도
    TextView AddressTextview;
    private String userLocation;

    private FirebaseAuth userAuth = FirebaseAuth.getInstance(); //로그인한 사용자 정보
    final FirebaseUser user = userAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    CircleImageView userprofile;
    private TextView username;

    ImageView WeatherIconImg;
    TextView now_temp;
    TextView max_temp;
    TextView min_temp;

    String name_str;
    int profile_state;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TrafficMapFragment trafficmapFragment;

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://hot-place-231908.appspot.com/");
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference;

    String username_for_marker;
    String friend_maker_title;

    private String memo_date_for_save;
    private String memo_date_yyyymmdd;
    private String memo_date_hh;
    private String memo_date_mm;
    private double memo_latitude;
    private double memo_longitude;

    long mNow;
    Date mDate;
    SimpleDateFormat nFormat = new SimpleDateFormat("yyyyMMdd");

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        AddressTextview = (TextView) findViewById(R.id.address_textview);
        navigationView.setNavigationItemSelectedListener(this);

        backPressCloseHandler = new BackPressCloseHandler(this);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        //GPS가 켜져있는지 확인
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
            finish();
        }

        //마시멜로 이상일 경우
        if(Build.VERSION.SDK_INT >= 23) {
            //권한 없는 경우
            if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION} , 1);
            }
            //권한이 있는 경우
            else {
                requestMyLocation();
            }
        } //마시멜로 아래
        else {
            requestMyLocation();
        }

        //navigation view
        View nav_header = navigationView.getHeaderView(0);
        View nav_header_view = LayoutInflater.from(this).inflate(R.layout.navigation_header, null, false);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.removeHeaderView(nav_header);
        navigationView.addHeaderView(nav_header_view);

        ViewGroup nav_head = (ViewGroup) nav_header_view.findViewById(R.id.nav_header_layout);
        nav_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserinfoList(v);
            }
        });

        //user info
        username = (TextView) nav_header_view.findViewById(R.id.user_name);
        userprofile = (CircleImageView) nav_header_view.findViewById(R.id.user_profile);

        database.getInstance().getReference("user_info").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user_ = dataSnapshot.getValue(User.class);
                name_str = user_.userName;
                profile_state = user_.userProfile;

                username.setText(name_str+"님");

                if(profile_state == 0)
                    userprofile.setImageResource(R.drawable.user_defaultprofile);
                else {
                    pathReference = storageReference.child(user.getUid()+"/ProfileImage/ProfilePhoto");
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageurl = uri.toString();
                            Glide.with(getApplicationContext())
                                    .load(imageurl)
                                    .into(userprofile);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });

        //날씨 정보 출력
        now_temp = (TextView) findViewById(R.id.weather_nowtemp);
        max_temp = (TextView) findViewById(R.id.weather_maxtemp);

        min_temp = (TextView) findViewById(R.id.weather_mintemp);
        WeatherIconImg = (ImageView) findViewById(R.id.weather_icon);

        //구글맵 생성
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    //액션바 메뉴
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_main, menu);
        return true;
    }

    //액션바 메뉴 선택시
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //share icon selected : click event for share item
        switch (item.getItemId()) {
            case R.id.share_kakao:
                Toast.makeText(this, "share for kakao", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_line:
                Toast.makeText(this, "share for line", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_facebook:
                Toast.makeText(this, "share for facebook", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_twitter:
                Toast.makeText(this, "share for twitter", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_instagram:
                Toast.makeText(this, "share for instagram", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //네비게이션 드로어 오픈하여 메뉴 선택 시
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.memo:
                openMemoList();
                break;
            case R.id.friends:
                openFriends();
                break;
            case R.id.setting:
                openSettings();
                break;
            case R.id.search:
                openSearch();
                break;
            case R.id.logout:
                AlertDialog.Builder logout_builder = new AlertDialog.Builder(this);
                logout_builder.setMessage("로그아웃 하시겠습니까?").setCancelable(false)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //로그아웃 "네" 클릭시
                                userAuth.signOut();
                                Toast.makeText(MainActivity.this ,"로그아웃되었습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                //android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //로그아웃 "아니오" 클릭시
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert = logout_builder.create();
                alert.show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //두번 눌렀을 때 종료
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            backPressCloseHandler.onBackPressed();
        }
    }

    //개인정보 출력 및 수정 창 띄우기
    public void openUserinfoList(View view) {
        Intent intent = new Intent(this, UserinfolistActivity.class);
        startActivity(intent);
    }

    //사용자가 작성한 메모 리스트 화면 띄우기
    public void openMemoList() {
        Intent intent = new Intent(this, MemoListActivity.class);
        startActivity(intent);
    }

    //친구 화면 띄우기
    public void openFriends() {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    //설정 화면 띄우기
    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    //검색 화면 띄우기
    public void openSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    //구글맵 레디
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //지도 타입 - 일반
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // 나의 위치 설정
        final LatLng position = new LatLng(mLatitude, mLongitude);

        //화면 중앙의 위치와 카메라 줌비율
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        database.getInstance().getReference("user_info").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User get = dataSnapshot.getValue(User.class);
                username_for_marker = get.userName + "님";
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(username_for_marker)
                        .snippet("메모를 추가하시려면 클릭하세요 !")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                marker.showInfoWindow();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });

        database.getInstance().getReference("Friends_info").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    for(DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                        final String friend_uid = friendSnapshot.getValue().toString();
                        database.getInstance().getReference("user_info").child(friend_uid).addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 final User friend_get = dataSnapshot.getValue(User.class);
                                 final User_UID friend_uid_class = new User_UID();
                                 friend_uid_class.userUID = friend_uid;
                                 database.getInstance().getReference("Memo").child(friend_uid).addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                         if(dataSnapshot.getChildrenCount() > 0) {
                                             for(DataSnapshot friend_memo_Snapshot : dataSnapshot.getChildren()) {
                                                 Memo_ memo = friend_memo_Snapshot.getValue(Memo_.class);
                                                 memo_date_for_save = memo.date;
                                                 memo_date_yyyymmdd = memo_date_for_save.substring(0,8);
                                                 memo_date_hh = memo_date_for_save.substring(9,11);
                                                 memo_date_mm = memo_date_for_save.substring(11,13);
                                                 memo_latitude = memo.location_Latitude;
                                                 memo_longitude = memo.location_Longitude;
                                                 final LatLng friend_position = new LatLng(memo_latitude, memo_longitude);
                                                 if(TodayDate().equals(memo_date_yyyymmdd)) { //오늘 올라온 메모만 표시
                                                     friend_maker_title = friend_get.userName + "님께서 " + memo_date_hh + "시" + memo_date_mm + "분에 메모를 올렸습니다!";
                                                     if(friend_get.userLocationOpenRange == true) {
                                                         Marker marker = mMap.addMarker(new MarkerOptions()
                                                                 .position(friend_position)
                                                                 .title(friend_maker_title));
                                                         marker.setTag(friend_uid_class.userUID);
                                                     }
                                                 }
                                             }
                                         }
                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError databaseError) {
                                        databaseError.getMessage();
                                     }
                                 });
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {
                                databaseError.getMessage();
                             }
                         });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }

    //오늘 날짜 불러오기 (현재)
    private String TodayDate() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return nFormat.format(mDate);
    }

    //구글맵 마커 클릭시
    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        mMap.animateCamera(center);
        return true;
    }

    //구글맵 정보창 클릭시
    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().equals(username_for_marker)) {
            AlertDialog.Builder CreateMemo_builder = new AlertDialog.Builder(this);
            CreateMemo_builder.setMessage("현재 위치에 메모를 추가하시겠습니까?").setCancelable(false)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //메모생성 '네' 클릭시
                            openMemo();
                        }
                    }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //메모생성 '아니요' 클릭시
                    dialog.dismiss();
                }
            });
            AlertDialog alert = CreateMemo_builder.create();
            alert.show();
        } else {
            friend_marker_memo(marker.getTag().toString(), marker.getTitle());
        }
    }

    //친구 마커 클릭 후 친구 메모 띄우기
    public void friend_marker_memo(String marker_friend_uid, String marker_title) {
        Intent intent = new Intent(MainActivity.this, FriendMarkerClickActivity.class);
        intent.putExtra("marker friend uid", marker_friend_uid);
        intent.putExtra("friend marker title", marker_title);
        startActivity(intent);
    }

    //메모 작성 화면 띄우기
    public void openMemo() {
        Intent intent = new Intent(MainActivity.this, MemoActivity.class);
        intent.putExtra("user location", userLocation + "," + mLatitude + "," + mLongitude);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //권한 요청후 응답 콜백
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        //ACCESS_COARSE_LOCATION 권한
        if(requestCode == 1) {
            //권한 받음
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestMyLocation();
            }
            //권한 못받음
            else {
                Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    //위치정보 구하기 리스너
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //나의 위치를 한번만 가져오기 위해
            locationManager.removeUpdates(locationListener);

            //위도 경도
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            //위도와 경도로 주소 찾기
            getAddress(mLatitude, mLongitude);

            //날씨 구하기
            find_weather(mLatitude, mLongitude);

            //교통 정보 제공 네이버 지도 생성
            trafficmapFragment = new TrafficMapFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putDouble("user_Latitude", mLatitude);
            bundle.putDouble("user_Longitude", mLongitude);
            trafficmapFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.traffic_map, trafficmapFragment);
            fragmentTransaction.commit();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("gps","onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    //나의 위치 요청
    public void requestMyLocation() {
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } //요청
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, locationListener);
    }

    //위도와 경도로 주소 찾기
    public void getAddress(double mLatitude, double mLongitude) {
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        String str = null;
        List<Address> address;

        try {
            if(geocoder != null) {
                address = geocoder.getFromLocation(mLatitude, mLongitude, 10);
                if(address != null && address.size() > 0) {
                    str = address.get(0).getAddressLine(0).toString();
                    address.get(0).getLocality();
                }
            }
        } catch (IOException e) {
            Toast.makeText(this, "현재 주소를 확인 할 수 없습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        userLocation = str;
        AddressTextview.setText(str);
    }

    //위도와 경도에 맞는 날씨 찾기
    public void find_weather(double mLatitude, double mLongitude) {
        String current_url = "https://api.openweathermap.org/data/2.5/weather?lat="+mLatitude+"&lon="+mLongitude+"&appid=e6b1f229824c3f4eac0cf8000177ecc2";

        JsonObjectRequest jor_current = new JsonObjectRequest(Request.Method.GET, current_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //current weater에서는 오늘 하루 현재의 온도와 습도, 바람 방향, 세기, 아이콘 등을 가져온다.
                try {
                    JSONObject main_object = response.getJSONObject("main"); //객체인 main 가져오기 (temp, pressure, humidity, temp_min, temp_max)
                    JSONArray array = response.getJSONArray("weather"); //배열인 weather 가져오기
                    JSONObject object = array.getJSONObject(0);
                    JSONObject wind_object = response.getJSONObject("wind"); //객체인 wind 가져오기 (speed, deg)

                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String humidity = String.valueOf(main_object.getDouble("humidity"));
                    String temp_max = String.valueOf(main_object.getDouble("temp_max"));
                    String temp_min = String.valueOf(main_object.getDouble("temp_min"));

                    String weather_icon = String.valueOf(object.getString("icon"));
                    ViewweatherIcon(weather_icon);

                    String wind_speed = String.valueOf(wind_object.getDouble("speed"));
                    String wind_deg = String.valueOf(wind_object.getDouble("deg"));

                    double temp_int = Double.parseDouble(temp);
                    double maxtemp_int = Double.parseDouble(temp_max);
                    double mintemp_int = Double.parseDouble(temp_min);

                    double centi1 = (temp_int - 273.15);
                    double centi2 = (maxtemp_int - 273.15);
                    double centi3 = (mintemp_int - 273.15);
                    int i1 = (int)centi1;
                    int i2 = (int)centi2;
                    int i3 = (int)centi3;

                    now_temp.setText(String.valueOf(i1));
                    max_temp.setText(String.valueOf(i2));
                    min_temp.setText(String.valueOf(i3));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor_current);
    }

    //날씨에 맞는 아이콘 출력
    public void ViewweatherIcon(String iconcode) {
        switch (iconcode) {
            case "01d" : //clear sky day
                WeatherIconImg.setImageResource(R.drawable.weather_sun);
                break;
            case "02d" : //few clouds day
                WeatherIconImg.setImageResource(R.drawable.weather_few_clouds_sun);
                break;
            case "03d" : //scattered clouds day
                WeatherIconImg.setImageResource(R.drawable.weather_clouds);
                break;
            case "04d" : //broken clouds day
                WeatherIconImg.setImageResource(R.drawable.weather_broken_clouds);
                break;
            case "09d" : //shower rain day
                WeatherIconImg.setImageResource(R.drawable.weather_shower_rain);
                break;
            case "10d" : //rain day
                WeatherIconImg.setImageResource(R.drawable.weather_rain_day);
                break;
            case "11d" : //thunderstorm day
                WeatherIconImg.setImageResource(R.drawable.weather_thuderstorm);
                break;
            case "13d" : //snow day
                WeatherIconImg.setImageResource(R.drawable.weather_snow);
                break;
            case "50d" : //mist day
                WeatherIconImg.setImageResource(R.drawable.weather_mist);
                break;
            case "01n" : //clear sky night
                WeatherIconImg.setImageResource(R.drawable.weather_night);
                break;
            case "02n" : //few clouds night
                WeatherIconImg.setImageResource(R.drawable.weather_few_clouds_nignt);
                break;
            case "03n" : //scattered clouds night
                WeatherIconImg.setImageResource(R.drawable.weather_clouds);
                break;
            case "04n" : //broken clouds night
                WeatherIconImg.setImageResource(R.drawable.weather_broken_clouds);
                break;
            case "09n" : //shower rain night
                WeatherIconImg.setImageResource(R.drawable.weather_shower_rain);
                break;
            case "10n" : //rain night
                WeatherIconImg.setImageResource(R.drawable.weather_rain_night);
                break;
            case "11n" : //thunderstorm night
                WeatherIconImg.setImageResource(R.drawable.weather_thuderstorm);
                break;
            case "13n" : //snow night
                WeatherIconImg.setImageResource(R.drawable.weather_snow);
                break;
            case "50n" : //mist night
                WeatherIconImg.setImageResource(R.drawable.weather_mist);
                break;
        }
    }
}

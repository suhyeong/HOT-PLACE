package org.androidtown.hotplace;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import static com.naver.maps.map.NaverMap.LAYER_GROUP_TRAFFIC;

public class TrafficMapFragment extends Fragment implements OnMapReadyCallback {

    private com.naver.maps.map.MapView mapView;
    NaverMap nMap;

    double user_Latitude; //위도
    double user_Longitude; //경도

    public TrafficMapFragment() {
        // Required empty public constructor
    }

    public static TrafficMapFragment newInstance() {
        TrafficMapFragment fragment = new TrafficMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_traffic_map, container, false);
        mapView = layout.findViewById(R.id.map_view);
        if(getArguments() != null) {
            user_Latitude = getArguments().getDouble("user_Latitude");
            user_Longitude = getArguments().getDouble("user_Longitude");
        }
        mapView.getMapAsync(this);
        return layout;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        nMap = naverMap;
        naverMap.setMapType(NaverMap.MapType.Basic);
        LatLng position = new LatLng(user_Latitude, user_Longitude);
        naverMap.moveCamera(CameraUpdate.scrollAndZoomTo(position, 13));
        naverMap.setLayerGroupEnabled(LAYER_GROUP_TRAFFIC, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

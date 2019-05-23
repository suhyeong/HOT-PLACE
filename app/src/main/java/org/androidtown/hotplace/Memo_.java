package org.androidtown.hotplace;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Memo_ {
    public String contents;
    public String date;
    public int photo_exist;
    public String location;
    public double location_Latitude; //위도
    public double location_Longitude; //경도

    public Memo_() {
        // Default constructor required for calls to DataSnapshot.getValue(Memo.class)
    }

    public Memo_(String contents, String date, int photo_exist, String location, double location_Latitude, double location_Longitude) {
        this.contents = contents;
        this.date = date;
        this.photo_exist = photo_exist;
        this.location = location;
        this.location_Latitude = location_Latitude;
        this.location_Longitude = location_Longitude;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Memo Contents", contents);
        result.put("Memo Date", date);
        result.put("Memo Photo exist", photo_exist);
        result.put("Memo Location", location);
        result.put("Memo Location Latitude", location_Latitude);
        result.put("Memo Location Longitude", location_Longitude);

        return result;
    }
}

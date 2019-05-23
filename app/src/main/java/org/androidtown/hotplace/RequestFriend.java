package org.androidtown.hotplace;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class RequestFriend {
    public String request_to;
    public String request_from;

    public RequestFriend() {
        // Default constructor required for calls to DataSnapshot.getValue(RequestFriend.class)
    }

    public RequestFriend(String request_to, String request_from) {
        this.request_to = request_to;
        this.request_from = request_from;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Friend Request to", request_to);
        result.put("Friend Request from", request_from);
        return result;
    }
}

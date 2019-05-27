package org.androidtown.hotplace;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User_UID {
    public String userUID;

    public User_UID() {
        // Default constructor required for calls to DataSnapshot.getValue(User_UID.class)
    }

    public User_UID(String userUID) {
        this.userUID = userUID;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userUID", userUID);
        return result;
    }
}

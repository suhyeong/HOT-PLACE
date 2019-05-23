package org.androidtown.hotplace;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    public String userEmail;
    public String userName;
    public String userBirth;
    public int userProfile;
    public int userMemoOpenRange; //0:공개 1:친구공개 2:비공개
    public boolean userLocationOpenRange; //true:공개 false:비공개

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)

    }

    public User(String userEmail, String userName, String userBirth, int userProfile, int userMemoOpenRange, boolean userLocationOpenRange) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userBirth = userBirth;
        this.userProfile = userProfile;
        this.userMemoOpenRange = userMemoOpenRange;
        this.userLocationOpenRange = userLocationOpenRange;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userEmail", userEmail);
        result.put("userName", userName);
        result.put("userBirth", userBirth);
        result.put("userProfile", userProfile);
        result.put("userMemoOpenRange", userMemoOpenRange);
        result.put("userLocationOpenRange", userLocationOpenRange);

        return result;
    }

}
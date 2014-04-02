package com.codepath.apps.mytwitterapp.models;

import java.io.Serializable;

import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

public class User implements Serializable {
    private static final long serialVersionUID = 6270162077468756896L;
    boolean defaultProfileImage;
    int friendsCount;
    String handle;
    long id;
    String name;
    String profileImageUrl;

    public static User fromJson(JSONObject json) {
        User u = new User();
        try {
            u.defaultProfileImage = Boolean.parseBoolean(json.getString("default_profile_image"));
            u.friendsCount = json.getInt("friends_count");
            u.handle = json.getString("screen_name");
            u.id = json.getLong("id");
            u.profileImageUrl = json.getString("profile_image_url");
            u.name = json.getString("name");
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("kuoj", "Error constructing User object from JSON");
        }
        return u;
    }

    public int getFriendsCount() { return friendsCount; }

    public String getHandle() { return handle; }

    public String getName() { return name; }

    public String getProfileImageUrl() { return profileImageUrl; }

    public long getUserId() { return id; }
}

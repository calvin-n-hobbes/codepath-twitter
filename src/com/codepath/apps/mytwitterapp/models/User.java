package com.codepath.apps.mytwitterapp.models;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class User implements Serializable {
    private static final long serialVersionUID = 6270162077468756896L;
    boolean defaultProfileImage;
    int followersCount;
    int friendsCount; // a.k.a. "following"
    String handle;
    long id;
    String name;
    String profileBannerUrl;
    String profileImageUrl;
    String tagLine = null;
    boolean hasTagLine = false;
    String displayUrl = null;
    boolean hasUrl = false;

    public static User fromJson(JSONObject json) {
        Log.v("kuoj-json-user", json.toString());
        User u = new User();
        try {
            u.defaultProfileImage = Boolean.parseBoolean(json.getString("default_profile_image"));
            u.followersCount = json.getInt("followers_count");
            u.friendsCount = json.getInt("friends_count");
            u.handle = json.getString("screen_name");
            u.id = json.getLong("id");
            u.name = json.getString("name");
            u.profileBannerUrl = json.optString("profile_banner_url");
            u.profileImageUrl = json.getString("profile_image_url");
            u.tagLine = json.getString("description");
            if ( !u.tagLine.isEmpty() ) u.hasTagLine = true;
            String profileUrl = json.optString("url");
            if ( !"null".equals(profileUrl) ) {
                // find URL entity and get its display URL
                Log.d("kuoj-json-user", "Processing user @" + u.handle + " with url " + profileUrl);
                JSONObject entities = json.getJSONObject("entities");
                JSONObject entryUrl = entities.getJSONObject("url");
                JSONArray urlEntities = entryUrl.getJSONArray("urls");
                for (int i = 0; i < urlEntities.length(); i++) {
                    String currentUrl = urlEntities.getJSONObject(i).optString("url"); 
                    if ( profileUrl.equals(currentUrl) ) {
                        u.displayUrl = urlEntities.getJSONObject(i).optString("display_url");
                        if (u.displayUrl.isEmpty()) u.displayUrl = profileUrl;
                        u.hasUrl = true;
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("kuoj-json-user", "Error constructing User object from JSON");
        }
        return u;
    }

    public int getFollowersCount() { return followersCount; }

    public int getFriendsCount() { return friendsCount; }

    public String getHandle() { return handle; }

    public String getName() { return name; }

    public String getProfileBannerUrl() { return profileBannerUrl; }

    public String getProfileImageUrl() { return profileImageUrl; }

    public String getTagLine() { return tagLine; }

    public String getUrl() { return displayUrl; }

    public long getUserId() { return id; }

    public boolean hasTagLine() { return hasTagLine; }

    public boolean hasUrl() { return hasUrl; }
}

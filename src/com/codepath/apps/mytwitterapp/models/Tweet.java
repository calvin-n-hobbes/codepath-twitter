package com.codepath.apps.mytwitterapp.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

public class Tweet {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

    String source;
    String text;
    Date timestamp;
    long tweetId;
    User user;

    public Tweet() {
        super();
    }

    public Tweet(JSONObject jsonObject) {
        super();

        String createdAt = null;
        try {
            this.text = jsonObject.getString("text");
            createdAt = jsonObject.getString("created_at");
            //SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
            this.timestamp = formatter.parse(createdAt);
            tweetId = jsonObject.getLong("id");
            this.user = User.fromJson(jsonObject.getJSONObject("user"));
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.e("kuoj", "Error constructing Tweet object from JSON");
        }
        catch (ParseException e) {
            Log.e("kuoj", "Error formatting creation date string '" + createdAt + "'");
            e.printStackTrace();
        }
    }

    public String getText() { return text; }

    public Date getTimestamp() { return timestamp; }

    public long getTweetId() { return tweetId; }

    public User getUser() { return user; }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            }
            catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Tweet tw = new Tweet(tweetJson);
            //tw.save();
            tweets.add(tw);
        }

        return tweets;
    }
}

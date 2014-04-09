package com.codepath.apps.mytwitterapp;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "vTyfBKkraOzyluyaptYU2g";
    public static final String REST_CONSUMER_SECRET = "FAxLU3jxGch2Emr6Jtke7KzUBkGiDv801bQppPs3Y8";
    public static final String REST_CALLBACK_URL = "oauth://mytwitterapp"; // Change this (here and in manifest)
    public static final int FETCH_SIZE = 20;
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
        getHomeTimelineWithCount(handler, FETCH_SIZE);
    }

    public void getHomeTimelineWithCount(AsyncHttpResponseHandler handler, int count) {
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(count));
        getHomeTimeline(handler, params);
    }

    public void getHomeTimelineWithMaxId(AsyncHttpResponseHandler handler, long maxId) {
        RequestParams params = new RequestParams();
        params.put("max_id", String.valueOf(maxId));
        getHomeTimeline(handler, params);        
    }

    private void getHomeTimeline(AsyncHttpResponseHandler handler, RequestParams params) {
        String url = getApiUrl("statuses/home_timeline.json");
        client.get(url, params, handler);
    }

    public void getMentions(AsyncHttpResponseHandler handler) {
        getMentionsWithCount(handler, FETCH_SIZE);
    }

    private void getMentionsWithCount(AsyncHttpResponseHandler handler, int count) {
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(count));
        getMentions(handler, params);
    }

    public void getMentionsWithMaxId(AsyncHttpResponseHandler handler, long maxId) {
        RequestParams params = new RequestParams();
        params.put("max_id", String.valueOf(maxId));
        getMentions(handler, params);
    }

    private void getMentions(AsyncHttpResponseHandler handler, RequestParams params) {
        String url = getApiUrl("statuses/mentions_timeline.json");
        client.get(url, params, handler);
    }

    public void getUserTimeline(long userId, AsyncHttpResponseHandler handler) {
        getUserTimelineWithCount(userId, handler, FETCH_SIZE);
    }

    public void getUserTimelineWithCount(long userId, AsyncHttpResponseHandler handler, int count) {
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(count));
        getUserTimeline(userId, handler, params);
    }

    public void getUserTimelineWithMaxId(long userId, AsyncHttpResponseHandler handler, long maxId) {
        RequestParams params = new RequestParams();
        params.put("max_id", String.valueOf(maxId));
        getUserTimeline(userId, handler, params);        
    }

    private void getUserTimeline(long userId, AsyncHttpResponseHandler handler, RequestParams params) {
        String url = getApiUrl("statuses/user_timeline.json");
        params.put("user_id", String.valueOf(userId));
        client.get(url, params, handler);
    }

    public void getCurrentUser(AsyncHttpResponseHandler handler) {
        String url = getApiUrl("account/verify_credentials.json");
        client.get(url, handler);
    }

    public void getUserInfoById(AsyncHttpResponseHandler handler, long userId) {
        String url = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("user_id", String.valueOf(userId));
        client.get(url, params, handler);
    }

    public void postTweet(String body, AsyncHttpResponseHandler handler) {
        String url = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);
        client.post(url, params, handler);
    }

    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
}
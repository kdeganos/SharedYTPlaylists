package com.trexarms.sharedytplaylists;

/**
 * Created by kdega on 8/9/2016.
 */
public class Constants {
    public static final String BASE_SEARCH_URL = "https://www.googleapis.com/youtube/v3/search";
    public static final String PART = "part";
    public static final String YOUTUBE_API_KEY = BuildConfig.YOUTUBE_API_KEY;
    public static final String API_PARAMETER = "key";
    public static final String SEARCH_PARAMETER = "q";

    public static final String FIREBASE_CHILD_USERS = "users";
    public static final String FIREBASE_CHILD_PLAYLISTIDS = "playlistIds";
    public static final String FIREBASE_CHILD_PLAYLISTS = "playlists";
    public static final String FIREBASE_CHILD_VIDEOS = "videoObjects";
}

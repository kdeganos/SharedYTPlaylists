package com.trexarms.sharedytplaylists.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kdega on 8/9/2016.
 */
public class PlaylistObj {
    public String playlistId;
    public String playlistName;
    public String ownerId;
    public List<VideoObj> videoObjects = new ArrayList<>();
    public List<String> videoIds = new ArrayList<>();
    public String timestamp;

    public List<String> sharedUsers = new ArrayList<>();


    public PlaylistObj() {}

    public PlaylistObj(String playlistName, Date timestamp, String ownerId) {

        this.playlistName = playlistName;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.timestamp = dateFormat.format(timestamp);
        this.ownerId = ownerId;

    }

    public PlaylistObj(List<VideoObj> videoObjects) {
        this.videoObjects = videoObjects;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public List<VideoObj> getVideos() {
        return this.videoObjects;
    }
    public void setVideos(List<VideoObj> videoObjects) {
        this.videoObjects = videoObjects;
    }

    public void addVideo(VideoObj video) {
        this.videoObjects.add(video);
    }

    public String getTimestamp() {
        return timestamp;
    }


    public List<String> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(List<String> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }

    public String getOwnerId() {
        return this.ownerId;
    }


    public List<String> getVideoIds() {
        return this.videoIds;
    }
    public void setVideoIds(List<String> videoIdss) {
        this.videoIds = videoIdss;
    }

    public void addVideoIds(String videoId) {
        this.videoIds.add(videoId);
    }
}

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
    public String ownerName;
    public List<VideoObj> videoObjects = new ArrayList<>();
    public List<String> videoIds = new ArrayList<>();
    public String timestamp;

    public List<String> sharedUserIds = new ArrayList<>();


    public PlaylistObj() {}

    public PlaylistObj(String playlistName, String timestamp, String ownerId, String ownerName, String playlistId) {

        this.playlistName = playlistName;
        this.timestamp = timestamp;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.playlistId = playlistId;

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


    public List<String> getSharedUserIds() {
        return sharedUserIds;
    }

    public void setSharedUsers(List<String> sharedUsers) {
        this.sharedUserIds = sharedUsers;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public String getOwnerName() {return this.ownerName;}


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

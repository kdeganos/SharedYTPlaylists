package com.trexarms.sharedytplaylists.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kdega on 8/9/2016.
 */
public class UserObj {
    String userId;
    String name;
    String email;
    List<String> ownedPlaylistIds = new ArrayList<>();
    List<String> sharedPlaylistIds = new ArrayList<>();
    List<String> friendIds = new ArrayList<>();

    public UserObj() {
    }

    public UserObj(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getName() {
        return name;
    }

    public List<String> getOwnedPlaylistIds() {
        return ownedPlaylistIds;
    }
    public void addOwnedPlaylistId(String playlistId) {
        this.ownedPlaylistIds.add(playlistId);
    }
    public void setOwnedPlaylistIds(List<String> playlistIds) {
        this.ownedPlaylistIds = playlistIds;
    }

    public List<String> getSharedPlaylistIds() {
        return sharedPlaylistIds;
    }
    public void addPlaylistId(String playlistId) {
        this.sharedPlaylistIds.add(playlistId);
    }
    public void setPlaylistIds(List<String> playlistIds) {
        this.sharedPlaylistIds = playlistIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getFriendIds() {
        return friendIds;
    }
    public void addFriendId(String friendId) {
        this.friendIds.add(friendId);
    }
    public void setFriendIds(List<String> friendIds) {
        this.friendIds = friendIds;
    }
}

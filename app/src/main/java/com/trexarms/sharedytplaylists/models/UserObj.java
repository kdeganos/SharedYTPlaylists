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
    List<String> playlistIds = new ArrayList<>();
    List<String> friendIds = new ArrayList<>();

    public UserObj() {
    }

    public UserObj(String userId, String name, String email, List<String> playlistIds) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.playlistIds = playlistIds;

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


    public List<String> getPlaylistIds() {
        return playlistIds;
    }
    public void addPlaylistId(String playlistId) {
        this.playlistIds.add(playlistId);
    }
    public void setPlaylistIds(List<String> playlistIds) {
        this.playlistIds = playlistIds;
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

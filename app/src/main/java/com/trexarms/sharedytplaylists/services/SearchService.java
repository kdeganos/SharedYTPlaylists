package com.trexarms.sharedytplaylists.services;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.trexarms.sharedytplaylists.Constants;
import com.trexarms.sharedytplaylists.models.VideoObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kdega on 8/10/2016.
 */
public class SearchService {
    public static final String TAG = SearchService.class.getSimpleName();

    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     *
     */
    public void findVideos(String queryTerm, Callback callback) {
//        https://www.googleapis.com/youtube/v3/search?part=snippet&q=cats&key=AIzaSyBlGhwpWvzeZrEzPNlNJl7WOi9O0Hs0vzc
//        https://www.googleapis.com/youtube/v3/search?part=snippet%2Cstatistics%2CcontentDetails&fields=items(id(kind%2CvideoId)%2Csnippet(description%2CpublishedAt%2Cthumbnails%2Fdefault%2Furl%2Ctitle))&q=cats&key={YOUR_API_KEY}
//        https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=7&q=cats&type=video&fields=items%2Fid%2FvideoId&key={YOUR_API_KEY}


        OkHttpClient client = new OkHttpClient.Builder().build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.BASE_SEARCH_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.PART, "snippet");
        urlBuilder.addQueryParameter(Constants.SEARCH_PARAMETER, queryTerm);
        urlBuilder.addQueryParameter(Constants.API_PARAMETER, Constants.YOUTUBE_API_KEY);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);
        call.enqueue(callback);

    }

    public ArrayList<VideoObj> processResults(Response response) {
        ArrayList<VideoObj> videos = new ArrayList<>();

        try {
            String jsonData = response.body().string();

            if (response.isSuccessful()) {
                JSONObject resultJSON = new JSONObject(jsonData);
                JSONArray itemsJSON = resultJSON.getJSONArray("items");
                for (int i = 0; i < itemsJSON.length(); i++) {
                    JSONObject videoJSON = itemsJSON.getJSONObject(i);
                    JSONObject snippetJSON = videoJSON.getJSONObject("snippet");
                    String videoId = videoJSON.getJSONObject("id").getString("videoId");
                    //2016-07-08T13:41:40.000Z
                    String publishedAt = snippetJSON.getString("publishedAt");
                    String title = snippetJSON.getString("title");
                    String description = snippetJSON.getString("description");
                    String thumbnail = snippetJSON.getJSONObject("thumbnails").getJSONObject("default").getString("url");

                    VideoObj video = new VideoObj(videoId, publishedAt, title, description, thumbnail);
                    videos.add(video);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videos;
    }


}

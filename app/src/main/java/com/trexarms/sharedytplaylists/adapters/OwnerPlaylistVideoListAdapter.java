package com.trexarms.sharedytplaylists.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.trexarms.sharedytplaylists.Constants;
import com.trexarms.sharedytplaylists.R;
import com.trexarms.sharedytplaylists.models.PlaylistObj;
import com.trexarms.sharedytplaylists.models.VideoObj;
import com.trexarms.sharedytplaylists.ui.OwnerPlaylistsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kdega on 8/28/2016.
 */
public class OwnerPlaylistVideoListAdapter extends RecyclerView.Adapter<OwnerPlaylistVideoListAdapter.VideoViewHolder> {
    public static final String TAG = VideoListAdapter.class.getSimpleName();
    private List<VideoObj> mVideos = new ArrayList<>();
    private Context mContext;
    private String mPlaylistName;
    private String mPlaylistId;
    private String mUId;

    public OwnerPlaylistVideoListAdapter(Context context, List<VideoObj> videos, String playlistName, String playlistId, String uId) {
        mContext = context;
        mVideos = videos;
        mPlaylistName = playlistName;
        mPlaylistId = playlistId;


        mUId = uId;

    }

    @Override
    public OwnerPlaylistVideoListAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        VideoViewHolder viewHolder = new VideoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OwnerPlaylistVideoListAdapter.VideoViewHolder holder, int position) {
        holder.bindVideo(mVideos.get(position));
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.thumbnailImageView)
        ImageView mthumbnailImageView;
        @Bind(R.id.titleTextView)
        TextView mTitleTextView;
        @Bind(R.id.descriptionTextView) TextView mDescriptionTextView;
        @Bind(R.id.publishedAtTextView) TextView mPublishedAtTextView;

        private Context mContext;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int itemPosition = getLayoutPosition();

            VideoObj video = mVideos.get(itemPosition);
            watchYoutubeVideo(video.getVideoId());

        }

        public void bindVideo(VideoObj video) {
            Picasso.with(mContext).load(video.getThumbnail()).into(mthumbnailImageView);
            mTitleTextView.setText(video.getTitle());
            mDescriptionTextView.setText(video.getDescription());
            mPublishedAtTextView.setText(video.getPublishedAt());
        }
    }

    public void clearData() {
        mVideos.clear();
    }

    public void watchYoutubeVideo(String id){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            mContext.startActivity(intent);
        }
    }
}
package com.trexarms.sharedytplaylists.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.trexarms.sharedytplaylists.ui.OwnerPlaylistsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kdega on 8/9/2016.
 */
public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.VideoViewHolder> {
    public static final String TAG = PlaylistListAdapter.class.getSimpleName();
    private List<PlaylistObj> mPlaylists = new ArrayList<>();
    private Context mContext;

    private String mUId;

    public PlaylistListAdapter(Context context, List<PlaylistObj> playlists, String uId) {
        mContext = context;
        mPlaylists = playlists;
        mUId = uId;

    }

    @Override
    public PlaylistListAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_list_item, parent, false);
        VideoViewHolder viewHolder = new VideoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PlaylistListAdapter.VideoViewHolder holder, int position) {
        holder.bindPlaylist(mPlaylists.get(position));
    }

    @Override
    public int getItemCount() {
        return mPlaylists.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.playlistNameTextView) TextView mPlaylistNameTextView;
        @Bind(R.id.playlistDateTextView) TextView mPlaylistDateTextView;

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

            final PlaylistObj playlist = mPlaylists.get(itemPosition);


           DatabaseReference playlistRef = FirebaseDatabase.getInstance()
                                    .getReference(Constants.FIREBASE_CHILD_PLAYLISTS).child(playlist.getPlaylistId());



            Intent intent = new Intent(mContext, OwnerPlaylistsActivity.class);
            intent.putExtra("playlistName", playlist.getPlaylistName());
            intent.putExtra("playlistId", playlist.getPlaylistId());
            mContext.startActivity(intent);

        }

        public void bindPlaylist(PlaylistObj playlist) {
            mPlaylistNameTextView.setText(playlist.getPlaylistName());
            mPlaylistDateTextView.setText(playlist.getTimestamp());
        }
    }

    public void clearData() {
        // clear the data
        mPlaylists.clear();
    }
}
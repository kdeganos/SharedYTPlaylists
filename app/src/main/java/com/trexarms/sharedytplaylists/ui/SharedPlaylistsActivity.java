package com.trexarms.sharedytplaylists.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trexarms.sharedytplaylists.Constants;
import com.trexarms.sharedytplaylists.R;
import com.trexarms.sharedytplaylists.adapters.OwnerPlaylistVideoListAdapter;
import com.trexarms.sharedytplaylists.adapters.PlaylistListAdapter;
import com.trexarms.sharedytplaylists.models.PlaylistObj;
import com.trexarms.sharedytplaylists.models.VideoObj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SharedPlaylistsActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = SharedPlaylistsActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mUserReference;
    DatabaseReference mPlaylistReference;

    private PlaylistListAdapter mAdapter;
    private Context mContext = this;

    private List<PlaylistObj> mPlaylists = new ArrayList<>();
    private String mUId;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_playlists);
        ButterKnife.bind(this);


        List<PlaylistObj> playlists = new ArrayList<>();

        mPlaylistReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_PLAYLISTS);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String name = user.getDisplayName();
                    mUId = user.getUid();
                    getSupportActionBar().setTitle("Shared Playlists");

                    mUserReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_USERS).child(mUId);

                    checkPlaylists();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            checkPlaylists();
//                        }
//                    });
//                    getPlaylists();
//                    new MyAsyncTask().execute();

//                    mPlaylistReference.addChildEventListener(
//                            new ChildEventListener() {
//                                @Override
//                                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                                    filterPlaylists(dataSnapshot.child("playlistId").getValue(String.class));
//                                    Log.d(TAG, "onChildAdded1: ");
////                                    getPlaylists();
//                                }
//
//                                @Override
//                                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                                    filterPlaylists(dataSnapshot.child("playlistId").getValue(String.class));
////                                    getPlaylists();
//                                }
//
//                                @Override
//                                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                                    filterPlaylists(dataSnapshot.child("playlistId").getValue(String.class));
////                                    getPlaylists();
//                                }
//
//                                @Override
//                                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//                                    filterPlaylists(dataSnapshot.child("playlistId").getValue(String.class));
////                                    getPlaylists();
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                    Toast.makeText(mContext, "Failed to load playlists.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                    );

                } else {
                }
            }
        };


    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            checkPlaylists();
        }
        @Override
        protected Void doInBackground(Void... params) {
            checkPlaylists();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

        }
    }

    @Override
    public void onClick(View v) {
//        if (v == mNewPlayListButton) {
//            final String newPlaylistName = mNewPlaylistName.getText().toString().trim();
//
//            DatabaseReference playlistPushRef = mPlaylistReference.push();
//            String pushId = playlistPushRef.getKey();
//            Date date = new Date();
//            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy 'at' HH:mm:ss");
//
//            PlaylistObj playlist = new PlaylistObj(newPlaylistName, dateFormat.format(date), mUId, pushId);
//
//            playlistPushRef.setValue(playlist);
//            mUserReference.child(pushId).setValue(true);
//            Intent intent = new Intent(SharedPlaylistsActivity.this, OwnerPlaylistsActivity.class);
//            intent.putExtra("playlistName", newPlaylistName);
//            intent.putExtra("playlistId", playlist.getPlaylistId());
//            startActivity(intent);
//
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shared_playlists, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionViewMyPlaylists) {
            Intent intent = new Intent(SharedPlaylistsActivity.this, MainActivity.class);
            intent.putExtra("uId", mUId);
            startActivity(intent);
        }
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SharedPlaylistsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if(mAdapter != null) {
            mAdapter.clearData();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if(mAdapter != null) {
            mAdapter.clearData();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void getPlaylists() {
        Log.d(TAG, "getPlaylists: " + String.valueOf(mPlaylists));
        mAdapter = new PlaylistListAdapter(getApplicationContext(), mPlaylists, mUId);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(SharedPlaylistsActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }


    private void checkPlaylists() {
        new Thread() {

            @Override
            public void run() {

                mPlaylistReference.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                mPlaylists = new ArrayList<>();
                                for (final DataSnapshot playlistSnapshot : snapshot.getChildren()) {
                                    mUserReference.child(Constants.FIREBASE_CHILD_SHARED_PLAYLISTS)
                                            .child((String) playlistSnapshot.child("playlistId").getValue())
                                            .addValueEventListener(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnap) {
                                                            if (dataSnap.getValue() != null) {
                                                                PlaylistObj playlist = filterPlaylists(playlistSnapshot);
                                                                if(!mPlaylists.contains(playlist)){

                                                                    mPlaylists.add(playlist);
                                                                    getPlaylists();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    }
                                            );

                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        }.start();
    }

    private PlaylistObj filterPlaylists(DataSnapshot playlistSnapshot) {

        String playlistName = (String) playlistSnapshot.child("playlistName").getValue();

        String timestamp = (String)playlistSnapshot.child("timestamp").getValue();

        String ownerId = (String) playlistSnapshot.child("ownerId").getValue();

        String ownerName = (String) playlistSnapshot.child("ownerName").getValue();

        String playlistId = (String) playlistSnapshot.child("playlistId").getValue();

        PlaylistObj playlist = new PlaylistObj(playlistName, timestamp, ownerId, ownerName, playlistId);

        return playlist;

//        mPlaylistReference.addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot snapshot) {
//                        for (final DataSnapshot playlistSnapshot : snapshot.getChildren()) {
//                            mUserReference.child(Constants.FIREBASE_CHILD_SHAREDPLAYLISTS)
//                                    .child((String)playlistSnapshot.child("playlistId").getValue())
//                                    .addValueEventListener(
//                                    new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnap) {
//                                            if(dataSnap != null) {
//                                                String playlistName = (String) playlistSnapshot.child("playlistName").getValue();
//
//                                                String timestamp = (String)playlistSnapshot.child("timestamp").getValue();
//
//                                                String ownerId = (String) playlistSnapshot.child("ownerId").getValue();
//
//                                                String playlistId = (String) playlistSnapshot.child("playlistId").getValue();
//
//                                                PlaylistObj playlist = new PlaylistObj(playlistName, timestamp, ownerId, playlistId);
//
//                                                mPlaylists.add(playlist);
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//
//                                        }
//                                    }
//                            );
//
//                        }
//
////                        getPlaylists(mPlaylists);
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdapter != null) {
            mAdapter.clearData();
            mAdapter.notifyDataSetChanged();
        }
    }
}

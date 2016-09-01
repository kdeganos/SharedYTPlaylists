package com.trexarms.sharedytplaylists.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trexarms.sharedytplaylists.Constants;
import com.trexarms.sharedytplaylists.R;
import com.trexarms.sharedytplaylists.adapters.OwnerPlaylistVideoListAdapter;
import com.trexarms.sharedytplaylists.adapters.VideoListAdapter;
import com.trexarms.sharedytplaylists.models.PlaylistObj;
import com.trexarms.sharedytplaylists.models.VideoObj;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OwnerPlaylistsActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = OwnerPlaylistsActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String mUId;
    private String mPlaylistName;
    private String mPlaylistId;
    private String mQuery;
    private List<VideoObj> mVideos = new ArrayList<>();

    private DatabaseReference mPlaylistReference;
    private OwnerPlaylistVideoListAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.videoSearchButton)
    Button mVideoSearchButton;
    @Bind(R.id.videoSearchEditText)
    EditText mVideoSearchEditText;
//    @Bind(R.id.actionAddUserSpinner) Spinner mAddUserSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_owner_playlists);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mPlaylistName = intent.getStringExtra("playlistName");
        mPlaylistId = intent.getStringExtra("playlistId");

        getSupportActionBar().setTitle(mPlaylistName);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mUId = user.getUid();
                    mPlaylistReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_PLAYLISTS)
                            .child(mPlaylistId).child(Constants.FIREBASE_CHILD_VIDEOS);

                    mPlaylistReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                for (DataSnapshot videoSnapshot : snapshot.getChildren()) {

                                    mVideos.add(videoSnapshot.getValue(VideoObj.class));
                                }
                                getVideos();
                            } else {
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError arg0) {
                        }
                    });
                } else {
                }
            }
        };

        mVideoSearchButton.setOnClickListener(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdapter != null) {
            mAdapter.clearData();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_playlist, menu);
        ButterKnife.bind(this);


//        Spinner mySpinnerSpinner = (Spinner)findViewById(R.id.actionAddUserSpinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mAddUserSpinner.setAdapter(adapter);


//        MenuItem menuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Intent intent = new Intent(OwnerPlaylistsActivity.this, SearchActivity.class);
//                intent.putExtra("searchTerms", query);
//                intent.putExtra("mPlaylistName", mPlaylistName);
//                intent.putExtra("uId", mUId);
//                startActivity(intent);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        return true;

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionAddUserSpinner) {
            Intent intent = new Intent(OwnerPlaylistsActivity.this, AddUserActivity.class);
            intent.putExtra("searchTerms", mQuery);
            intent.putExtra("playlistName", mPlaylistName);
            intent.putExtra("playlistId", mPlaylistId);
            intent.putExtra("uId", mUId);
            startActivity(intent);
            return true;

        }
        if (id == R.id.actionViewMyPlaylists) {
            Intent intent = new Intent(OwnerPlaylistsActivity.this, MainActivity.class);
            intent.putExtra("uId", mUId);
            startActivity(intent);
        }
        if (id == R.id.actionViewSharedPlaylists) {
            Intent intent = new Intent(OwnerPlaylistsActivity.this, SharedPlaylistsActivity.class);
            intent.putExtra("uId", mUId);
            startActivity(intent);
        }
        if (id == R.id.action_logout) {
            logout();
            return true;

        }
        return super.onOptionsItemSelected(item);    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(OwnerPlaylistsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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

    @Override
    public void onClick(View view) {
        if (view == mVideoSearchButton) {
            mQuery = mVideoSearchEditText.getText().toString();
            Intent intent = new Intent(OwnerPlaylistsActivity.this, SearchActivity.class);
            intent.putExtra("searchTerms", mQuery);
            intent.putExtra("playlistName", mPlaylistName);
            intent.putExtra("playlistId", mPlaylistId);
            intent.putExtra("uId", mUId);
            startActivity(intent);
        }
    }

    private void getVideos() {
        mAdapter = new OwnerPlaylistVideoListAdapter(this, mVideos, mPlaylistName, mPlaylistId, mUId);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(OwnerPlaylistsActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }
}


package com.trexarms.sharedytplaylists.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trexarms.sharedytplaylists.Constants;
import com.trexarms.sharedytplaylists.R;
import com.trexarms.sharedytplaylists.adapters.UserListAdapter;
import com.trexarms.sharedytplaylists.models.UserObj;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddUserActivity extends AppCompatActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;


    private String mPlaylistName;
    private String mPlaylistId;
    private String mOwnerUId;
    private String mSearchTerms;
    private UserListAdapter mAdapter;

    private DatabaseReference mUserReference;


    private ArrayList<UserObj> mUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mPlaylistName = intent.getStringExtra("playlistName");
        mOwnerUId = intent.getStringExtra("uId");
        mPlaylistId = intent.getStringExtra("playlistId");
        mSearchTerms = intent.getStringExtra("searchTerms");
        getSupportActionBar().setTitle("Share with another user");

        mUserReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_USERS);

        mUserReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                            String userId = (String) userSnapshot.child("userId").getValue();
                            String name = (String) userSnapshot.child("name").getValue();
                            String email = (String) userSnapshot.child("email").getValue();

                            UserObj userObj = new UserObj(userId, name, email);
                            mUsers.add(userObj);
                        }

                        mAdapter = new UserListAdapter(getApplicationContext(), mUsers, mPlaylistName, mOwnerUId, mPlaylistId);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(AddUserActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_general, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionViewMyPlaylists) {
            Intent intent = new Intent(AddUserActivity.this, MainActivity.class);
            intent.putExtra("uId", mOwnerUId);
            startActivity(intent);
        }
        if (id == R.id.actionViewSharedPlaylists) {
            Intent intent = new Intent(AddUserActivity.this, SharedPlaylistsActivity.class);
            intent.putExtra("uId", mOwnerUId);
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
        Intent intent = new Intent(AddUserActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

//    private void getUsers() {
//
//        mAdapter = new UserListAdapter(getApplicationContext(), mUsers, mPlaylistName, mOwnerUId);
//        mRecyclerView.setAdapter(mAdapter);
//        RecyclerView.LayoutManager layoutManager =
//                new LinearLayoutManager(AddUserActivity.this);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setHasFixedSize(true);
//    }
}

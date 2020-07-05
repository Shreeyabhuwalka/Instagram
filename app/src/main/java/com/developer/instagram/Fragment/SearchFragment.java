package com.developer.instagram.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.instagram.Adapter.TagAdapter;
import com.developer.instagram.Adapter.UserAdapter;
import com.developer.instagram.Model.User;
import com.developer.instagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SocialAutoCompleteTextView search_bar;
    private List<User> mUsers;
    private UserAdapter userAdapter;

    //Hash tags
    private RecyclerView recyclerViewTags;
    private List<String> mHashTags;
    private List<String> mHashTagsCount;
    private TagAdapter tagAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_users);
        search_bar = view.findViewById(R.id.search_bar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(),mUsers,true);
        recyclerView.setAdapter(userAdapter);

        readUsers();


        //For checking the text is input in search bar
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }
        });

        //........Hash tags........
        recyclerViewTags = view.findViewById(R.id.recycler_view_tags);
        recyclerViewTags.setHasFixedSize(true);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(getContext()));
        mHashTags = new ArrayList<>();
        mHashTagsCount = new ArrayList<>();
        tagAdapter = new TagAdapter(getContext(),mHashTags,mHashTagsCount);
        recyclerViewTags.setAdapter(tagAdapter);

        readTags();

    return view;
    }

    private void readTags() {

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference()
                .child("HashTags");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (TextUtils.isEmpty(search_bar.getText().toString()))
                {
                    mHashTagsCount.clear();
                    mHashTags.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                        mHashTags.add(snapshot.getKey());
                        mHashTagsCount.add(snapshot.getChildrenCount()+"");

                    }
                    tagAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readUsers() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(TextUtils.isEmpty(search_bar.getText().toString()))
                {
                    mUsers.clear();

                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                       User user = snapshot.getValue(User.class);
                       mUsers.add(user);
                    }
                userAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void searchUser (String s)
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("username")
                .startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    mUsers.add(user);

                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void filter(String text)
    {
        List<String> mSearchTags = new ArrayList<>();
        List<String> mSearchTagsCount = new ArrayList<>();

        for (String s:mHashTags)
        {
            if (s.toLowerCase().contains(text.toLowerCase()))
            {
                mSearchTags.add(s);
                mSearchTagsCount.add(mHashTagsCount.get(mHashTags.indexOf(s)));
            }

        }
        tagAdapter.filter(mSearchTags,mSearchTagsCount);
    }
}

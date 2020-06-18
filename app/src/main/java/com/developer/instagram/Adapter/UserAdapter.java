package com.developer.instagram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.instagram.Model.User;
import com.developer.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean isFragment;

    public UserAdapter(Context context, List<User> users, boolean isFragment) {
        mContext = context;
        mUsers = users;
        this.isFragment = isFragment;
    }

    private FirebaseUser firebaseUser;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        User users = mUsers.get(position);
        holder.btnFollow.setVisibility(View.VISIBLE);

        holder.fullname.setText(users.getName());
        holder.username.setText(users.getUsername());

        Picasso.get().load(users.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
        isFollowed(users.getId(),holder.btnFollow);

        if (users.getId().equals(firebaseUser.getUid()))
        {
            holder.btnFollow.setVisibility(View.GONE);
        }

    }

    private void isFollowed(final String id, final Button btnFollow) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow")
                .child(firebaseUser.getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(id).exists())
                {
                    btnFollow.setText("following");
                }
                else
                {
                    btnFollow.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView imageProfile;
        public TextView username;
        public TextView fullname;
        public Button btnFollow;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            btnFollow = itemView.findViewById(R.id.button_follow);



        }
    }
}

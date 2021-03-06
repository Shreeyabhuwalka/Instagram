package com.developer.instagram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.instagram.CommentActivity;
import com.developer.instagram.Model.Post;
import com.developer.instagram.Model.User;
import com.developer.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private Context mContext;
    private List<Post> mPosts;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item,parent,false);
        return new PostAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Post post = mPosts.get(position);
        Picasso.get().load(post.getImageUrl()).into(holder.postImage);
        holder.description.setText(post.getDescription());

        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(post.getPublisher())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);
                        Picasso.get().load(user.getImageUrl()).into(holder.imageProfile);
                        holder.username.setText(user.getUsername());
                        holder.author.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        isLiked(post.getPostId(),holder.like);
        noOfLikes(post.getPostId(),holder.noOfLikes);
        getComments(post.getPostId(),holder.noOfComments);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getTag().equals("Like"))
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).setValue(true);
                }
                else
                {

                    FirebaseDatabase.getInstance().getReference()
                            .child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId",post.getPostId());
                intent.putExtra("publisher",post.getPublisher());
                mContext.startActivity(intent);
            }
        });
        holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId",post.getPostId());
                intent.putExtra("publisher",post.getPublisher());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageProfile;
        public ImageView postImage;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
        public ImageView more;

        public TextView username;
        public TextView noOfLikes;
        public TextView author;
        public TextView noOfComments;
        SocialAutoCompleteTextView description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            more = itemView.findViewById(R.id.more);

            username = itemView.findViewById(R.id.username);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            author = itemView.findViewById(R.id.author);
            noOfComments = itemView.findViewById(R.id.no_of_comments);
            description = itemView.findViewById(R.id.description);

        }
    }
    private void isLiked(String postId, final ImageView imageView)
    {
        FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(postId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(firebaseUser.getUid()).exists())
                {

                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("Liked");
                }
                else
                {

                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("Like");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void noOfLikes(String postId, final TextView text)
    {
        FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        text.setText(dataSnapshot.getChildrenCount()+" likes");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void getComments(String postId,final TextView text)
    {
        FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText("View all "+dataSnapshot.getChildrenCount()+" Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

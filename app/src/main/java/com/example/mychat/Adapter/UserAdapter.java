package com.example.mychat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.Activity.ChatActivity;
import com.example.mychat.Activity.Home;
import com.example.mychat.ModelClass.Users;
import com.example.mychat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter <UserAdapter.ViewHolder>{
    Context homeActivity;ArrayList<Users> usersArrayList;
    public UserAdapter(Home homeActivity, ArrayList<Users> usersArrayList) {
        this.homeActivity=homeActivity;
        this.usersArrayList=usersArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(homeActivity).inflate(R.layout.item_user_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.ViewHolder holder, int position) {
        Users users= usersArrayList.get(position);

        if(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(users.getUid()))
        {
            holder.itemView.setVisibility(View.GONE);

        }






        holder.user_name.setText(users.name);
        holder.user_status.setText(users.status);
        Picasso.get().load(users.imageUri).into(holder.user_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(homeActivity, ChatActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReceiverImage",users.getImageUri());
                intent.putExtra("uid",users.getUid());
                homeActivity.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView user_image;
        TextView user_name;
        TextView user_status;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            user_image=itemView.findViewById(R.id.user_image);
            user_name=itemView.findViewById(R.id.user_name);
            user_status=itemView.findViewById(R.id.user_status);
        }
    }
}

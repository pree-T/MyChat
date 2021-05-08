package com.example.mychat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mychat.R;
import com.example.mychat.Adapter.UserAdapter;
import com.example.mychat.ModelClass.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView img_logout,img_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        auth=FirebaseAuth.getInstance();

        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(Home.this, Registration.class));
        }

        database=FirebaseDatabase.getInstance();

        usersArrayList=new ArrayList<>();

        DatabaseReference reference=database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Users users=dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        img_logout=findViewById(R.id.img_logout);
        mainUserRecyclerView=findViewById(R.id.mainUserRecyclerView);
        img_settings=findViewById(R.id.img_settings);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UserAdapter(Home.this,usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);

        img_logout.setOnClickListener(v -> {
            Dialog dialog =new Dialog(Home.this,R.style.Dialog);
            dialog.setContentView(R.layout.dialog_layout);
            TextView yes_btn,no_btn;
            yes_btn=dialog.findViewById(R.id.yes_btn);
            no_btn=dialog.findViewById(R.id.no_btn);
            yes_btn.setOnClickListener(v1 -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this,Login.class));
            });
            no_btn.setOnClickListener(v12 -> {
                dialog.dismiss();

            });
            dialog.show();

        });
        img_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,SettingsActivity.class));
            }
        });



    }
    @Override
    public void onBackPressed(){

        moveTaskToBack(true);

    }
}
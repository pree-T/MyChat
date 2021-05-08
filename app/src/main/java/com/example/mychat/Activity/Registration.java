package com.example.mychat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychat.R;
import com.example.mychat.ModelClass.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity {
    TextView signin,signup;
    CircleImageView profileimage;
    EditText namer,emailr,passr,cpassr;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    Uri imageUri;
    String imageURI;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();



        signin=findViewById(R.id.signin);
        profileimage=findViewById(R.id.profile_image);
        namer=findViewById(R.id.name);
        emailr=findViewById(R.id.email);
        passr=findViewById(R.id.pass);
        cpassr=findViewById(R.id.confirmpass);
        signup=findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name=namer.getText().toString();
                String email=emailr.getText().toString();
                String password=passr.getText().toString();
                String cpassword=cpassr.getText().toString();
                String status="Hey there!I am using My Chat.";

                if(TextUtils.isEmpty(email))
                {
                    progressDialog.dismiss();
                    emailr.setError("Email cannot be empty");
                    Toast.makeText(Registration.this,"Please enter valid data",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(name))
                {
                    namer.setError("Name cannot be empty");
                    Toast.makeText(Registration.this,"Please enter valid data",Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<6)
                {
                    progressDialog.dismiss();
                    passr.setError("Password cannot be smaller than 6 characters");
                    Toast.makeText(Registration.this,"Please enter valid data",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.matches(emailPattern)))
                {
                    progressDialog.dismiss();
                    emailr.setError("Invalid Email");
                    Toast.makeText(Registration.this,"Invalid Email",Toast.LENGTH_SHORT).show();

                }
                else if(TextUtils.isEmpty(cpassword))
                {

                    progressDialog.dismiss();
                    cpassr.setError("Confirm password cannot be empty");
                    Toast.makeText(Registration.this,"Invalid Data",Toast.LENGTH_SHORT).show();

                }
                else if(!cpassword.equals(password))
                {
                    progressDialog.dismiss();
                    cpassr.setError("Password and confirm password do not match");
                    passr.setError("Password and confirm password do not match");
                    Toast.makeText(Registration.this,"Invalid Data",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {

                            DatabaseReference reference=database.getReference().child("user").child(Objects.requireNonNull(auth.getUid()));
                            StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());
                            if(imageUri!=null)
                            {
                                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                                       if(task.isSuccessful())
                                       {

                                           storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                               @Override
                                               public void onSuccess(Uri uri) {
                                                   imageURI=uri.toString();
                                                   Users users=new Users(auth.getUid(),name,email,imageURI,status);
                                                   reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                           if(task.isSuccessful())
                                                           {
                                                               progressDialog.dismiss();
                                                               startActivity(new Intent(Registration.this, Home.class));
                                                           }
                                                           else
                                                           {
                                                               progressDialog.dismiss();
                                                               Toast.makeText(Registration.this,"Error in creating a new user",Toast.LENGTH_SHORT).show();
                                                           }

                                                       }
                                                   });

                                               }
                                           });
                                       }
                                    }
                                });
                            }
                            else
                            {

                                progressDialog.dismiss();

                                imageURI="https://firebasestorage.googleapis.com/v0/b/my-chat-431eb.appspot.com/o/profile.png?alt=media&token=30b97c9a-96a1-4f29-a132-4eec6699d3ae";
                                Users users=new Users(auth.getUid(),name,email,imageURI,status);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            startActivity(new Intent(Registration.this,Home.class));
                                        }
                                        else
                                        {
                                            Toast.makeText(Registration.this,"Error in creating a new user",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }
                        else
                        {
                            Toast.makeText(Registration.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

                }




            }
        });
        profileimage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
        });

        signin.setOnClickListener(v -> startActivity(new Intent(Registration.this, Login.class)));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            if(data!=null)
            {
                 imageUri=data.getData();
                profileimage.setImageURI(imageUri);
            }
        }
    }
}
package com.abdulkarim.userapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.abdulkarim.userapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private EditText fullNameEt, emailEt, passwordEt, confirmPasswordEt, phoneNumberEt;
    private ImageView profileIv;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        fullNameEt = findViewById(R.id.nameET);
        emailEt = findViewById(R.id.emailET);
        passwordEt = findViewById(R.id.passwordET);
        confirmPasswordEt = findViewById(R.id.confirmPasswordET);
        phoneNumberEt = findViewById(R.id.phoneET);
        profileIv = findViewById(R.id.profileIv);

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        findViewById(R.id.singUpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String confirmPassword = confirmPasswordEt.getText().toString().trim();

                if (email.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Password Not Match", Toast.LENGTH_SHORT).show();
                } else if (imageUri == null) {
                    Toast.makeText(SignUpActivity.this, "Please Select your profile picture", Toast.LENGTH_SHORT).show();
                } else {
                    signUp(email, password);
                }
            }
        });
    }

    private void signUp(final String email, final String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uploadProfileImage();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadProfileImage() {
        File file = new File(imageUri.getPath());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_images").child(file.getName());
        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            insertUser(imageUrl);
                        }
                    });

                }
            }
        });

    }

    private void insertUser(String imageUrl) {

        String name = fullNameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        String phoneNumber = phoneNumberEt.getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        Map<String, Object> userMap = new HashMap<>();

        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("password", password);
        userMap.put("phoneNumber", phoneNumber);
        userMap.put("image_url", imageUrl);


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);

        documentReference.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SignUpActivity.this, "Insert User success", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                profileIv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
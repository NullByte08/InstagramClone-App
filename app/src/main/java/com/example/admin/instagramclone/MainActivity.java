package com.example.admin.instagramclone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout emailText;
    private TextInputLayout passwordText;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    DatabaseReference ref;
    Intent intent;

    public boolean validateEmail(){
        String emailInput=emailText.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            emailText.setError("Field can't be empty");
            return false;
        }
        else {
            emailText.setError(null);
            return true;
        }
    }
    public boolean validatePassword(){
        String passwordInput=passwordText.getEditText().getText().toString().trim();
        if(passwordInput.isEmpty()){
            passwordText.setError("Field can't be empty");
            return false;
        }
        else {
            passwordText.setError(null);
            return true;
        }
    }
    public void login(View view){
        if(!validateEmail()|!validatePassword()){
            return;
        }
    }
    public void signup(View view){
        if(!validatePassword()|!validateEmail()){
            return;
        }
        else{

            ref = FirebaseDatabase.getInstance().getReference();
            ref.child("users").child(emailText.getEditText().getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // use "username" already exists
                        // Let the user know he needs to pick another username.
                        emailText.setError("Username or Email already exists, pick another!");
                    } else {
                            // User does not exist. NOW call createUserWithEmailAndPassword
                            // mAuth.createUserWithPassword(...);
                            // Your previous code here.
                            mAuth.createUserWithEmailAndPassword(emailText.getEditText().getText().toString(), passwordText.getEditText().getText().toString())
                                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                FirebaseUser user = mAuth.getCurrentUser();

                                                startActivity(intent);
                                            } else {
                                                // If sign in fails, display a message to the user.

                                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();

                                            }

                                            // ...
                                        }
                                    });
                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        }

    }
    public void hideKeyboard(View view){
        if(view.getId()!=R.id.textEmail||view.getId()!=R.id.textPassword){
            InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailText=findViewById(R.id.textEmail);
        passwordText=findViewById(R.id.textPassword);
        mAuth=FirebaseAuth.getInstance();
        intent=new Intent(this,HomeScreenActivity.class);
        

    }


}




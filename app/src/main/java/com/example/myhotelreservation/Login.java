package com.example.myhotelreservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button Login;
    TextInputLayout regUsername, regPassword;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        register = findViewById(R.id.register);
        Login = findViewById(R.id.Login);
        regPassword = findViewById(R.id.password);
        regUsername = findViewById(R.id.username);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity2.class);
                startActivity(intent);
                SignUp();
            }
        });

    }


    private Boolean validateUsername(){
        String val = regUsername.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            regUsername.setError("Field cannot be empty");
            return false;
        }
        else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            regPassword.setError("Field Cannot be empty");
            return false;
        }
        else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void SignUp() {

        if (!validateUsername() | !validatePassword()){
            return;
        }
        else {
            isUser();

        }
    }

    private void isUser() {
        final String userEnteredUsername = regUsername.getEditText().getText().toString().trim();
        final String userEnteredPassword = regPassword.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    regUsername.setError(null);
                    regUsername.setErrorEnabled(false);


                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    if(passwordFromDB.equals(userEnteredPassword)){

                        regPassword.setError(null);
                        regPassword.setErrorEnabled(false);

                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String phoneNoFromDB = snapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);

                        Toast.makeText(Login.this, nameFromDB+"\n"+emailFromDB+"\n"+phoneNoFromDB, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), SignUp.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        intent.putExtra("password", passwordFromDB);

                        startActivity(intent);

                    }
                    else {
                        regPassword.setError("Wrong Password!");
                        regPassword.requestFocus();
                    }
                }
                else {
                    regUsername.setError("No such User exists");
                    regUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }
}
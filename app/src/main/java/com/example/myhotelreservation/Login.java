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
    TextInputLayout username, password;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        register = findViewById(R.id.register);
        Login = findViewById(R.id.Login);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);

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
                SignUp();
            }
        });

    }


    private Boolean validateUsername(){
        String val = username.getEditText().getText().toString();

        if (val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        }
        else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            password.setError("Field Cannot be empty");
            return false;
        }
        else {
            password.setError(null);
            password.setErrorEnabled(false);
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
        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("phoneNo").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    if(passwordFromDB.equals(userEnteredPassword)){

                        username.setError(null);
                        username.setErrorEnabled(false);

                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String phoneNoFromDB = snapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        intent.putExtra("password", passwordFromDB);

                        startActivity(intent);
                        Toast.makeText(Login.this, "Login Successfully!", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        password.setError("Wrong Password!");
                        password.requestFocus();
                    }
                }
                else {
                    username.setError("No such User exists");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
package com.example.myhotelreservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextInputLayout regName, regPhoneNo, regEmail, regPassword;
    Button regRegister;
    TextView regLogin;

    FirebaseDatabase rootNode;
    DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        regName = findViewById(R.id.name);
        regEmail = findViewById(R.id.email);
        regPhoneNo = findViewById(R.id.phoneNo);
        regPassword = findViewById(R.id.password);
        regRegister = findViewById(R.id.register);
        regLogin = findViewById(R.id.Login);

// Once clicked, goes back to the Login screen
        regLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });


        //once clicked, saves data in firebase.

        regRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                registerUser();

                //Get all the values

                String name = regName.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNo = regPhoneNo.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();

                UserHelperClass helperClass = new UserHelperClass(name,phoneNo,email,password);
                reference.child(phoneNo).setValue(helperClass);

            }
        });                                  

    }
    private Boolean validateName(){
        String val = regName.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            regName.setError("Field cannot be empty");
            return false;
        }
        else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo(){
        String val = regPhoneNo.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            regPhoneNo.setError("Field Cannot be empty");
            return false;
        }
        else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = regEmail.getEditText().getText().toString().trim();
        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            regEmail.setError("Field Cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            regEmail.setError("invalid email address");
            return false;
        }
        else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString().trim();
        String passwordVal = "^" +


                //"(?=.*[a-zA-Z])" +              //any letter
                "(?=.*[a-z])"+                  //at least one lower case
                "(?=.*[A-Z])"+                  //at least one upper case
                "(?=.*[0-9])"+                  //at least one digit
                "(?=.*[@#$%^&+=])" +            //at least one special character
                "(?=\\S+$)" +                   //no white spaces
                ".{5,}";                        //at least 5 characters

        if (val.isEmpty()){
            regPassword.setError("Field Cannot be empty");
            return false;
        }
        else if(!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        }

        else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }


    private void registerUser() {

        if(!validateName() |!validatePassword() | !validatePhoneNo() | !validateEmail())
        {
            return;
        }

        String name = regName.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneNo = regPhoneNo.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        UserHelperClass helperClass = new UserHelperClass(name,phoneNo,email,password);
        reference.child(phoneNo).setValue(helperClass);
    }



}
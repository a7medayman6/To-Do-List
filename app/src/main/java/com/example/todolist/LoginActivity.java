package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;


public class LoginActivity extends AppCompatActivity
{

    EditText username;
    EditText password;
    Button login;
    SQLiteDatabase database;
    DbHandler dbh;
    String passedUsername;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        dbh = new DbHandler(this);

        username = findViewById(R.id.username_in);
        password = findViewById(R.id.password_in);
        login = findViewById(R.id.login);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                boolean valid = Validate(username.getText().toString(), password.getText().toString());

                if(valid)
                {

                    CharSequence text = "You've been Logged In Successfully.";

                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();

                    dbh.Login(username.getText().toString());

                    //passing the current username to Home Activity
                    passedUsername = username.getText().toString();
                    Intent UsernameToHome = new Intent(LoginActivity.this, Home.class);
                    UsernameToHome.putExtra("passedUsername", passedUsername);
                    startActivity(UsernameToHome);
                    finish();

                }
                else
                {
                    CharSequence text = "Wrong Username or Password.";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                    //try again
                }
            }
        });


    }

    private boolean Validate(String name, String pass)
    {
        if(name.isEmpty() || pass.isEmpty())
            return false;

        User user = dbh.SelectUser(name);

        if(user != null && pass.equals(user.password))
            return true;

        else
            return false;
    }

}
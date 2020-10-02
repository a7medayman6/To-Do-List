package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity
{
    EditText name;
    EditText username;
    EditText password;
    EditText passwordII;
    Button signup;
    Context context;
    int toast_duration;
    DbHandler dbh;
    Activity thisActivity;
    Intent signin_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        thisActivity = this;
        context = getApplicationContext();
        toast_duration = Toast.LENGTH_SHORT;
        dbh = new DbHandler(this);
        signin_intent = new Intent(this, LoginActivity.class);

        name = findViewById(R.id.name_in);
        username = findViewById(R.id.username_in);
        password = findViewById(R.id.password_in);
        passwordII = findViewById(R.id.password_inII);
        signup = findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Handler handler = new Handler(name,  username, password, passwordII, thisActivity, dbh);
                boolean valid = handler.Validate();
                if (valid)
                {
                    handler.Insert();
                    CharSequence text = "You've Signed Up Successfully.";
                    Toast toast = Toast.makeText(context, text, toast_duration);
                    toast.show();
                    startActivity(signin_intent);
                    finish();
                }

            }
        });
    }



}
class Handler
{
    String name;
    String username;
    String password;
    String passwordII;
    Context context;
    int toast_duration;
    //DbOperations db;
    DbHandler dbh;

    public Handler(EditText name, EditText username, EditText password, EditText passwordII, Context activity, DbHandler dbh)
    {
        this.name = name.getText().toString();
        this.username = username.getText().toString();
        this.password = password.getText().toString();
        this.passwordII = passwordII.getText().toString();
        context = activity;
        toast_duration = Toast.LENGTH_SHORT;
        this. dbh = dbh;
        //db = new DbOperations();
    }


    boolean Validate()
    {
        //CHECK IF NOTHING IS EMPTY
        if (name.equals("") || username.equals("") || password.equals("") || passwordII.equals(""))
        {
            Show("Please Fill Each Field.");
            return false;
        }
        //CHECK IF THE TWO PASSWORDS MATCH
        if(!password.equals(passwordII))
        {
            Show("The Two Password Must Be The Same.");
            return false;
        }
        //CHECK IF THE USERNAME IS UNIQUE
        if(dbh.IsThere(username))
        {
            Show("Sorry This Username is Taken.");
            return false;
        }
        if(password.length() < 6)
        {
            Show("Your Password Must Be At Least 6 Characters Long.");
            return false;
        }


        return true;
    }

    void Insert()
    {
        User user = new User(name, username, password);
        dbh.insertUserDetails(user);
    }
    void Show(CharSequence msg)
    {
        Toast toast = Toast.makeText(context, msg, toast_duration);
        toast.show();
    }
}
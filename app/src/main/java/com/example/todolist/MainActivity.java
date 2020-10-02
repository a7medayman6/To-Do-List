package com.example.todolist;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.Console;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    //DbOperations db = new DbOperations();

    DbHandler dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dbh = new DbHandler(this);
        //User admin = new User("admin", "admin", "admin");
        //dbh.insertUserDetails(admin);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        final Intent signin_intent = new Intent(this, LoginActivity.class);
        final Intent signup_intent = new Intent(this, Registration.class);


        User loggedinuser = dbh.Logged_in_user();
        if (loggedinuser != null)
        {
            String passedUsername = loggedinuser.username;
            Intent UsernameToHome = new Intent(MainActivity.this, Home.class);
            UsernameToHome.putExtra("passedUsername", passedUsername);
            startActivity(UsernameToHome);
            finish();

        }
        Button signin = findViewById(R.id.signin);
        Button signup = findViewById(R.id.signup);

        //GOTO SIGN IN PAGE
        signin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(signin_intent);
            finish();
        }
        });

        //GOTO SIGN UP PAGE
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(signup_intent);
                finish();
            }
        });

    }

    //just for testing , delete later
    void printTasks(ArrayList<Task> tasks)
    {
        for(Task task : tasks)
        {
            CharSequence text = task.id + "\t" + task.name + "\t" + task.details + "\t" + task.date + "\t" + task.username;

            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
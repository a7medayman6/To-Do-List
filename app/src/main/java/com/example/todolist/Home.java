package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity
{

    private static final String TAG = "HomePage";

    Button addTask,logout;
    RecyclerView.LayoutManager layoutManager;
    DbHandler dbh;
    ArrayList<Task> tasks;
    Context context;
    String currentUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        //getting the current username
        currentUsername = getIntent().getStringExtra("passedUsername");

        addTask = findViewById(R.id.addTask);
        logout =findViewById(R.id.logout);

        dbh = new DbHandler(Home.this);
        tasks = new ArrayList<>();
        Log.d(TAG, "onCreate: currenusername \t" + currentUsername);
        tasks = dbh.GetTasks(currentUsername);
        printTasks(tasks);

        Log.d(TAG, "onCreate: tasks size\t" + tasks.size());


        layoutManager = new LinearLayoutManager(this);

        //get the user tasks from the database


        initRecyvlerView(tasks);

        //ADD TASk BUTTON - Home
        addTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent UsernameToNewTask = new Intent(Home.this, NewTask.class);
                UsernameToNewTask.putExtra("passedUsername", currentUsername);
                UsernameToNewTask.putExtra("passedTask","");
                UsernameToNewTask.putExtra("update", false);
                startActivity(UsernameToNewTask);
                //finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbh.Logout();
                finish();;
                startActivity(new Intent(Home.this,MainActivity.class));
            }
        });
    }

    private void initRecyvlerView(ArrayList<Task> tasks)
    {
        Log.d(TAG, "initRecyvlerView: init RV");
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        Adaptor adaptor = new Adaptor(tasks, this, dbh, currentUsername);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    ArrayList<String> printTasks(ArrayList<Task> tasks)
    {

        ArrayList<String> StringTasks = new ArrayList<>();

        for (Task task : tasks)
        {
            String text = String.valueOf(task.id) + "\t" + task.name + "\t" + task.details + "\t" + task.date + "\t" + task.username;
            //printing all data
            Log.d(" USER DATA : ", text);
            StringTasks.add(text);
        }
        return StringTasks;
    }
}


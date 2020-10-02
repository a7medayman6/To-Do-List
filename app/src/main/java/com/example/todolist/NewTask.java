package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewTask extends AppCompatActivity
{
    private static final String TAG = "NewTask";
    TextView title;
    EditText taskName;
    EditText taskDetails;
    EditText dueDate;
    Button addTask;
    Button backButton;
    DbHandler dbh;
    String currentUsername;
    TextView authour;
    Task task;
    boolean update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_task);

        title = findViewById(R.id.title);
        taskName = findViewById(R.id.taskName);
        taskDetails = findViewById(R.id.taskDetails);
        dueDate = findViewById(R.id.dueDate);
        authour = findViewById(R.id.taskauthor);
        addTask = findViewById(R.id.addTask);
        backButton = findViewById(R.id.backButton);
        dbh = new DbHandler(this);


        currentUsername = getIntent().getStringExtra("passedUsername");
        update = getIntent().getBooleanExtra("update", false);
        if(update)
        {
            title.setText("Update Task");
            addTask.setText("Update");
            task = (Task) getIntent().getSerializableExtra("passedTask");
            taskName.setText(task.name);
            taskDetails.setText(task.details);
            dueDate.setText(task.date);
            if(currentUsername.equals("admin"))
            {
                authour.setText(task.username);
                authour.setVisibility(View.VISIBLE);
                authour.setGravity(Gravity.CENTER);
                if(!task.username.equals("admin"))
                {
                    addTask.setEnabled(false);
                    addTask.setBackgroundDrawable(getResources().getDrawable(R.drawable.customdisablebutton));
                }
            }
        }
        else
        {
            authour.setVisibility(View.INVISIBLE);
            authour.setText(null);
            title.setText("Add Task");
            addTask.setText("Add");
            taskName.setText("");
            taskDetails.setText("");
            dueDate.setText("");
        }
        //ADD TASK BUTTON - NEW TASK
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d("CURRENT USERNAME NEW_TASK", currentUsername);
                taskHandler Thandler;
                if(update)
                    Thandler = new taskHandler(task.id, currentUsername, taskName, taskDetails, dueDate,NewTask.this, dbh);
                else
                    Thandler = new taskHandler(currentUsername, taskName, taskDetails, dueDate,NewTask.this, dbh);

                DbHandler dbh = new DbHandler(NewTask.this);
                boolean valid = Thandler.Validate();

                if (valid) {
                    CharSequence text;
                    if (update)
                    {
                        text = "Task Updated Successfully.";
                        Thandler.Update();

                    }
                    else
                    {
                        Thandler.Insert();
                        text = "Task Added Successfully.";
                    }

                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();


                    Intent UsernameToHome = new Intent(NewTask.this, Home.class);
                    UsernameToHome.putExtra("passedUsername", currentUsername);

                    startActivity(UsernameToHome);
                    finish();

                }
            }
        });

        //
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent UsernameToHome = new Intent(NewTask.this, Home.class);
                UsernameToHome.putExtra("passedUsername", currentUsername);
                startActivity(UsernameToHome);
            }
        });
    }
}


class taskHandler
{
    int id;
    String name;
    String details;
    String date;
    String username;
    Context context;
    DbHandler dbh;


    public taskHandler(String username, EditText name, EditText details, EditText date, Context activity, DbHandler dbh)
    {
        this.username = username;
        this.name = name.getText().toString();
        this.details = details.getText().toString();
        this.date = date.getText().toString();
        context = activity;
        this. dbh = dbh;
    }
    //OVERLOADING
    public taskHandler(int id, String username, EditText name, EditText details, EditText date, Context activity, DbHandler dbh)
    {
        this.username = username;
        this.name = name.getText().toString();
        this.details = details.getText().toString();
        this.date = date.getText().toString();
        context = activity;
        this. dbh = dbh;
        this.id = id;
    }

    boolean Validate() {

        //check if task name isn't empty
        if (name.equals("")) {
            CharSequence text = "Please Enter The Task Name.";
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }


    void Insert()
    {
        //LoginActivity login = new LoginActivity();
        Task task = new Task(name, details, date, username);
        dbh.InsertTask(task);
    }

    void Update()
    {
        Task task = new Task(id, name, details, date, username);
        dbh.UpdateTask(task);
    }

}
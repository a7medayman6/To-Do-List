package com.example.todolist;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHandler extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "todolistdb";
    private static final String TABLE_Users = "users";

    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private static final String TABLE_Tasks = "tasks";

    private static final String KEY_NAME_T = "name";
    private static final String KEY_DATA_T = "data";
    private static final String KEY_DATE_T = "date";
    private static final String KEY_USERNAME_T = "username";

    private static final String TAG = "DbHandler";
    public DbHandler(Context context)
    {
        super(context,DB_NAME, null, DB_VERSION);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /// CREATE TABLE users ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, username TEXT, password TEXT);
        String CREATE_TABLE = "CREATE TABLE users (name TEXT, username TEXT, password TEXT, loggedin INTEGER DEFAULT 0);";
        db.execSQL(CREATE_TABLE);
        CREATE_TABLE = "CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, data TEXT, date DATE, username TEXT);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if exist
        String query = "DROP TABLE IF EXISTS users;";
        db.execSQL(query);
        query = "DROP TABLE IF EXISTS tasks;";
        db.execSQL(query);
        // Create tables again
        onCreate(db);
        db.close();
    }
    public void InsertTask(Task task)
    {

        SQLiteDatabase db = this.getWritableDatabase();
       //db.execSQL("DELETE FROM tasks;");
        String insertQuery = "INSERT INTO tasks (name, data, date, username) VALUES ('" + task.name + "', '" + task.details + "', '" + task.date + "', '" + task.username + "');";
        db.execSQL(insertQuery);
        db.close();

    }

    public ArrayList<Task> GetTasks()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Task> tasksList = new ArrayList<>();

        String query = "SELECT * FROM tasks";
        Cursor c = db.rawQuery(query,null);

        while (c.moveToNext())
        {
            Task task = new  Task(Integer.parseInt(c.getString(0)), c.getString(1),
                    c.getString(2), c.getString(3), c.getString(4));
            tasksList.add(task);
        }
        c.close();
        db.close();

        return  tasksList;
    }




    public Task SelectTask(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM tasks WHERE id = '" + Integer.toString(id) + "'", null);

        //if no result came back from the db return null
        Task task = null;

        if (c.moveToFirst())
        {
            task = new  Task(Integer.parseInt(c.getString(0)), c.getString(1),
                    c.getString(2), c.getString(3), c.getString(4));
        }

        c.close();
        db.close();

        return task;
    }
    //OVERLOADING
    public ArrayList<Task> GetTasks(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Task> tasksList = new ArrayList<>();

        String query;
        if(username.equals("admin"))
            query = "SELECT *  FROM tasks;";
        else
            query = "SELECT * " + " FROM "+ TABLE_Tasks + " WHERE username = '" + username + "';";

        Cursor c = db.rawQuery(query,null);

        while (c.moveToNext())
        {

            Task task = new  Task(c.getInt(0), c.getString(1),
                    c.getString(2), c.getString(3), c.getString(4));
            tasksList.add(task);
        }
        c.close();
        db.close();

        return  tasksList;
    }

    public void DeleteTask(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM tasks WHERE id = '" + id + "'";
        db.execSQL(query);
        db.close();
    }

    // Update User Details
    public void UpdateTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE tasks SET name = '" + task.name +"', data = '" + task.details + "', date = '" + task.date  + "'WHERE id = " + task.id + ";";
        Log.d(TAG, "UpdateTask: " + query);
        db.execSQL(query);
        db.close();
    }
    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    // Adding new User Details
    void insertUserDetails(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cValues = new ContentValues();
        cValues.put(KEY_NAME, user.name);
        cValues.put(KEY_USERNAME, user.username);
        cValues.put(KEY_PASSWORD, user.password);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Users,null, cValues);
//        String query = "INSERT INTO users(name, username, password) VALUES('" + user.name + "', '" + user.username + "','" + user.password + "');";
//        db.execSQL(query, null);

        db.close();
    }

    // Get All Users Details
    public ArrayList<User> GetUsers()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<User> userList = new ArrayList<>();

        String query = "SELECT * " + " FROM "+ TABLE_Users;
        Cursor c = db.rawQuery(query,null);

        while (c.moveToNext())
        {

            User user = new  User(c.getString(c.getColumnIndex(KEY_NAME)), c.getString(c.getColumnIndex(KEY_USERNAME)), c.getString(c.getColumnIndex(KEY_PASSWORD)));
            userList.add(user);
        }
        c.close();
        db.close();

        return  userList;
    }

    // Get User Details based on username
    public User SelectUser(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM users WHERE username = '" + username + "'", null);

        //if no result came back from the db return null
        User user = null;

        if (c.moveToFirst())
        {
            user = new  User(c.getString(c.getColumnIndex(KEY_NAME)), c.getString(c.getColumnIndex(KEY_USERNAME)), c.getString(c.getColumnIndex(KEY_PASSWORD)));
        }

        c.close();
        db.close();

        return user;
    }

    // Delete User Details
    public void DeleteUser(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM users WHERE username = '" + username + "'";
        db.execSQL(query);
        db.close();
    }

    // Update User Details
    public void UpdateUserDetails(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE users SET name = '" + user.name +"', password = '" + user.password + "' WHERE username = '" + user.username + "';";
        db.execSQL(query);
        db.close();

    }

    public boolean IsThere(String username)
    {
        if(SelectUser(username) == null)

            return false;

        else
            return true;
    }

    public User Logged_in_user()
    {
        User user = null;
        String query = "SELECT * FROM users WHERE loggedin = '1';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst())
        {
            user = new  User(c.getString(c.getColumnIndex(KEY_NAME)), c.getString(c.getColumnIndex(KEY_USERNAME)), c.getString(c.getColumnIndex(KEY_PASSWORD)));
        }
        c.close();
        db.close();
        return user;
    }
    public void Login(String username)
    {
        String query = "UPDATE users SET loggedin = '1' WHERE username = '" + username + "';";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
    public void Logout()
    {
        String query = "UPDATE users SET loggedin = '0';";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
}

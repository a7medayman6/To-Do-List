package com.example.todolist;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Task implements Serializable
{
    int id;
    String name;
    String details;
    String date;
    String username;

    public Task(String name, String details, String date, String username)
    {
        this.name = name;
        this.details = details;
        this.username = username;
        this.date = date;
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(new Date());*/
    }
    public Task( int id, String name, String details, String date, String username)
    {
        this.name = name;
        this.details = details;
        this.username = username;
        this.date = date;
        this.id = id;
    /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    date = sdf.format(new Date());*/
    }

}

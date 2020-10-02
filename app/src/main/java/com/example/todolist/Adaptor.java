package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adaptor extends RecyclerView.Adapter<Adaptor.ViewHolder>
{
    private static final String TAG = "Adaptor";
    String currentusername;
    private ArrayList<Task> tasks = new ArrayList<>();
    private Context mcontext;
    DbHandler dbh;

    public Adaptor(ArrayList<Task> tasks, Context mcontext, DbHandler dbh, String currentusername)
    {
        this.tasks = tasks;
        this.mcontext = mcontext;
        this.dbh = dbh;
        this.currentusername = currentusername;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        Log.d(TAG, "onBindViewHolder: onBindViewHolder called");

        final Task task = tasks.get(position);
        holder.name.setText(String.valueOf(task.name));
        holder.details.setText(String.valueOf(task.details));


        holder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent updateTask = new Intent(mcontext, NewTask.class);
                updateTask.putExtra("passedUsername", currentusername);

                updateTask.putExtra("passedTask", task);
                updateTask.putExtra("update", true);
                ((Activity)v.getContext()).finish();
                mcontext.startActivity(updateTask);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dbh.DeleteTask(task.id);
                CharSequence text = "Your Task Has Been Deleted Successfully.";
                Toast toast = Toast.makeText(mcontext, text, Toast.LENGTH_SHORT);
                toast.show();
                Intent UsernameToHome = new Intent(mcontext, Home.class);
                UsernameToHome.putExtra("passedUsername", currentusername);
                ((Activity)v.getContext()).finish();
                mcontext.startActivity(UsernameToHome);


            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView name;
        TextView details;
        Button delete;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.taskname);
            details = itemView.findViewById(R.id.taskdetails);
            delete = itemView.findViewById(R.id.deletetask);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            //Log.d(TAG, "onBindViewHolder: details "+ details);

        }
    }

}

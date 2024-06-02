package com.example.projectetoh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    public List<Task> taskList;
    private final DBHandler dbHandler;
    private final Context context;

    TaskAdapter(Context context, List<Task> nw, DBHandler db) {
        this.taskList = nw;
        this.dbHandler = db;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.descriptionTX.setText(task.getDescription());
        holder.deadlineTX.setText(task.getDeadline());
        if (task.isExpired())
            holder.deadlineTX.setTextColor(Color.RED);
        holder.doneBT.setChecked(task.isDone());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra("idCode", task.getIdCode());
            intent.putExtra("isDone", task.isDone());
            intent.putExtra("category", task.getCategory());
            intent.putExtra("position", position);
            intent.putExtra("description", task.getDescription());
            intent.putExtra("deadline", task.getDeadline());
            intent.putExtra("notes", task.getNotes());
            ((Activity) context).startActivityForResult(intent, 1);
        });
        holder.doneBT.setOnClickListener(v -> {
            task.setDone(!task.isDone());
            dbHandler.updateCourse(task);
            taskList.remove(position);
            notifyDataSetChanged();
        });
        holder.deleteBT.setOnClickListener(v -> {
            taskList.remove(position);
            dbHandler.deleteTask(task.getIdCode());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView descriptionTX;
        final TextView deadlineTX;
        final RadioButton doneBT;
        final Button deleteBT;

        public ViewHolder(View itemView) {
            super(itemView);
            descriptionTX = itemView.findViewById(R.id.description);
            deadlineTX = itemView.findViewById(R.id.deadline);
            doneBT = itemView.findViewById(R.id.done);
            deleteBT = itemView.findViewById(R.id.deleteButton);
        }
    }
}

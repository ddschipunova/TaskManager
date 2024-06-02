package com.example.projectetoh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    private ListsFragment tasksFragment = new ListsFragment();
    private  String goToUncompleted;
    private  String goToCompleted;
    private Boolean current = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasksFragment = (ListsFragment) getSupportFragmentManager().findFragmentById(R.id.listFragment);
        goToUncompleted= this.getResources().getString(R.string.goToUncompletedBT);
        goToCompleted = this.getResources().getString(R.string.goToCompletedBT);
        Button changeButton = this.findViewById(R.id.changeButton);
        changeButton.setText(goToCompleted);
        changeButton.setOnClickListener(v -> {
            current = !current;
            tasksFragment.setIsCompleted(current);
            tasksFragment.notifyChange();
            if (current) changeButton.setText(goToUncompleted);
            else changeButton.setText(goToCompleted);
        });

        Button addButton = this.findViewById(R.id.addTask);
        addButton.setOnClickListener(v -> {
            if (current) {
                current = false;
                tasksFragment.setIsCompleted(false);
                tasksFragment.notifyChange();
            }
            if (current) changeButton.setText(goToUncompleted);
            else changeButton.setText(goToCompleted);
            tasksFragment.addTask();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        boolean type = data.getBooleanExtra("isChanged", false);
        if (!type) {
            return;
        }
        int position = data.getIntExtra("position", -1);
        boolean isDone = data.getBooleanExtra("isDone", false);
        Integer idCode = data.getIntExtra("idCode", 0);
        String description = data.getStringExtra("description");
        String deadline = data.getStringExtra("deadline");
        String notes = data.getStringExtra("notes");
        Task newTask = new Task(idCode, description, deadline, notes, isDone);
        tasksFragment.change(position, newTask);
    }
}
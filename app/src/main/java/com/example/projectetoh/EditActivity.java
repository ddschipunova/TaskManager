package com.example.projectetoh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EditActivity extends AppCompatActivity {
    EditFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        fragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.editFragment);
        int position = getIntent().getIntExtra("position", -1);
        boolean isDone = getIntent().getBooleanExtra("isDone", false);
        Integer idCode = getIntent().getIntExtra("idCode", 0);
        String description = getIntent().getStringExtra("description");
        String deadline = getIntent().getStringExtra("deadline");
        String notes = getIntent().getStringExtra("notes");
        fragment.set(new Task(idCode, description, deadline, notes, isDone), position);
    }
}
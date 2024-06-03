package com.example.projectetoh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ListsFragment tasksFragment = new ListsFragment();
    private  String goToUncompleted;
    private  String goToCompleted;
    private String category;
    private List<String> categories = null;
    private Boolean current = false;
    private ArrayAdapter<String> adapter;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasksFragment = (ListsFragment) getSupportFragmentManager().findFragmentById(R.id.listFragment);
        goToUncompleted= this.getResources().getString(R.string.goToUncompletedBT);
        goToCompleted = this.getResources().getString(R.string.goToCompletedBT);
      //  category = this.getResources().getString(R.string.allCategory);
        dbHandler = new DBHandler(this);
        categories = dbHandler.readCategories();
       // tasksFragment.setCategory(category);
      //  tasksFragment.setBasicCategory(category);
        Button changeButton = this.findViewById(R.id.changeButton);
        changeButton.setText(goToCompleted);
        changeButton.setOnClickListener(v -> {
            current = !current;
            tasksFragment.setIsCompleted(current);
            tasksFragment.notifyChange();
            if (current) changeButton.setText(goToUncompleted);
            else changeButton.setText(goToCompleted);
        });
        Spinner spinner = findViewById(R.id.spinner);
        adapter = new ArrayAdapter(this,R.layout.my_spinner, categories);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = (String)parent.getItemAtPosition(position);
                category = item;
                tasksFragment.setCategory(item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
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
        String category = data.getStringExtra("category");
        String notes = data.getStringExtra("notes");
        Task newTask = new Task(idCode, description, deadline, notes, isDone);
        newTask.setCategory(category);
        tasksFragment.change(position, newTask);
        categories.clear();
        List<String> cur =  dbHandler.readCategories();
        while (cur.size() > 0) {
            categories.add(cur.get(0));
            cur.remove(0);
        }
        adapter.notifyDataSetChanged();

    }
}
package com.example.projectetoh;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Objects;

public class EditFragment extends Fragment {
    private Task task;
    private int position;
    private EditText description, notes;
    private EditText deadline;

    private DatePickerDialog.OnDateSetListener mDateSetListener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment, container, false);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button returnButton = view.findViewById(R.id.returnButton);
        description = view.findViewById(R.id.description);
        notes = view.findViewById(R.id.notes);
        deadline = view.findViewById(R.id.deadline);

        deadline.setOnClickListener(view1 -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            Date dt = new Date(day, month, year);
            deadline.setText(dt.toString());

        };
        saveButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("idCode", task.getIdCode());
            intent.putExtra("isDone", task.isDone());
            intent.putExtra("isChanged", true);
            intent.putExtra("position", position);
            intent.putExtra("description", description.getText().toString());
            intent.putExtra("deadline", deadline.getText().toString());
            intent.putExtra("notes", notes.getText().toString());
            ((Activity) view.getContext()).setResult(Activity.RESULT_OK, intent);
            ((Activity) view.getContext()).finish();
        });
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("isChanged", false);
            ((Activity) view.getContext()).setResult(Activity.RESULT_OK, intent);
            ((Activity) view.getContext()).finish();
        });
        return view;
    }

    public void set(Task current, int position) {
        this.task = current;
        this.position = position;
        description.setText(this.task.getDescription());
        notes.setText(this.task.getNotes());
        deadline.setText(this.task.getDeadline());
    }
}

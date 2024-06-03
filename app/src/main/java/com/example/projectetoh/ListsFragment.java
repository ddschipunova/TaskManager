package com.example.projectetoh;

import android.os.Bundle;
import android.view.*;

import androidx.fragment.app.*;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class ListsFragment extends Fragment {
    public List<Task> ts = new ArrayList<>();
    private boolean isCompletedTasks = false;

    private String category = "All";
    private String basicCategory = "";
    private DBHandler dbHandler;
    public TaskAdapter adapter;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) isCompletedTasks = bundle.getBoolean("isCompleted", false);
        dbHandler = new DBHandler(view.getContext());
        ts = dbHandler.readTasks(isCompletedTasks, category);
        ts.sort(Task.DEADLINE_IDCODE);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        adapter = new TaskAdapter(view.getContext(), ts, dbHandler);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void addTask() {
        Task nw = new Task("New");
        nw.setCategory(basicCategory);
        nw.setIdCode(dbHandler.getMaxIdCode() + 1);
        dbHandler.addNewTask(nw);
        ts.add(nw);
        ts.sort(Task.DEADLINE_IDCODE);
        adapter.notifyDataSetChanged();
    }

    public void notifyChange() {
        ts.clear();
        List<Task> cur;
        cur = dbHandler.readTasks(isCompletedTasks, category);
        while (cur.size() > 0) {
            Task nw = cur.get(0);
            ts.add(nw);
            cur.remove(0);
        }
        ts.sort(Task.DEADLINE_IDCODE);
        adapter.notifyDataSetChanged();
    }

    public void change(int position, Task newTask) {
        dbHandler.updateCourse(newTask);
        ts.set(position, newTask);
        ts.sort(Task.DEADLINE_IDCODE);
        adapter.notifyDataSetChanged();
    }

    public void setIsCompleted(boolean f) {
        isCompletedTasks = f;
        notifyChange();
    }

    public void setCategory(String c){
        category = c;
        notifyChange();
    }

    public void setBasicCategory(String basicCategory) {
        this.basicCategory = basicCategory;
    }
}

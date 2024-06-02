package com.example.projectetoh;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;

public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idCode = -1;
    private final String description;
    private String notes = "";
    private String deadline = "none";
    private Date date = new Date(1000, 1000, 10000);
    private boolean done = false;

    public Task(String d) {
        this.description = d;
    }

    public Task(Integer id, String d, String de, String n, boolean f) {
        this.idCode = id;
        this.description = d;
        this.deadline = de;
        this.notes = n;
        this.done = f;
        setDeadline(de);
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
        if (deadline.length() == 10) {
            date = new Date(deadline);
        }
    }

    public String getNotes() {
        return notes;
    }

    @NonNull
    @Override
    public String toString() {
        return "Task{" + "description='" + description + '\'' + ", deadline=" + deadline + '\'' + ", notes='" + notes + '}';
    }

    public Integer getIdCode() {
        return idCode;
    }

    public void setIdCode(Integer idCode) {
        this.idCode = idCode;
    }

    public static final Comparator<Task> DEADLINE_IDCODE = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            if(o1.getDate().compareTo(o2.getDate())!= 0)
                return o1.getDate().compareTo(o2.getDate());
            return o1.getIdCode().compareTo(o2.getIdCode());
        }
    };
    public Date getDate(){
        return date;
    }

    public boolean expired() {
        Calendar cal = Calendar.getInstance();
        Date cur = new Date(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH),cal.get(Calendar.YEAR) );
        return(date.compareTo(cur) > 0);
    }

}

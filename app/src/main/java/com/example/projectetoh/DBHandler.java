package com.example.projectetoh;

import static java.lang.Math.max;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "taskDB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "completedTasks";
    private static final String ID_COL = "id";
    private static final String IDCODE_COL = "idCode";
    private static final String DESC_COL = "description";
    private static final String CATEG_COL = "category";
    private static final String DONE_COL = "done";
    private static final String DEADLINE_COL = "deadline";
    private static final String NOTES_COL = "notes";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + IDCODE_COL + " TEXT," + DESC_COL + " TEXT," + DEADLINE_COL + " TEXT," + CATEG_COL + " TEXT,"+ NOTES_COL + " TEXT," + DONE_COL + " INTEGER)";
        db.execSQL(query);
    }

    public void addNewTask(Task t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDCODE_COL, t.getIdCode().toString());
        values.put(DESC_COL, t.getDescription());
        values.put(CATEG_COL, t.getCategory());
        values.put(DEADLINE_COL, t.getDeadline());
        values.put(NOTES_COL, t.getNotes());
        values.put(DONE_COL, t.isDone());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Task> readTasks(boolean isDone, String category) {
        String ask = isDone ? "1" : "0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorTasks;
        if(category.equals("All"))
            cursorTasks= db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE done = ?", new String[]{ask});
        else
            cursorTasks= db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE done = ? AND category = ?", new String[]{ask, category});
        List<Task> ts = new ArrayList<>();
        if (cursorTasks.moveToFirst()) {
            do {
                Task t = new Task(Integer.valueOf(cursorTasks.getString(1)), cursorTasks.getString(2), cursorTasks.getString(3), cursorTasks.getString(5), (cursorTasks.getInt(6) == 1));
                t.setCategory(cursorTasks.getString(4));
                ts.add(t);
            } while (cursorTasks.moveToNext());
        }
        cursorTasks.close();
        return ts;
    }

    public List<String> readCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        TreeSet<String> ts = new TreeSet<String>() ;
        if (cursorTasks.moveToFirst()) {
            do {
                ts.add(cursorTasks.getString(4));
            } while (cursorTasks.moveToNext());
        }
        cursorTasks.close();
        ts.add("All");
        List<String> cur = new ArrayList<String>();
        Iterator<String> itr = ts.iterator();
        while (itr.hasNext()) {
            cur.add(itr.next());
        }
        return cur;
    }

    public void deleteTask(Integer idCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "idCode=?", new String[]{idCode.toString()});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void updateCourse(Task t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDCODE_COL, t.getIdCode().toString());
        values.put(DESC_COL, t.getDescription());
        values.put(CATEG_COL, t.getCategory());
        values.put(DEADLINE_COL, t.getDeadline());
        values.put(NOTES_COL, t.getNotes());
        values.put(DONE_COL, t.isDone());
        db.update(TABLE_NAME, values, "idCode=?", new String[]{t.getIdCode().toString()});
        db.close();
    }

    public Integer getMaxIdCode() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        int ans = 1;
        if (cursorTasks.moveToFirst()) {
            do {
                ans = max(ans, Integer.parseInt(cursorTasks.getString(1)));
            } while (cursorTasks.moveToNext());
        }
        return ans;
    }

}

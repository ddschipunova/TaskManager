package com.example.projectetoh;

import static java.lang.Math.max;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "tasksDB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "completedTasks";
    private static final String ID_COL = "id";
    private static final String IDCODE_COL = "idCode";
    private static final String DESC_COL = "description";
    private static final String DONE_COL = "done";
    private static final String DEADLINE_COL = "deadline";
    private static final String NOTES_COL = "notes";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + IDCODE_COL + " TEXT," + DESC_COL + " TEXT," + DEADLINE_COL + " TEXT," + NOTES_COL + " TEXT," + DONE_COL + " INTEGER)";
        db.execSQL(query);
    }

    public void addNewTask(Integer idCode, String description, String deadline, String notes, Integer done) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDCODE_COL, idCode.toString());
        values.put(DESC_COL, description);
        values.put(DEADLINE_COL, deadline);
        values.put(NOTES_COL, notes);
        values.put(DONE_COL, done);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Task> readTasks(boolean isDone) {
        String ask = isDone ? "1" : "0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE done = ?", new String[]{ask});
        List<Task> ts = new ArrayList<>();
        if (cursorTasks.moveToFirst()) {
            do {
                ts.add(new Task(Integer.valueOf(cursorTasks.getString(1)), cursorTasks.getString(2), cursorTasks.getString(3), cursorTasks.getString(4), (cursorTasks.getInt(5)) == 1));
            } while (cursorTasks.moveToNext());
        }
        cursorTasks.close();
        return ts;
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

    public void updateCourse(Integer idCode, String description, String deadline, String notes, Integer done) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDCODE_COL, idCode);
        values.put(DESC_COL, description);
        values.put(DEADLINE_COL, deadline);
        values.put(NOTES_COL, notes);
        values.put(DONE_COL, done);
        db.update(TABLE_NAME, values, "idCode=?", new String[]{idCode.toString()});
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

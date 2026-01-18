package com.example.live_streaming;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="Users_db";
    private static final String TABLE_NAME="users";
    private static final String COL_1="id";
    private static final String COL_2="email";
    private static final String COL_3="password";
    private static final String COL_4="role";

    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create TABLE "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT,email TEXT,password TEXT,role TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String email,String password,String role)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,email);
        contentValues.put(COL_3,password);
        contentValues.put(COL_4,role);

        long result = db.insert(TABLE_NAME,null,contentValues);
        return result!=-1;

    }
    public boolean updateData(String id,String email,String password,String role)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,email);
        contentValues.put(COL_3,password);
        contentValues.put(COL_4,role);

        int result=db.update(TABLE_NAME,contentValues,"id=?",new String[]{id});
        return result>0;

    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"id=?",new String[]{id});
    }
    public Cursor getAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("SELECT * from "+ TABLE_NAME,null);

    }
    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_4 + " = ?",
                new String[]{"Student"});
    }
    public List<Student> getStudentList() {
        List<Student> students = new ArrayList<>();
        Cursor cursor = getAllStudents();

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Get column indices
                    int idIndex = cursor.getColumnIndex(COL_1);
                    int emailIndex = cursor.getColumnIndex(COL_2);

                    // Extract data
                    String id = cursor.getString(idIndex);
                    String email = cursor.getString(emailIndex);

                    // Extract name from email (before @)
                    String name = email.substring(0, email.indexOf('@'));

                    // Create Student object with default attendance status
                    Student student = new Student(id, name, email, AttendanceStatus.PRESENT);
                    students.add(student);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching students: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return students;
    }

    /**
     * UPDATED METHOD: Checks if a user exists with the given email, password, AND role.
     * @param email The user's email
     * @param password The user's password
     * @param role The user's selected role (e.g., "Student")
     * @return true if a match is found, false otherwise.
     */
    public boolean checkUser(String email, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean userExists = false;

        if (email == null || password == null || role == null) {
            return false;
        }

        try {
            // Query for the user, matching email, password, AND role
            String[] selectionArgs = {email, password, role};
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                    + COL_2 + " = ? AND "
                    + COL_3 + " = ? AND "
                    + COL_4 + " = ?", selectionArgs);

            // If count > 0, a match was found
            if (cursor != null && cursor.getCount() > 0) {
                userExists = true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close(); // Always close the cursor
            }
        }
        return userExists; // true if match found, false otherwise
    }
}


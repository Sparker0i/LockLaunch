package me.sparker0i.question.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import me.sparker0i.question.model.Category;
import me.sparker0i.question.model.Question;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "questiondatabase";
    private static final String TABLE_QUESTIONS = "question";
    private static final String TABLE_CATEGORIES = "categories";

    private static final String KEY_QN = "qn";
    private static final String KEY_A = "a";
    private static final String KEY_B = "b";
    private static final String KEY_C = "c";
    private static final String KEY_D = "d";
    private static final String KEY_ON = "enabled";
    private static final String KEY_CAT = "cat";
    private static final String KEY_ANS = "ans";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance  
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + KEY_QN + " TEXT, "
                + KEY_A + " TEXT, "
                + KEY_B + " TEXT, "
                + KEY_C + " TEXT, "
                + KEY_D + " TEXT, "
                + KEY_ANS + " TEXT, "
                + KEY_CAT + " TEXT, "
                + "PRIMARY KEY (" + KEY_QN + "," + KEY_A + "," + KEY_B + "," + KEY_C + "," + KEY_D + "," + KEY_CAT + "," + KEY_ANS + ")" + " )";
        db.execSQL(CREATE_QUESTION_TABLE);

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_CAT + " TEXT PRIMARY KEY, "
                + KEY_ON + " INT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    // Upgrading database  
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed  
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

        // Create tables again  
        onCreate(db);
    }

    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAT , category.getName());
        values.put(KEY_ON , category.isSelected());

        Log.i("values : " , category.getName() + category.isSelected());

        db.insert(TABLE_CATEGORIES , null , values);
        db.close();
    }

    public void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QN, question.getQN());
        values.put(KEY_A, question.getA());
        values.put(KEY_B, question.getB());
        values.put(KEY_C, question.getC());
        values.put(KEY_D, question.getD());
        values.put(KEY_CAT, question.getCAT());
        values.put(KEY_ANS , question.getANS());

        Log.i("values : " , question.getQN() + " " + question.getA() + " " + question.getB() + " " + question.getC() + " " + question.getD() + " " + question.getCAT() + " " + question.getANS());

        // Inserting Row
        db.insert(TABLE_QUESTIONS, null, values);
        //2nd argument is String containing nullColumnHack

        db.close(); // Closing database connection  
    }

    public void updateCategory(String cat , boolean value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_CATEGORIES + " SET " + KEY_ON + " = " + (value ? 1 : 0) + " WHERE " + KEY_CAT + " = \'" + cat + "\'");
        db.close();
    }

    public List<Category> getCategories() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CATEGORIES;
        Cursor cursor = db.rawQuery(query , null);

        List<Category> categoryList = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                do {
                    categoryList.add(new Category(cursor.getString(0) , cursor.getInt(1) == 1));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return categoryList;
    }

    public Question getRandomQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_QUESTIONS + " NATURAL JOIN " + TABLE_CATEGORIES;
        Cursor cursor = db.rawQuery(query , null);
        ArrayList<Question> list = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.moveToFirst())
                do {
                    list.add(new Question(cursor.getString(0),
                            cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
                }
                while (cursor.moveToNext());
            cursor.close();
        }
        return list.get(new Random().nextInt(list.size()));
    }
}  
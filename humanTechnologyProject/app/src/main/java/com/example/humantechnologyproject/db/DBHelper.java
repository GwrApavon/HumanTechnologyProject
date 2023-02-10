package com.example.humantechnologyproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HTProject.db";
    public static final String TABLE_BUTTONS = "t_buttons";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String Query = "CREATE TABLE TABLE_BUTTONS" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo TEXT NOT NULL," +
                "imagen TEXT NOT NULL," +
                "audio TEXT NOT NULL," +
                "color TEXT NOT NULL UNIQUE," +
                "tiempo_Pantalla INTEGER," +
                "tiempo_Sonido INTEGER)";

        sqLiteDatabase.execSQL(Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            sqLiteDatabase.execSQL("DROP TABLE TABLE_BUTTONS");
            onCreate(sqLiteDatabase);
    }
}

package com.example.humantechnologyproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;

import com.example.humantechnologyproject.Button;

import java.util.ArrayList;


public class DBButtons extends DBHelper{

    Context context;
    public DBButtons(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    /*
        Receives an id to delete a button from the database
        also returns true if success
             returns false if failed
     */
    public boolean deleteButton(int id) {

        boolean validate = false;

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{

            db.execSQL("DELETE FROM " + TABLE_BUTTONS + " WHERE id = '" + id +  "'" ) ;

            validate = true;

        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.close();
        }
        return validate;
    }

    /*
        Edits the button from the database whose id is the one received as parameter
        Receives all the fields in case any of them was edited
        also returns true if success
             returns false if failed
     */
    public boolean editButton(int id, String titulo, String imagen, String audio, String color, int tiempo_Pantalla, int tiempo_Sonido) {

        boolean validate = false;

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{

            db.execSQL("UPDATE "+ TABLE_BUTTONS + " SET titulo = '" + titulo + "',imagen = '" + imagen
                        + "',audio = '" + audio + "',color = '" + color + "',tiempo_Pantalla = '"
                        + tiempo_Pantalla + "',tiempo_Sonido = '" + tiempo_Sonido
                        + "' WHERE id = '" + id + "'");

            validate = true;

        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.close();
        }
        return validate;
    }

    /*
        Adds a button to the database
        Receives all the fields in case any of them was edited
        also returns the id if success
             returns 0 if failed
     */
    public long insertButton(String titulo, String imagen, String audio, String color, int tiempo_Pantalla, int tiempo_Sonido) {

        long id = 0;
        try {
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();


            values.put("titulo", titulo);
            values.put("imagen", imagen);
            values.put("audio", audio);
            values.put("color", color);
            values.put("tiempo_Pantalla", tiempo_Pantalla);
            values.put("tiempo_Sonido", tiempo_Sonido);
            id = db.insert(TABLE_BUTTONS, null, values);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return id;
    }

    /*
        Returns the buttons' ArrayList from the database
     */
    public ArrayList<Button> buttonList() {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Button> buttonList = new ArrayList<>();
        Button button;
        Cursor buttonCursor;

        buttonCursor = db.rawQuery("SELECT * FROM t_buttons", null);

        if (buttonCursor.moveToFirst()) {
            do {
                button = new Button();
                button.setId(buttonCursor.getInt(0));
                button.setTitle(buttonCursor.getString(1));
                button.setImage(buttonCursor.getString(2));
                button.setAudio(buttonCursor.getString(3));
                button.setColor(buttonCursor.getString(4));
                button.setScreenTime(buttonCursor.getInt(5));
                button.setAudioTime(buttonCursor.getInt(6));
                buttonList.add(button);
            } while (buttonCursor.moveToNext());
        }

        buttonCursor.close();

        return buttonList;
    }

    /*
        Returns a button from the database whose id is the one received as parameter
     */
    public Button buttonView(int id) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Button button = null;
        Cursor buttonCursor;

        buttonCursor = db.rawQuery("SELECT * FROM " + TABLE_BUTTONS + " WHERE id = " + id + " LIMIT 1", null);

        if (buttonCursor.moveToFirst()) {
            button = new Button();
            button.setTitle(buttonCursor.getString(1));
            button.setImage(buttonCursor.getString(2));
            button.setAudio(buttonCursor.getString(3));
            button.setColor(buttonCursor.getString(4));
            button.setScreenTime(buttonCursor.getInt(5));
            button.setAudioTime(buttonCursor.getInt(6));
        }

        buttonCursor.close();

        return button;
    }



}

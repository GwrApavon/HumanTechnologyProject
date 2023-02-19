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

    public boolean editButton(int id, String titulo, String imagen, String audio, String color, int tiempo_Pantalla, int tiempo_Sonido) {

        boolean validate = false;

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{

            db.execSQL("UPDATE TABLE_BUTTONS" +"SET titulo = '" + titulo + "',imagen = '" + imagen
                        + "',audio = '" + audio +"',color = '" + color +"',tiempo_Pantalla = '" + tiempo_Pantalla +"',tiempo_Sonido = '" + tiempo_Sonido
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
    public ArrayList<Button> mostrarBotones() {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Button> listaBotones = new ArrayList<>();
        Button boton;
        Cursor cursorBotones;

        cursorBotones = db.rawQuery("SELECT * FROM t_buttons", null);

        if (cursorBotones.moveToFirst()) {
            do {
                boton = new Button();
                boton.setTitulo(cursorBotones.getString(1));
                boton.setImagen(cursorBotones.getString(2));
                boton.setAudio(cursorBotones.getString(3));
                boton.setColor(cursorBotones.getString(4));
                listaBotones.add(boton);
            } while (cursorBotones.moveToNext());
        }

        cursorBotones.close();

        return listaBotones;
    }

    public Button buttonView(int id) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Button boton = null;
        Cursor cursorBotones;

        cursorBotones = db.rawQuery("SELECT * FROM t_buttons WHERE id = " + id + " LIMIT 1", null);

        if (cursorBotones.moveToFirst()) {
            boton = new Button();
            boton.setTitulo(cursorBotones.getString(1));
            boton.setImagen(cursorBotones.getString(2));
            boton.setAudio(cursorBotones.getString(3));
            boton.setColor(cursorBotones.getString(4));
            boton.setScreenTime(cursorBotones.getInt(5));
            boton.setAudioTime(cursorBotones.getInt(6));
        }

        cursorBotones.close();

        return boton;
    }



}

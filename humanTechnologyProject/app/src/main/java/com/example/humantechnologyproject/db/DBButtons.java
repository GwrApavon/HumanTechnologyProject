package com.example.humantechnologyproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.humantechnologyproject.Datos;

import java.util.ArrayList;


public class DBButtons extends DBHelper{

    Context context;
    public DBButtons(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarBoton(String titulo, String imagen, String audio, String color, int tiempo_Pantalla, int tiempo_Sonido) {

        long idnt = 0;
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
            idnt = db.insert(TABLE_BUTTONS, null, values);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return idnt;
    }

    public ArrayList<Datos> mostrarBotones() {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Datos> listaBotones = new ArrayList<>();
        Datos boton;
        Cursor cursorBotones;

        cursorBotones = db.rawQuery("SELECT * FROM t_buttons", null);

        if (cursorBotones.moveToFirst()) {
            do {
                boton = new Datos();
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



}
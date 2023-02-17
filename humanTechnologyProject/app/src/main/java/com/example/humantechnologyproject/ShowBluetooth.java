package com.example.humantechnologyproject;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.humantechnologyproject.databinding.ActivityShowBluetoothBinding;

import java.util.ArrayList;
/*
    Pasos:
        - Metodo de recuperar un único boton
        - obtener y poner inf con setFields
        - mirar reproducir musica
    Tener en cuenta bluetooth:
        - Cuando se llame a setFields hay que pasarle como parametro el resultado de getBoton a partir del id que se recibirá por bluetoth
 */
public class ShowBluetooth extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityShowBluetoothBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowBluetoothBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
    }

    //volcar los datos de la BD a los campos
    public void setFields(Datos boton) {
        ImageView imagen = findViewById(R.id.imagenDB);
        Uri uriFoto = Uri.parse(boton.getImagen());
        imagen.setImageURI(uriFoto);
        /*
        try {
            //Crear un imageView a partir de una ruta:
            File file = new File(datos.get(i).getImagen());
            Uri uri = Uri.fromFile(file);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), uri);
            imagen.setImageBitmap(getImageView((Bitmap) getResizedBitmap(bitmap, 1024)));
        }catch(IOException e) {
            e.printStackTrace();
        }
        */
        TextView titulo = findViewById(R.id.tituloDB);
        titulo.setText("Título: "+boton.getTitulo());

        TextView color = findViewById(R.id.colorDB);
        color.setText("Se ha seleccionado el botón "+boton.getColor());
    }
}
package com.example.humantechnologyproject;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.humantechnologyproject.databinding.ActivityShowBluetoothBinding;

import java.io.IOException;
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

        TextView titulo = findViewById(R.id.tituloDB);
        titulo.setText("Título: "+boton.getTitulo());

        TextView color = findViewById(R.id.colorDB);
        color.setText("Se ha seleccionado el botón "+boton.getColor());

        //Reproducir musica:
        Uri uriAudio = Uri.parse(boton.getAudio());
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uriAudio);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        //Parar musica al pulsar boton:
        Button bAceptar = findViewById(R.id.bAceptar);
        bAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
            }
        });
    }
}
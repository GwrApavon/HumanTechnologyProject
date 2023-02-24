package com.example.humantechnologyproject.Bluetooth;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.humantechnologyproject.Button;
import com.example.humantechnologyproject.R;
import com.example.humantechnologyproject.databinding.ActivityShowBluetoothBinding;
import java.io.IOException;

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
    public void setFields(Button button) {
        ImageView imagen = findViewById(R.id.imagenDB);
        Uri uriFoto = Uri.parse(button.getImage());
        imagen.setImageURI(uriFoto);

        TextView titulo = findViewById(R.id.tituloDB);
        titulo.setText("Título: "+button.getTitle());

        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.back);
        switch(button.getColor()) {
            case "Azul":
                cl.setBackgroundColor(getResources().getColor(R.color.azul));
                break;
            case "Amarillo":
                cl.setBackgroundColor(getResources().getColor(R.color.amarillo));
                break;
            case "Rojo":
                cl.setBackgroundColor(getResources().getColor(R.color.rojo));
                break;
            case "Verde":
                cl.setBackgroundColor(getResources().getColor(R.color.verde));
                break;
        }

        //Reproducir musica:
        Uri uriAudio = Uri.parse(button.getAudio());
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
        android.widget.Button bAceptar = findViewById(R.id.bAceptar);
        bAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
            }
        });
    }
}
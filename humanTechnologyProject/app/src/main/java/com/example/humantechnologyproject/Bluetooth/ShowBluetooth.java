package com.example.humantechnologyproject.Bluetooth;

import android.app.ActivityManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.humantechnologyproject.Button;
import com.example.humantechnologyproject.R;
import com.example.humantechnologyproject.databinding.ActivityShowBluetoothBinding;
import com.example.humantechnologyproject.db.DBButtons;

import java.io.IOException;

/*
    Pasos:
        - Metodo de recuperar un único boton
        - obtener y poner inf con setFields
        - mirar reproducir musica
    Tener en cuenta bluetooth:
        - Cuando se llame a setFields hay que pasarle como parametro el resultado de getBoton a partir del id que se recibirá por bluetoth
 */

/**
 * Se recibe desde BTService el id del boton a mostrar y se muestra
 */
public class ShowBluetooth extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityShowBluetoothBinding binding;
    String strLetra;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowBluetoothBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        //Recibe el id del boton de btservice:
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null)
            {
                id = Integer.parseInt(null);
            }
            else
            {
                id = extras.getInt("ID");
            }
        }
        else
        {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        //Obtiene el boton al que corresponde el id enviado:
        DBButtons dbButtons = new DBButtons(ShowBluetooth.this);
        Button b = dbButtons.buttonView(id);
        if (id > 0) {
            setFields(b);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        Intent intent = getIntent();
        strLetra = intent.getStringExtra("id");
        if (strLetra != null) {
        }
        //Poner el id q corresponde con las 4 letras que se reciben
        DBButtons buttonId = new DBButtons(getBaseContext());
        //aqui va el switch con las 4 opciones de letras y volvado a la variable id
        int id = 0;
        Button b = buttonId.buttonView(id);
        if (id > 0) {
            setFields(b);
        }

         */
        moveTaskToBack(false);
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        am.moveTaskToFront(this.getTaskId(),0);
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
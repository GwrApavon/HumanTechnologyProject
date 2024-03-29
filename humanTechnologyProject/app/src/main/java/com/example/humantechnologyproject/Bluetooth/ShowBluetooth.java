package com.example.humantechnologyproject.Bluetooth;

import android.app.ActivityManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
    Steps:
        - Method to recover one button
        - obtain and put inf with setFields
        - check music play
    Have Bluetooth in mind:
        - When setFields id called you have to send as parameter the result of getButton
          by the id received from bluetooth
 */

/**
 * Button's Id is received from BTService and its shown
 */
public class ShowBluetooth extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityShowBluetoothBinding binding;
    String color = "";
    int counter;

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

        //Receives button's id from btservice
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null)
            {
                color = "";
            }
            else
            {
                color = extras.getString("Color");
            }
        }
        else
        {
            color = ""+savedInstanceState.getSerializable("Color");
        }
        Log.d("btservice", color);

        //Obtiene el boton al que corresponde el id enviado:
        DBButtons dbButtons = new DBButtons(ShowBluetooth.this);
        Button b = dbButtons.buttonView(color);
        //Log.d("btservice", color);
        //Log.d("btservice", b.toString());

        setFields(b);
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
        ImageView image = findViewById(R.id.imagenDB);
        Log.d("btservice", "onCreate");
        Uri uriPhoto = Uri.parse(button.getImage());
        image.setImageURI(uriPhoto);

        this.setTitle(button.getTitle());

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

        //Music:
        String filePath = button.getAudio();
        MediaPlayer mp = new MediaPlayer();

        try {
            mp.setDataSource(this, Uri.parse(filePath));
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mp.start();
        mp.setVolume(1f, 1f);

        android.widget.Button bAccept = findViewById(R.id.bAceptar);
        bAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                finish();
            }
        });

        new CountDownTimer(button.getAudioTime()* 1000L, 1000){
            public void onTick(long millisUntilFinished){
                counter++;
            }
            public  void onFinish(){
                mp.stop();
            }
        }.start();
        new CountDownTimer(button.getScreenTime()* 1000L, 1000){
            public void onTick(long millisUntilFinished){
                counter++;
            }
            public  void onFinish(){
                finish();
            }
        }.start();
    }
}
package com.example.humantechnologyproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.humantechnologyproject.databinding.ActivityMain2Binding;

import java.io.File;

public class MainActivity2 extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMain2Binding binding;
    private static final int PICK_AUDIO = 1;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    Uri audioUri;
    ImageView foto_gallery;
    private Object result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        //Seleccionar foto:
        foto_gallery = (ImageView)findViewById(R.id.addImagen);
        foto_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Button bAudio = (Button) findViewById(R.id.bAudio);
        bAudio = (Button) findViewById(R.id.bAudio);
        bAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAudio();
            }
        });
    }

//Seleccionar foto:
//Visto: https://es.stackoverflow.com/questions/41707/cargar-una-imagen-desde-la-galeria-android
//       https://stackoverflow.com/questions/71082372/startactivityforresult-is-deprecated-im-trying-to-update-my-code
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
        /**
        ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        imageUri = gallery.getData();
                        foto_gallery.setImageURI(imageUri);
                    }
                });
         */
    }
//Seleccionar foto:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            foto_gallery.setImageURI(imageUri);
        }
        else {
            if(resultCode == RESULT_OK && requestCode == PICK_AUDIO) {
                audioUri = data.getData();
                String enlace = data.getDataString();

                TextView rAudio = (TextView) findViewById(R.id.resultadoAudio);
                rAudio.setText("Enlace: " + enlace);
            }
        }
    }

    //https://stackoverflow.com/questions/16806905/android-which-implicit-intent-for-picking-audio-files
    private void takeAudio(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i, PICK_AUDIO);
    }

}



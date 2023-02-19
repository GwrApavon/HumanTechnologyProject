package com.example.humantechnologyproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.humantechnologyproject.databinding.ActivityMain2Binding;
import com.example.humantechnologyproject.db.DBButtons;

import java.util.ArrayList;

public class ButtonSettings extends AppCompatActivity {
    private static final int STORAGE_PERMISSIONS_CODE = 1;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMain2Binding binding;
    private static final int PICK_AUDIO = 1;
    private static final int PICK_IMAGE = 100;
    boolean givenPermissions = false;

    //Campos boton:
    EditText enterTitle, ScreenTime, AudioTime;
    TextView rAudio;
    ImageView addImage, idAudio;
    Spinner buttonColor;
    Button button;
    int id = 0;

    Uri imageUri;
    Uri audioUri;
    ArrayList<String> coloresBoton;

    //valores para crear el boton:
    String imagePath = "";
    String audioPath = "";
    String color = "";
    String title = "";
    int screenTime = 0;
    int audioTime = 0;

    private Object result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enterTitle = findViewById(R.id.enterTitle);
        ScreenTime = findViewById(R.id.ScreenTime);
        AudioTime = findViewById(R.id.AudioTime);
        addImage = findViewById(R.id.addImage);
        idAudio = findViewById(R.id.idAudio);
        buttonColor = findViewById(R.id.buttonColor);
        rAudio = findViewById(R.id.resultadoAudio);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        coloresBoton = new ArrayList<String>();
        coloresBoton.add("Azul");
        coloresBoton.add("Rojo");
        coloresBoton.add("Amarillo");
        coloresBoton.add("Verde");
        // receives the id from the main activity
        id = receivedInt(savedInstanceState);

        if(id > 0){

            DBButtons dbButtons = new DBButtons(this);
            button = dbButtons.buttonView(id);

            title = button.getTitulo();
            enterTitle.setText(title);

            imagePath = button.getImagen();
            imageUri = Uri.parse(imagePath);
            addImage.setImageURI(imageUri);

            audioPath = button.getAudio();
            if(audioPath != null) {
                rAudio.setText("Audio seleccionado");
            }

            color = button.getColor();
            int colorPosition = coloresBoton.indexOf(color);
            buttonColor.setSelection(colorPosition);

            if(button.getScreenTime() > 0){
                screenTime = button.getScreenTime();
                ScreenTime.setText(screenTime);
            }
            if (button.getAudioTime() > 0 ){
                audioTime = button.getAudioTime();
                AudioTime.setText(audioTime);
            }

        }
        else {
            //Seleccionar foto:
            addImage = findViewById(R.id.addImage);
            ImageSelect();

            //Seleccionar audio2:
            audioSelect();

            //Seleccionar boton:
            Spinner buttonColor = (Spinner) findViewById(R.id.buttonColor);
            colorSelectionSpinner(buttonColor, coloresBoton);

            //Opciones avanzadas:
            showAdvacedOptions();

            //toolbar back button
            backButtonToolbar();
        }
    }

    /*
        Checks the integer received from the main activity
     */
    private int receivedInt(Bundle savedInstanceState)
    {
        int id;
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
        return id;
    }

    /*
        Used to select the audio you'll use as a notification alarm for a button
     */
    private void audioSelect() {
        idAudio = findViewById(R.id.idAudio);
        idAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarPermisos()) {
                    SelectAudio();
                }
                else {
                    pedirPermisos();
                }
            }
        });
    }

    /*
        Color selection for a button (depending on the color of the button from the keypad)
     */
    private void colorSelectionSpinner(Spinner buttonColor, ArrayList<String> coloresBoton) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, coloresBoton);
        buttonColor.setAdapter(adapter);
        buttonColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String colorSeleccionado = (String) buttonColor.getSelectedItem();

                TextView rBoton = (TextView) findViewById(R.id.resultadoBoton);
                rBoton.setText("El color seleccionado es: " + colorSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*
        Back button toolbar
        Takes you to the main activity
     */
    private void backButtonToolbar() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /*
        Shows the advanced options for every button
        ScreenTime = time the notification will be on the screen
        AudioTime = time the audio will sound
     */
    private void showAdvacedOptions() {
        android.widget.Button bAvanzada = (android.widget.Button) findViewById(R.id.bAvanzado);
        bAvanzada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tAudio = (TextView) findViewById(R.id.AudioTime);
                tAudio.setVisibility(View.VISIBLE);
                TextView tPantalla = (TextView) findViewById(R.id.ScreenTime);
                tPantalla.setVisibility(View.VISIBLE);
            }
        });
    }

    /*
        Used to select the Image you'll use for a button
     */
    private void ImageSelect() {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(verificarPermisos()) {
                    openGallery();
                }
                else {
                    pedirPermisos();
                }
            }
        });
    }

    /*
        Toolbar options
        Home(back) = close the activity and returns you to the previous
        Save = saves the button in the database
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //back arrow function
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (id == R.id.save) {
            enterTitle = findViewById(R.id.enterTitle);
            title = enterTitle.getText().toString();

            buttonColor = findViewById(R.id.buttonColor);
            color = buttonColor.getSelectedItem().toString();

            ScreenTime = findViewById(R.id.ScreenTime);
            screenTime = Integer.parseInt("" + ScreenTime.getText());

            AudioTime = findViewById(R.id.AudioTime);
            audioTime = Integer.parseInt("" + AudioTime.getText());


            DBButtons dbButtons = new DBButtons(this);

            if(verifyFullFilled()) {
                if(id == 0) {
                    long comprobacion = dbButtons.insertButton(title, imagePath, audioPath, color, screenTime, audioTime);

                    if (comprobacion > 0) {
                        Toast.makeText(this, "REGISTRO GUARDADO", Toast.LENGTH_SHORT).show();
                        //FieldCleaner();
                    } else {
                        Toast.makeText(this, "ERROR AL GUARDAR REGISTRO", Toast.LENGTH_SHORT).show();
                        //FieldCleaner();
                    }
                    finish();
                }
                if(id > 0){
                    boolean edited = dbButtons.editButton(id, title, imagePath, audioPath, color, screenTime, audioTime);

                    if(edited){
                        Toast.makeText(this, "REGISTRO ACTUALIZADO", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "ERROR AL EDITAR REGISTRO", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
                else{
                    finish();
                }
            }
            else{
                Toast.makeText(this, "DEBE RELLENAR TODOS LOS CAMPOS OBLIGATORIOS", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean verifyFullFilled(){
        if(!title.equals("") && !imagePath.equals("")&& !audioPath.equals("") && !color.equals("")){
            return true;
        }
        return false;
    }

    /*
        Allows you to pick a photo from the gallery
     */
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    /*
        Sets ImageView with the photo picked from the gallery
        or
        Sets TextView with "Audio Seleccionado" once you picked an audio from your storage
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imagePath = imageUri.toString();
            addImage.setImageURI(imageUri);
        }
        else {
            if(resultCode == RESULT_OK && requestCode == PICK_AUDIO) {
                audioUri = data.getData();
                String enlace = "";
                enlace = data.getDataString();
                audioPath = data.getDataString();
                rAudio = (TextView) findViewById(R.id.resultadoAudio);
                if(!enlace.equals("")) {
                    rAudio.setText("Audio seleccionado");
                }
            }
        }
    }

   /*
        Allows you to select an Audio from the storage
    */
    private void SelectAudio(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i, PICK_AUDIO);

    }

    /*
        Asks for permissions to access the storage
     */
    private void pedirPermisos() {
        AlertDialog AD;
        AlertDialog.Builder ADBuilder = new AlertDialog.Builder(ButtonSettings.this);
        ADBuilder.setMessage("Permite que 'SerrAlertas' pueda acceder al almacenamiento.");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(
                            ButtonSettings.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_CODE
                    );
                }
            });
            AD = ADBuilder.create();
            AD.show();
        }
    }

    /*
        After accepting permissions, it checks if it actually has permissions to access the storage
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSIONS_CODE) {
            /* Resultado de la solicitud para permiso de cámara
             Si la solicitud es cancelada por el usuario, el método .lenght sobre el array
             'grantResults' devolverá null.*/

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                givenPermissions = true;

            } else {
                AlertDialog AD;
                AlertDialog.Builder ADBuilder = new AlertDialog.Builder(ButtonSettings.this);
                ADBuilder.setMessage("Permite que 'SerrAlertas' pueda acceder al almacenamiento");

                ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                                ButtonSettings.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_CODE
                        );
                    }
                });
                AD = ADBuilder.create();
                AD.show();
            }
        }
    }


    /*
        Verifies if it has permissions to access the storage
     */
    private boolean verificarPermisos() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(ButtonSettings.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            return true;
        } else {
            return false;
        }
    }

    /*
        Inflate the menu; this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_a2, menu);
        return true;
    }
}


/* COSAS HECHAS A MITAD PARA
    Bundle extras = getIntent().getExtras();
        id = extras.getInt("ID");
        if(id > 0){
            DBButtons dbButtons = new DBButtons(this);
            button = dbButtons.buttonView(id);

            if(button != null){
                enterTitle = findViewById(R.id.enterTitle);
                ScreenTime = findViewById(R.id.ScreenTime);
                AudioTime = findViewById(R.id.AudioTime);
                addImage = findViewById(R.id.addImage);
                idAudio = findViewById(R.id.idAudio);
                buttonColor = findViewById(R.id.buttonColor);

                binding = ActivityMain2Binding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());

                setSupportActionBar(binding.toolbar);

                Uri uriFoto = Uri.parse(button.getImagen());
                addImage.setImageURI(uriFoto);
                String audioPath = button.getAudio();
                String color = button.getColor();
                enterTitle.setText(button.getTitulo());
                if(button.getScreenTime() > 0){
                    ScreenTime.setText(button.getScreenTime());
                }
                if(button.getAudioTime() > 0){
                    AudioTime.setText(button.getAudioTime());
                }
            }
        }
 */


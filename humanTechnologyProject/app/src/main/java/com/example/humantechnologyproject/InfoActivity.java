package com.example.humantechnologyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.humantechnologyproject.db.DBButtons;

public class InfoActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        TextView info = findViewById(R.id.infoTxV);

        info.setText("\nComo usar SerraAlertas:"
                +"\n\n\n\t\t1.En la aplicacion pulse el icono desplegable de arriba a la derecha."
                +"\n\n\n\t\t2.Presione Bluetooth para solicitar los permisos necesarios."
                +"\n\n\n\t\t3.Añada los botones que representen cada necesidad."
                +"\n\n\n\t\t4.Conecte la botonera."
                +"\n\n\n¡Comience a usar la app!");

        info.setTextSize(22);

        backButtonToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DBButtons dbButtons = new DBButtons(this);
        //back arrow function
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void backButtonToolbar() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
}
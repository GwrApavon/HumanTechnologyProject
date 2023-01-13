package com.example.humantechnologyproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    Context context;
    ArrayList<Datos> datos = new ArrayList<>();

    public Adaptador(Context context, ArrayList<Datos> datos) {
        this.context = context;
        this.datos = datos;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int i) {
        return datos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater mostrado = LayoutInflater.from(context);
        View elemento = mostrado.inflate(R.layout.elemento, viewGroup, false);

        ImageView imagen = elemento.findViewById(R.id.imagen);
        imagen.setImageResource(datos.get(i).getImagen());

        TextView titulo = elemento.findViewById(R.id.titulo);
        titulo.setText(datos.get(i).getTitulo());

        TextView audio = elemento.findViewById(R.id.audio);
        audio.setText(datos.get(i).getAudio());

        TextView num_button = elemento.findViewById(R.id.num_button);
        num_button.setText(datos.get(i).getNumButton());

        ImageView edit_button = elemento.findViewById(R.id.edit_button);
        edit_button.setImageResource(datos.get(i).getEdit_button());
        return elemento;
    }
}

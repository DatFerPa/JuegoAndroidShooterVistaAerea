package com.shooteraereo.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;

/**
 * Created by Fernando on 18/11/2017.
 */

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Fondo fondo;

    public boolean inicializado;

    public Nivel(Context context, int numeroNivel) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        inicializar();

        inicializado = true;
    }

    public void inicializar()throws Exception {
        fondo = new Fondo(context, CargadorGraficos.cargarDrawable(context, R.drawable.background));
    }


    public void actualizar (long tiempo){
        if (inicializado) {

        }
    }


    public void dibujar (Canvas canvas) {
        if(inicializado) {
            fondo.dibujar(canvas);
        }
    }
}
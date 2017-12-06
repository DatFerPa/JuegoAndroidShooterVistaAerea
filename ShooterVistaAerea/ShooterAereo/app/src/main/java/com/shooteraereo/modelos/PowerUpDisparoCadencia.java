package com.shooteraereo.modelos;

import android.content.Context;

import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;

/**
 * Created by Fernando on 05/12/2017.
 */

public class PowerUpDisparoCadencia extends PowerUp {
    public PowerUpDisparoCadencia(Context context, double x, double y) {
        super(context, x, y);
    }

    @Override
    protected void inicializar() {
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.bala);
    }

    @Override
    public void accion(Jugador jugador) {
        System.out.println("Cadencia antes : "+jugador.tiempoParaDisparo);
        jugador.reducirCoolDown(5);
        System.out.println("Cadencia antes : "+jugador.tiempoParaDisparo);
    }
}

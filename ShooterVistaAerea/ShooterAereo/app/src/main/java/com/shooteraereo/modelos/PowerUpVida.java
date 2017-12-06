package com.shooteraereo.modelos;

import android.content.Context;

import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;

/**
 * Created by Fernando on 26/11/2017.
 */

public class PowerUpVida extends PowerUp {

    public PowerUpVida(Context context, double x, double y) {
        super(context, x, y);
    }

    @Override
    protected void inicializar() {
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.corazon_power_up);
    }

    @Override
    public void accion(Jugador jugador) {
        System.out.println("Vida antes : "+jugador.vida);
        jugador.sumarVida(20);
        System.out.println("Vida despues : "+jugador.vida);
    }
}

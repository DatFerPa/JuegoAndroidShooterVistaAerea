package com.shooteraereo.modelos;

import android.content.Context;

import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;

/**
 * Created by Fernando on 26/11/2017.
 */

public class PowerUpDisparoPoder extends PowerUp{

    public PowerUpDisparoPoder(Context context, double x, double y) {
        super(context, x, y);
    }

    @Override
    protected void inicializar() {
            imagen = CargadorGraficos.cargarDrawable(context, R.drawable.balas);
    }

    @Override
    public void accion(Jugador jugador) {
        System.out.println("Daño antes : "+jugador.dañoAtaque);
        jugador.aumentarDaño(5);
        System.out.println("Daño despues : "+jugador.dañoAtaque);
    }
}

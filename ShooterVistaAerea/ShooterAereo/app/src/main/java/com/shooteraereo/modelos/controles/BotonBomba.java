package com.shooteraereo.modelos.controles;

import android.content.Context;

import com.shooteraereo.GameView;
import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;
import com.shooteraereo.modelos.Modelo;

/**
 * Created by Fernando on 18/11/2017.
 */

public class BotonBomba extends Modelo {

    public static int ANCHO_BOTON = 70;
    public static int ALTO_BOTON = 70;

    public BotonBomba(Context context) {
        super(context, GameView.pantallaAncho*0.9 , GameView.pantallaAlto*0.65,
                ANCHO_BOTON,ALTO_BOTON);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.botonbomba);
    }


}

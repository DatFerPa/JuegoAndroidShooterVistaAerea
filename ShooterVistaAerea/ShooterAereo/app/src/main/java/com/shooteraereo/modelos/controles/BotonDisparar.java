package com.shooteraereo.modelos.controles;

import android.content.Context;

import com.shooteraereo.GameView;
import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;
import com.shooteraereo.modelos.Modelo;

/**
 * Created by Fernando on 18/11/2017.
 */

public class BotonDisparar extends Modelo {

    public static int ANCHO_BOTON = 70;
    public static int ALTO_BOTON = 70;

    public BotonDisparar(Context context) {
        super(context, GameView.pantallaAncho*0.7 , GameView.pantallaAlto*0.8,
                ANCHO_BOTON,ALTO_BOTON);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.buttonfirepad);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        //hacemos click dentro de la circunferencia
        double distancia = Math.sqrt(Math.pow(clickX - x, 2) + Math.pow(clickY - y, 2));

        if(distancia < 35){
            estaPulsado = true;
            System.out.println("disaparo pulsado");
        }
        return estaPulsado;
    }

    public int getOrientacionX(
            float cliclX) {
        //System.out.println("click en x :"+ (x-cliclX)*-1);
        return (int) (x - cliclX) *-1;
    }

    public int getOrientacionY(
            float cliclY) {
        //System.out.println("click en y :"+ (y-cliclY)*-1);
        return (int) (y - cliclY) *-1;
    }

}

package com.shooteraereo.modelos.controles;

import android.content.Context;

import com.shooteraereo.GameView;
import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;
import com.shooteraereo.modelos.Modelo;

/**
 * Created by Fernando on 18/11/2017.
 */

public class Pad extends Modelo {


    public Pad(Context context) {
        super(context, GameView.pantallaAncho*0.15 , GameView.pantallaAlto*0.75 ,
                GameView.pantallaAlto, GameView.pantallaAncho);

        altura = 100;
        ancho = 100;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.pad);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        //hacemos click dentro de la circunferencia
        double distancia = Math.sqrt(Math.pow(clickX - x, 2) + Math.pow(clickY - y, 2));

        if(distancia < 50){
            estaPulsado = true;
        }
        System.out.println("pad movimineto Pulasdo: "+estaPulsado);
        return estaPulsado;
    }

    public int getOrientacionX(
            float cliclX) {

        //System.out.println("click en x :"+ (x-cliclX));
        return (int) (x - cliclX);
    }

    public int getOrientacionY(
            float cliclY) {
        //System.out.println("click en y :"+ (y-cliclY)*-1);
        return (int) (y - cliclY) *-1;
    }




}

package com.shooteraereo.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;
import com.shooteraereo.gestores.GestorAudio;
import com.shooteraereo.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by Fernando on 11/12/2017.
 */

public class Bomba extends Modelo {

    public static final String BOMBA_NORMAL = "bombaNormal";
    public static final String BOMBA_EXPLOTANDO = "bombaExplotando";

    public boolean exploto = false;
    public int tiempoExplotar = 30;
    public int tiempoEnExplosion = 20;

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    public Bomba(Context context, double x, double y) {
        super(context, x, y, 20, 20);
        inicializar();
    }

    public void inicializar (){

        Sprite bomba_normal = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_bomba),
                ancho, altura,
                6, 6, true);
        sprites.put(BOMBA_NORMAL, bomba_normal);

        Sprite bomba_explotando = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.explosion_3),
                150, 150,
                1, 1, true);
        sprites.put(BOMBA_EXPLOTANDO, bomba_explotando);

        sprite = bomba_normal;
    }

    public void actualizar (long tiempo) {
        sprite.actualizar(tiempo);
        if(tiempoExplotar <=0 ){
            sprite = sprites.get(BOMBA_EXPLOTANDO);
            this.altura = 150;
            this.ancho = 150;
            cDerecha = ancho/2;
            cIzquierda = ancho/2;
            cArriba = altura/2;
            cAbajo = altura/2;
        }
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX , (int) y - Nivel.scrollEjeY);
    }

    public void actualizarTiempos(){
        if(tiempoExplotar <= 0 && exploto==false){
            GestorAudio.getInstancia().reproducirSonido(GestorAudio.BOMBA);
            exploto = true;
        }else {
            if (!exploto) {
                tiempoExplotar--;
            } else {
                tiempoEnExplosion--;
            }
        }
    }





}

package com.shooteraereo.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Fernando on 18/11/2017.
 */

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Fondo fondo;
    private Tile[][] mapaTiles;
    private Jugador jugador;

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
        inicializarMapaTiles();
    }


    public void actualizar (long tiempo){
        if (inicializado) {
            jugador.actualizar(tiempo);
        }
    }


    public void dibujar (Canvas canvas) {
        if(inicializado) {
            fondo.dibujar(canvas);
            dibujarTiles(canvas);
            jugador.dibujar(canvas);
        }
    }

    private void dibujarTiles(Canvas canvas) {
        for (int y = 0; y < altoMapaTiles(); y++) {
            for (int x = 0; x < anchoMapaTiles(); x++) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo

                    mapaTiles[x][y].imagen.setBounds(
                            x * Tile.ancho,
                            y * Tile.altura,
                            x * Tile.ancho + Tile.ancho,
                            y * Tile.altura + Tile.altura);

                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }
    }

    public int anchoMapaTiles(){
        return mapaTiles.length;
    }

    public int altoMapaTiles(){

        return mapaTiles[0].length;
    }


    private void inicializarMapaTiles() throws Exception {
        InputStream is = context.getAssets().open(numeroNivel+".txt");
        int anchoLinea;

        List<String> lineas = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        {
            String linea = reader.readLine();
            anchoLinea = linea.length();
            while (linea != null)
            {
                lineas.add(linea);
                if (linea.length() != anchoLinea)
                {
                    Log.e("ERROR", "Dimensiones incorrectas en la línea");
                    throw new Exception("Dimensiones incorrectas en la línea.");
                }
                linea = reader.readLine();
            }
        }

        // Inicializar la matriz
        mapaTiles = new Tile[anchoLinea][lineas.size()];
        // Iterar y completar todas las posiciones
        for (int y = 0; y < altoMapaTiles(); ++y) {
            for (int x = 0; x < anchoMapaTiles(); ++x) {
                char tipoDeTile = lineas.get(y).charAt(x);//lines[y][x];
                mapaTiles[x][y] = inicializarTile(tipoDeTile,x,y);
            }
        }
    }

    private Tile inicializarTile(char codigoTile,int x, int y) {
        switch (codigoTile) {
            case '.':
                // en blanco, sin textura
                return new Tile(null, Tile.PASABLE);
            case '#':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.piedra), Tile.SOLIDO);
            case '1':
                // Jugador
                // Posicion centro abajo
                int xCentroAbajoTile = x * Tile.ancho + Tile.ancho/2;
                int yCentroAbajoTile = y * Tile.altura + Tile.altura;
                jugador = new Jugador(context,xCentroAbajoTile,yCentroAbajoTile);

                return new Tile(null, Tile.PASABLE);
            default:
                //cualquier otro caso
                return new Tile(null, Tile.PASABLE);
        }
    }

}
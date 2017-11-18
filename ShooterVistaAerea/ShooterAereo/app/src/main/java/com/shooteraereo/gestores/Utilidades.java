package com.shooteraereo.gestores;

/**
 * Created by Fernando on 18/11/2017.
 */

public class Utilidades {

    public static double proximoACero(double a, double b) {
        if (Math.pow(a,2) <  Math.pow(b,2))
            return a;
        else
            return b;

    }
}
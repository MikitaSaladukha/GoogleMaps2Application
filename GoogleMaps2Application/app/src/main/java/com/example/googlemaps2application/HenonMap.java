package com.example.googlemaps2application;

public class HenonMap {

    double x0 = Math.random();
    double y0 = Math.random();
    String  newKey=Double.toString(x0).replace(".","").substring(0,9);
    int intKey;

    public synchronized void update() {

        double a = 1.4;
        double b = 0.3;

        int newKeyI;

        //result[0]=(int) Math.round(amplitude*x[0]);

        double x = 1 - a * x0 * x0 + y0;
        double y = b * x0;

        System.out.println("x=" + x + " y=" + y);


        intKey = (int) (x * 100000);
        newKey = Double.toString(x);
        newKey = newKey.replace(".", "p");
        newKey = newKey.replace("-", "m");
        newKey = newKey.substring(0, 9);


        x0 = x;
        y0 = y;
        //newKey=Integer.toString(newKeyI);


        return;

    }

    public synchronized String getNewKey() {
        return newKey;
    }

}


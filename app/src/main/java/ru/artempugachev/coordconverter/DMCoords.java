package ru.artempugachev.coordconverter;

/**
 * Created by artem on 31.01.16.
 */
public class DMCoords {
    //  Координаты в виде градусов и минут с десятичной частью
    private int deg;
    private double min;

    public DMCoords(int deg, double min) {
        this.deg = deg;
        this.min = min;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }
}

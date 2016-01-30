package ru.artempugachev.coordconverter;

/**
 * Created by artem on 31.01.16.
 */
public class Dcoords {
    //  Координаты в виде градусов с десятичной частью
    private double deg;

    public Dcoords(double deg) {
        this.deg = deg;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }
}

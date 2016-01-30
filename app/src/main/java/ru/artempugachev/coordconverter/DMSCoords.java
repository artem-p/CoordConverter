package ru.artempugachev.coordconverter;

/**
 * Created by artem on 31.01.16.
 */
public class DMSCoords {
    //  Координаты в виде градусов, минут и секунд с десятичной дробью
    private int deg;
    private int min;
    private double sec;

    public DMSCoords(int deg, int min, double sec) {
        this.deg = deg;
        this.min = min;
        this.sec = sec;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public double getSec() {
        return sec;
    }

    public void setSec(double sec) {
        this.sec = sec;
    }
}

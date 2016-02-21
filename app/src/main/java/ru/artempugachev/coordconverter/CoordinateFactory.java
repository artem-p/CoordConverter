package ru.artempugachev.coordconverter;

/**
 * Created by user on 17.02.2016.
 */

public abstract class CoordinateFactory {
    public abstract Dcoords createDcoords();
    public abstract DMSCoords createDMSCoords();
}


abstract class Coordinate {
    protected int minVal;
    protected int maxVal;

    protected abstract boolean isRightCoords(); //  Проверяем, попадают ли координаты в диапазон -90;90 или -180;180
}


class Dcoords extends Coordinate{
    //  Координаты в виде градусов с десятичной частью
    private double deg;

    public Dcoords(double deg, int minVal, int maxVal) {
        this.deg = deg;
        this.minVal = minVal;
        this.maxVal = maxVal;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }


    @Override
    protected boolean isRightCoords() {
        boolean isRightCoords = false;
        if(this.minVal <= this.deg && this.deg <= this.maxVal) {
            isRightCoords = true;
        }

        return isRightCoords;
    }
}


class DMSCoords extends Coordinate{
    //  Координаты в виде градусов, минут и секунд с десятичной дробью
    private int deg;
    private int min;
    private double sec;

    public DMSCoords(int deg, int min, double sec, int minVal, int maxVal) {
        this.deg = deg;
        this.min = min;
        this.sec = sec;
        this.minVal = minVal;
        this.maxVal = maxVal;
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


    @Override
    protected boolean isRightCoords() {
        boolean isRightCoords = false;

        if(this.minVal <= this.deg && this.deg <= this.maxVal &&
                0 <= this.min && this.min <= 59 &&
                0 <= this.sec && this.sec <= 59) {
            isRightCoords = true;
        }
        return isRightCoords;
    }
}
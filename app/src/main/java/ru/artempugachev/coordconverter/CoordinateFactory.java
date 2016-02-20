package ru.artempugachev.coordconverter;

/**
 * Created by user on 17.02.2016.
 */

public abstract class CoordinateFactory {
    public abstract Dcoords createDcoords();
    public abstract DMSCoords createDMSCoords();
}


abstract class Coordinate {
    protected int min;
    protected int max;

    protected abstract boolean isInDiapazon();
}


class Dcoords extends Coordinate{
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

    @Override
    protected boolean isInDiapazon() {
        return false;
    }
}


public class DMSCoords {
    //  Координаты в виде градусов, минут и секунд с десятичной дробью
    protected int deg;
    protected int min;
    protected double sec;

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

class DMSLat extends DMSCoords {

    public DMSLat(int deg, int min, double sec) {
        super(deg, min, sec);
    }

    public boolean isRightCoords() {
        //  Проверяем, что координаты попадают в заданный диапазон
        boolean isRightCoords = false;

        if(-90 <= this.deg && this.deg <= 90 &&
                0 <= this.min && this.min <= 59 &&
                0 <= this.sec && this.sec <= 59) {
            isRightCoords = true;
        }

        return isRightCoords;
    }
}
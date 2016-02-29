package ru.artempugachev.coordconverter;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by user on 27.02.2016.
 */
public abstract class Coordinate {
    protected int minVal;
    protected int maxVal;

    private double deg;
    protected double min;
    protected double sec;

    public Coordinate (double deg) {
        this.deg = deg;
        this.min = 0;
        this.sec = 0;
    }

    public Coordinate (int deg, int min, double sec) {
        this.deg = dms2deg(deg, min, sec);
        this.min = min;
        this.sec = sec;
    }

    public double getD() {
        //  В виде градусов с десятыми
        return deg;
    }

    public int getIntD() {
        //  Целочисленное значение градусов


        return new BigDecimal((int)Math.abs(this.deg)).intValue();
    }

    public int getIntMin() {
        //  Целые минуты

        return getDecimalPartInMinutes().intValue();
    }

    public double getSec() {
        //  Секунды с плавающей точкой

        BigDecimal min = getDecimalPartInMinutes();
        BigDecimal sec = min.subtract(new BigDecimal(min.intValue())).multiply(new BigDecimal("60"));
        return sec.doubleValue();
    }

    private BigDecimal getDecimalPartInMinutes() {
        //  Дробную часть градуса выражаем в минутах
        String sDeg = String.valueOf(Math.abs(this.deg));
        BigDecimal absDeg = new BigDecimal(sDeg).abs();
        BigDecimal degDecimal = absDeg.subtract(new BigDecimal(this.getIntD()));
        BigDecimal min = (degDecimal.multiply(new BigDecimal("60")));
        return min;
    }

    private double dms2deg(int deg, int min, double sec) {
        //  dms to d
        BigDecimal absDeg = new BigDecimal(deg).abs();
        BigDecimal bdMin = new BigDecimal(min).divide(new BigDecimal("60"), MathContext.DECIMAL64);
        BigDecimal bdSec = new BigDecimal(sec).divide(new BigDecimal("3600"), MathContext.DECIMAL64);

        BigDecimal bdDeg = absDeg.add(bdMin).add(bdSec);

        double ddeg = bdDeg.doubleValue();
        if(deg < 0) ddeg = -ddeg;               //ю.ш. и з.д. с минусом
        return ddeg;
    }

    protected boolean isRightCoords() {
        boolean isRightCoords = false;
        if(this.minVal <= this.deg && this.deg <= this.maxVal) {
            if(0 <= this.min && this.min <= 59) {
                if(0 <= this.min && this.min <= 59) {
                    isRightCoords = true;
                }
            }
        }

        return isRightCoords;
    }
//    public DMSCoords convert(Dcoords dcoords) {
//
//        String sDeg = String.valueOf(Math.abs(dcoords.getDeg()));
//        BigDecimal absDeg = new BigDecimal(sDeg).abs();
//
//        int degIntPart = new BigDecimal((int)Math.abs(dcoords.getDeg())).intValue();
//
//        BigDecimal degDecimal = absDeg.subtract(new BigDecimal(degIntPart));
//        BigDecimal min = (degDecimal.multiply(new BigDecimal("60")));
//        int minIntPart = min.intValue();
//        BigDecimal sec = min.subtract(new BigDecimal(minIntPart)).multiply(new BigDecimal("60"));
//        if(dcoords.getDeg() < 0) degIntPart = - degIntPart;
//        return new DMSCoords(degIntPart, minIntPart, sec.doubleValue());
//    }
}

class Lat extends Coordinate {

    public Lat(double deg) {
        super(deg);
        this.minVal = -90;
        this.maxVal = 90;
    }

    public Lat(int deg, int min, double sec) {
        super(deg, min, sec);
        this.minVal = -90;
        this.maxVal = 90;
    }
}

class Lon extends Coordinate {

    public Lon(double deg) {
        super(deg);
        this.minVal = -180;
        this.maxVal = 180;
    }

    public Lon(int deg, int min, double sec) {
        super(deg, min, sec);
        this.minVal = -180;
        this.maxVal = 180;
    }
}
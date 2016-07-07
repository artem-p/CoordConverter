package ru.artempugachev.coordconverter;

import java.math.BigDecimal;
import java.math.MathContext;


abstract class Coordinate {
    private int minVal;
    private int maxVal;

    private double decimalPres;         // Целочисленное представление координаты.
                                        // К нему приводятся все другие представления

    Coordinate (double degDec) {
        this.decimalPres = degDec;
    }

    Coordinate (int deg, int min, double sec, String label) {
        //  Приводим к десятичному виду
    }

    Coordinate (int deg, double min, String label) {
        //  Приводим к десятичному виду
    }
}






abstract class Coordinate_old {
    private int minVal;
    private int maxVal;

    private double deg;
    private double min;
    private double sec;

    Coordinate_old(double deg) {
        this.deg = deg;
        this.min = 0;
        this.sec = 0;
    }

    Coordinate_old(int deg, int min, double sec, String coordLabel) {
        this.deg = dms2deg(deg, min, sec);
        this.min = min;
        this.sec = sec;

        if(coordLabel.equals("S") || coordLabel.equals("W")){
            this.deg = -this.deg;
        }
    }



    double getD() {
        //  В виде градусов с десятыми
        return deg;
    }

    private int getIntAbsD() {
        //  Целочисленное значение градусов по модулю
        return new BigDecimal((int)Math.abs(this.deg)).intValue();
    }

    int getIntD() {
        int deg = this.getIntAbsD();
        if(this.deg <0) deg = -deg;
        return deg;
    }

    int getIntMin() {
        //  Целые минуты

        return getDecimalPartInMinutes().intValue();
    }

    double getSec() {
        //  Секунды с плавающей точкой

        BigDecimal min = getDecimalPartInMinutes();
        BigDecimal sec = min.subtract(new BigDecimal(min.intValue())).multiply(new BigDecimal("60"));
        return sec.doubleValue();
    }

    private BigDecimal getDecimalPartInMinutes() {
        //  Дробную часть градуса выражаем в минутах
        String sDeg = String.valueOf(Math.abs(this.deg));
        BigDecimal absDeg = new BigDecimal(sDeg).abs();
        BigDecimal degDecimal = absDeg.subtract(new BigDecimal(this.getIntAbsD()));
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

    boolean isRightCoords() {
        boolean isRightCoords = false;
        if(this.minVal <= this.deg && this.deg <= this.maxVal) {
            if(0 <= this.min && this.min <= 59) {
                if(0 <= this.sec && this.sec <= 59) {
                    isRightCoords = true;
                }
            }
        }

        return isRightCoords;
    }

    void setBorders(int min, int max) {
        //  Устанавливаем границы координат
        this.minVal = min;
        this.maxVal = max;
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


class Lat extends Coordinate_old {

    Lat(double deg) {
        super(deg);
        this.setBorders(-90, 90);
    }


    Lat(int deg, int min, double sec, String coordLabel) {
        super(deg, min, sec, coordLabel);
        this.setBorders(-90, 90);
    }
}


class Lon extends Coordinate_old {

    Lon(double deg) {
        super(deg);
        this.setBorders(-180, 180);
    }

    Lon(int deg, int min, double sec, String coordLabel) {
        super(deg, min, sec, coordLabel);
        this.setBorders(-180, 180);
    }
}
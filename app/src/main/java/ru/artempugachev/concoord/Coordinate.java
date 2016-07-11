package ru.artempugachev.concoord;

import java.math.BigDecimal;
import java.math.MathContext;


public class Coordinate {

    public double getDecimalPres() {
        return decimalPres;
    }

    public void setDecimalPres(double decimalPres) {
        this.decimalPres = decimalPres;
    }

    private double decimalPres;         // Целочисленное представление координаты.
                                        // К нему приводятся все другие представления

    Coordinate (double degDec) {
        this.decimalPres = degDec;
    }

    Coordinate (int deg, int min, double sec, String coordLabel) {
        this.decimalPres = dms2d(deg, min, sec);


        if(coordLabel.equals("S") || coordLabel.equals("W")){
            this.decimalPres = -this.decimalPres;
        }
    }

    Coordinate (int deg, double min, String coordLabel) {
        this.decimalPres = dm2d(deg, min);


        if(coordLabel.equals("S") || coordLabel.equals("W")){
            this.decimalPres = -this.decimalPres;
        }
    }

    private double dm2d(int deg, double min) {
        BigDecimal absDeg = new BigDecimal(deg).abs();
        BigDecimal bdMin = new BigDecimal(min).divide(new BigDecimal("60"), MathContext.DECIMAL64);
        BigDecimal bdDeg = absDeg.add(bdMin);

        double ddeg = bdDeg.doubleValue();
        if(deg < 0) ddeg = -ddeg;               //ю.ш. и з.д. с минусом
        return ddeg;
    }

    private double dms2d(int deg, int min, double sec) {
        //  dms to d
        BigDecimal absDeg = new BigDecimal(deg).abs();
        BigDecimal bdMin = new BigDecimal(min).divide(new BigDecimal("60"), MathContext.DECIMAL64);
        BigDecimal bdSec = new BigDecimal(sec).divide(new BigDecimal("3600"), MathContext.DECIMAL64);

        BigDecimal bdDeg = absDeg.add(bdMin).add(bdSec);

        double ddeg = bdDeg.doubleValue();
        if(deg < 0) ddeg = -ddeg;               //ю.ш. и з.д. с минусом
        return ddeg;
    }

    public double asDDD() {
        return this.decimalPres;
    }

    private int getIntAbsD() {
        //  Целочисленное значение градусов по модулю
        return new BigDecimal((int)Math.abs(this.decimalPres)).intValue();
    }

    int getIntD() {
        int deg = this.getIntAbsD();
        if(this.decimalPres <0) deg = -deg;
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

    public double getDoubleMin() {
        //  Минуты с плавающей точкой (для DM)
        return  this.getDecimalPartInMinutes().doubleValue();
    }

    private BigDecimal getDecimalPartInMinutes() {
        //  Дробную часть градуса выражаем в минутах
        String sDeg = String.valueOf(Math.abs(this.decimalPres));
        BigDecimal absDeg = new BigDecimal(sDeg).abs();
        BigDecimal degDecimal = absDeg.subtract(new BigDecimal(this.getIntAbsD()));
        BigDecimal min = (degDecimal.multiply(new BigDecimal("60")));
        return min;
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
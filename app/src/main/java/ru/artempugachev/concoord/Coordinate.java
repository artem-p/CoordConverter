package ru.artempugachev.concoord;

import java.math.BigDecimal;
import java.math.MathContext;


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

    public Coordinate (String sDeg) {
        double degVal = 99999.0;
        if(!sDeg.isEmpty()) {
            degVal = Double.parseDouble(sDeg);
        }

        this.deg = degVal;
        this.min = 0;
        this.sec = 0;
    }

    public Coordinate (int deg, int min, double sec) {
        this.deg = dms2deg(deg, min, sec);
        this.min = min;
        this.sec = sec;
    }

    public Coordinate (String sDeg, String sMin, String sSec, String coordLabel) {
        this.deg = 99999.0;
        this.min = 99999.0;
        this.sec = 99999.0;

        if(!sDeg.isEmpty() && !sMin.isEmpty() && !sSec.isEmpty()) {
            int iDeg = Integer.parseInt(sDeg);
            int iMin = Integer.parseInt(sMin);
            double dSec = Double.parseDouble(sSec);

            this.deg = dms2deg(iDeg, iMin, dSec);
            this.min = iMin;
            this.sec = dSec;
            if(coordLabel.equals("S") || coordLabel.equals("W")){
                this.deg = -this.deg;
            }
        }

//        this.min = min;
//        this.sec = sec;
    }

    public double getD() {
        //  В виде градусов с десятыми
        return deg;
    }

    public int getIntAbsD() {
        //  Целочисленное значение градусов по модулю
        return new BigDecimal((int)Math.abs(this.deg)).intValue();
    }

    public int getIntD() {
        int deg = this.getIntAbsD();
        if(this.deg <0) deg = -deg;
        return deg;
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

    protected boolean isRightCoords() {
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

    protected void setBorders(int min, int max) {
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


class Lat extends Coordinate {

    public Lat(double deg) {
        super(deg);
        this.setBorders(-90, 90);
    }

    public Lat(String sDeg) {
        super(sDeg);
        this.setBorders(-90, 90);
    }

    public Lat(int deg, int min, double sec) {
        super(deg, min, sec);
        this.setBorders(-90, 90);
    }

    public Lat(String sDeg, String sMin, String sSec, String coordLabel) {
        super(sDeg, sMin, sSec, coordLabel);
        this.setBorders(-90, 90);
    }
}


class Lon extends Coordinate {

    public Lon(double deg) {
        super(deg);
        this.setBorders(-180, 180);
    }

    public Lon(String sDeg) {
        super(sDeg);
        this.setBorders(-180, 180);
    }

    public Lon(int deg, int min, double sec) {
        super(deg, min, sec);
        this.setBorders(-180, 180);
    }

    public Lon(String sDeg, String sMin, String sSec, String coordLabel) {
        super(sDeg, sMin, sSec, coordLabel);
        this.setBorders(-180, 180);
    }
}
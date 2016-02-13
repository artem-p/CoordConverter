package ru.artempugachev.coordconverter;

import java.math.BigDecimal;

/**
 * Created by artem on 31.01.16.
 */
public class Converter {
    //  Преобразуем координаты из одного формата в другой
    //  Во всех форматах отрицательные градусы означают южную широту и западную долготу

    public Dcoords convert(DMSCoords dms) {
        //  dms to d
        double deg = Math.abs(dms.getDeg()) + dms.getMin()/ 60.0 + dms.getSec()/ 3600.0;
        if(dms.getDeg() < 0) deg = -deg;
        Dcoords dcoords = new Dcoords(deg);
        return dcoords;
    }

    public DMSCoords convert(Dcoords dcoords) {

        String sDeg = String.valueOf(Math.abs(dcoords.getDeg()));
        BigDecimal absDeg = new BigDecimal(sDeg).abs();

        int degIntPart = new BigDecimal((int)Math.abs(dcoords.getDeg())).intValue();

        BigDecimal degDecimal = absDeg.subtract(new BigDecimal(degIntPart));
        BigDecimal min = (degDecimal.multiply(new BigDecimal("60")));
        int minIntPart = min.intValue();
        BigDecimal sec = min.subtract(new BigDecimal(minIntPart)).multiply(new BigDecimal("60"));
        if(dcoords.getDeg() < 0) degIntPart = - degIntPart;
        return new DMSCoords(degIntPart, minIntPart, sec.doubleValue());
    }
}

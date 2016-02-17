package ru.artempugachev.coordconverter;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by artem on 31.01.16.
 */
public class Converter {
    //  Преобразуем координаты из одного формата в другой
    //  Во всех форматах отрицательные градусы означают южную широту и западную долготу

    public Dcoords convert(DMSCoords dms) {
        //  dms to d
        BigDecimal absDeg = new BigDecimal(dms.getDeg()).abs();
        BigDecimal min = new BigDecimal(dms.getMin()).divide(new BigDecimal("60"), MathContext.DECIMAL64);
        BigDecimal sec = new BigDecimal(dms.getSec()).divide(new BigDecimal("3600"), MathContext.DECIMAL64);

        BigDecimal bdDeg = absDeg.add(min).add(sec);

        double deg = bdDeg.doubleValue();
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

    public void checkDmsCoordinates(Lat lat, Lon lon) throws WrongCoordinatesException{
        //  Проверяем координаты на попадание в диапазоны
        throw new WrongCoordinatesException("Неверное значение координат");
    }

    private void isValRight(String sVal, int min, int max) {
        //  Значение поля проверяем на попадание в диапазон
        boolean isRight = false;
        if(sVal != null && !sVal.isEmpty()) {
//            double
        }
    }

}
class WrongCoordinatesException extends Exception {
    public WrongCoordinatesException(String message) {
        super(message);
    }
}

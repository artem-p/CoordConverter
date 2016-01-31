package ru.artempugachev.coordconverter;

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
        int deg, min;
        double sec;

        deg = (int) Math.abs(dcoords.getDeg());
        min = (int) (Math.abs(dcoords.getDeg()) - deg)*60;
        sec = ((Math.abs(dcoords.getDeg()) - deg) * 60 - min) * 60;
        if(dcoords.getDeg() < 0) deg = - deg;
        DMSCoords dms = new DMSCoords(deg, min, sec);
        return dms;
    }
}

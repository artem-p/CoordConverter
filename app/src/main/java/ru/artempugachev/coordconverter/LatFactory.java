package ru.artempugachev.coordconverter;

/**
 * Created by artem on 21.02.16.
 */
public class LatFactory extends CoordinateFactory {
    @Override
    public Dcoords createDcoords(double deg) {
        return new Dcoords(deg, -90, 90);
    }


    @Override
    public DMSCoords createDMSCoords(int deg, int min, double sec) {
        return new DMSCoords(deg, min, sec, -90, 90);
    }
}

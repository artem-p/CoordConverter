package ru.artempugachev.coordconverter;

/**
 * Created by artem on 21.02.16.
 */
public class LonFactory extends CoordinateFactory {
    @Override
    public Dcoords createDcoords(double deg) {
        return new Dcoords(deg, -180, 180);
    }

    @Override
    public DMSCoords createDMSCoords(int deg, int min, double sec) {
        return new DMSCoords(deg, min, sec, -180, 180);
    }
}

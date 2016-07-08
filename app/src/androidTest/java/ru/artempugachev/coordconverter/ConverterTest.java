package ru.artempugachev.coordconverter;

import junit.framework.TestCase;

public class ConverterTest extends TestCase {


    public void testConvertDMStoDDD(){
        Lat lat = new Lat(0, 0, 0, "N");
        assertEquals(lat.getDecimalPres(), 0.0, 1e-8);

        Lon lon = new Lon(30, 30, 0, "E");
        assertEquals(lon.getDecimalPres(), 30.5, 1e-8);

        lat = new Lat(30, 30, 30, "N");
        assertEquals(lat.getDecimalPres(), 30.508333333333333, 1e-8);

        lat = new Lat(30, 30, 30, "S");
        assertEquals(lat.getDecimalPres(), -30.508333333333333, 1e-8);

        lon = new Lon(18, 38, 46.48, "W");
        assertEquals(lon.getDecimalPres(), -18.646244444444445, 1e-8);

        lat = new Lat(82, 58, 7.50, "N");
        assertEquals(lat.getDecimalPres(), 82.96875, 1e-8);

        lon = new Lon(67, 36, 14.36, "E");
        assertEquals(lon.getDecimalPres(), 67.60398889, 1e-8);

        lat = new Lat(63, 23, 29.48, "N");
        assertEquals(lat.getDecimalPres(), 63.39152222, 1e-8);
    }

    public void testConvertDMtoDDD() {
        Lat lat = new Lat(0, 0, "N");
        assertEquals(lat.getDecimalPres(), 0.0, 1e-8);

        Lon lon = new Lon(30, 30.0, "E");
        assertEquals(lon.getDecimalPres(), 30.5, 1e-8);

        lat = new Lat(30, 30.5, "N");
        assertEquals(lat.getDecimalPres(), 30.508333333333333, 1e-8);

        lat = new Lat(30, 30.5, "S");
        assertEquals(lat.getDecimalPres(), -30.508333333333333, 1e-8);
    }

    public void testConvertDDDtoDMS() {
        checkDDDtoDMS(-34.30714385628803, -34, 18, 25.717882636908);
        checkDDDtoDMS(63.39178056, 63, 23, 30.410016);
        checkDDDtoDMS(67.60398889, 67, 36, 14.360004);
        checkDDDtoDMS(55.8, 55, 48, 0.0);
        checkDDDtoDMS(30.0, 30, 0, 0.0);
        checkDDDtoDMS(60.9554, 60, 57, 19.44);
        checkDDDtoDMS(-23.78, -23, 46, 48.00);
    }

    private void checkDDDtoDMS(double deg, int degExpected, int minExpected, double secExpected) {
        Lat lat = new Lat(deg);

        assertEquals(degExpected, lat.getIntD());
        assertEquals(minExpected, lat.getIntMin());
        assertEquals(secExpected, lat.getSec(), 1e-10);

        Lon lon = new Lon(deg);

        assertEquals(degExpected, lon.getIntD());
        assertEquals(minExpected, lon.getIntMin());
        assertEquals(secExpected, lon.getSec(), 1e-10);
    }

    public void testConvertDDDtoDM() {
        Lat lat = new Lat(30.5);
        assertEquals(lat.getIntD(), 30);
        assertEquals(lat.getDoubleMin(), 30.0, 1e-8);

        Lon lon = new Lon(-45.7);
        assertEquals(lon.getIntD(), -45);
        assertEquals(lon.getDoubleMin(), 42.0, 1e-8);

        lat = new Lat(-50.333);
        assertEquals(lat.getIntD(), -50);
        assertEquals(lat.getDoubleMin(), 19.98, 1e-8);

        lon = new Lon(156.7567);
        assertEquals(lon.getIntD(), 156);
        assertEquals(lon.getDoubleMin(), 45.402, 1e-8);
    }
}

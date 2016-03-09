package ru.artempugachev.coordconverter;

import junit.framework.TestCase;


public class CoordinateTest extends TestCase {
    public void testStringConstructors() {
        Lat lat = new Lat("");
        assertEquals(99999.0, lat.getD(), 1e-1);

        lat = new Lat("35.5");
        assertEquals(35.5, lat.getD(), 1e-1);

        lat = new Lat("" , "", "", "");
        assertEquals(99999.0, lat.getD());

        lat = new Lat("30", "0", "0", "N");
        assertEquals(30.0, lat.getD());

        lat = new Lat("30", "0", "0", "S");
        assertEquals(-30.0, lat.getD());

        Lon lon = new Lon("");
        assertEquals(99999.0, lon.getD(), 1e-1);

        lon = new Lon("-178.3");
        assertEquals(-178.3, lon.getD(), 1e-1);

        lon = new Lon("", "", "", "");
        assertEquals(99999.0, lon.getD(), 1e-1);

        lon = new Lon("50", "0", "0", "E");
        assertEquals(50, lon.getD(), 1e-1);

        lon = new Lon("50", "0", "0", "W");
        assertEquals(-50, lon.getD(), 1e-1);

        lon = new Lon("50", "30", "0", "W");
        assertEquals(30, lon.getIntMin());
    }

    public void testDMSRightCoordinates(){
        Lat lat;
        lat = new Lat(-90, 1, 0);
        assertFalse(lat.isRightCoords());

        lat = new Lat(30, 0, 0);
        assertTrue(lat.isRightCoords());

        lat = new Lat(89, 59, 59);
        assertTrue(lat.isRightCoords());

        lat = new Lat(90, 0, 1);
        assertFalse(lat.isRightCoords());


        Lon lon = new Lon(0, 0, 0);
        assertTrue(lon.isRightCoords());

        lon = new Lon(18, 66, 0);
        assertFalse(lon.isRightCoords());

        lon = new Lon(30, 30, -5);
        assertFalse(lon.isRightCoords());
    }
    public void testDDDRightCoordinates() {

        //  Проверяем, что координаты попадают в диапазон
        Lat lat = new Lat(30.0);
        assertTrue(lat.isRightCoords());

        lat = new Lat(-91);
        assertFalse(lat.isRightCoords());

        lat = new Lat(91);
        assertFalse(lat.isRightCoords());

        Lon lon = new Lon(45);
        assertTrue(lon.isRightCoords());

        lon = new Lon(0.0);
        assertTrue(lon.isRightCoords());

        lon = new Lon(-181);
        assertFalse(lon.isRightCoords());

        lon = new Lon(181);
        assertFalse(lon.isRightCoords());
    }



}

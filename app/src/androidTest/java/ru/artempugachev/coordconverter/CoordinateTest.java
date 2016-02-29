package ru.artempugachev.coordconverter;

import junit.framework.TestCase;

/**
 * Created by artem on 22.02.16.
 */
public class CoordinateTest extends TestCase {
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

//package ru.artempugachev.coordconverter;
//
//import junit.framework.TestCase;
//
//
//public class CoordinateTest extends TestCase {
//
//
//    public void testDMSRightCoordinates(){
//        Lat lat;
//        lat = new Lat(90, 1, 0, "S");
//        assertFalse(lat.isRightCoords());
//
//        lat = new Lat(30, 0, 0, "N");
//        assertTrue(lat.isRightCoords());
//
//        lat = new Lat(89, 59, 59, "N");
//        assertTrue(lat.isRightCoords());
//
//        lat = new Lat(90, 0, 1, "N");
//        assertFalse(lat.isRightCoords());
//
//
//        Lon lon = new Lon(0, 0, 0, "E");
//        assertTrue(lon.isRightCoords());
//
//        lon = new Lon(18, 66, 0, "E");
//        assertFalse(lon.isRightCoords());
//
//        lon = new Lon(30, 30, -5, "E");
//        assertFalse(lon.isRightCoords());
//    }
//    public void testDDDRightCoordinates() {
//
//        //  Проверяем, что координаты попадают в диапазон
//        Lat lat = new Lat(30.0);
//        assertTrue(lat.isRightCoords());
//
//        lat = new Lat(-91);
//        assertFalse(lat.isRightCoords());
//
//        lat = new Lat(91);
//        assertFalse(lat.isRightCoords());
//
//        Lon lon = new Lon(45);
//        assertTrue(lon.isRightCoords());
//
//        lon = new Lon(0.0);
//        assertTrue(lon.isRightCoords());
//
//        lon = new Lon(-181);
//        assertFalse(lon.isRightCoords());
//
//        lon = new Lon(181);
//        assertFalse(lon.isRightCoords());
//    }
//
//
//
//}

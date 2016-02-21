package ru.artempugachev.coordconverter;

import junit.framework.TestCase;

/**
 * Created by artem on 22.02.16.
 */
public class CoordinateTest extends TestCase {
    private CoordinateFactory coordinateFactory;
    public void testRightCoordinates() {

        //  Проверяем, что координаты попадают в диапазон
        Dcoords dLat = coordinateFactory.createDcoords(35);
        assertTrue(dLat.isRightCoords());
    }
}

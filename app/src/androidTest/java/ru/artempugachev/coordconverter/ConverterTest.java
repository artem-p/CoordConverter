package ru.artempugachev.coordconverter;

import junit.framework.TestCase;

/**
 * Created by artem on 31.01.16.
 */
public class ConverterTest extends TestCase {
    private Converter mConverter;
    private Dcoords ddd;
    private DMSCoords dms;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mConverter = new Converter();
    }

    public void testConvertDMStoDDD(){
        checkDMStoDDD(0, 0, 0, 0.0);
        checkDMStoDDD(30, 30, 0, 30.5);
        checkDMStoDDD(30, 30, 30, 30.508333333333333);
        checkDMStoDDD(-30, 30, 30, -30.508333333333333);
    }

    private void checkDMStoDDD(int deg, int min, double sec, double degExpected) {
        dms = new DMSCoords(deg, min, sec);
        ddd = mConverter.convert(dms);
        assertEquals(degExpected, ddd.getDeg(), 1e-10);
    }

    public void testConvertDDDtoDMS() {
        checkDDDtoDMS(30.0, 30, 0, 0.0);
        checkDDDtoDMS(55.8, 55, 48, 0.0);
        checkDDDtoDMS(60.9554, 60, 57, 19.44);
    }

    private void checkDDDtoDMS(double deg, int degExpected, int minExpected, double secExpected) {
        ddd = new Dcoords(deg);
        dms = mConverter.convert(ddd);
        assertEquals(degExpected, dms.getDeg());
        assertEquals(minExpected, dms.getMin());
        assertEquals(secExpected, dms.getSec(), 1e-10);
    }
}

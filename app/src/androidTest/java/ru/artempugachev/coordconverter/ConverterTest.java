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
        checkDMStoDDD(-18, 38, 46.48, -18.646244444444445);
        checkDMStoDDD(82, 58, 7.50, 82.96875);
        checkDMStoDDD(67, 36, 14.36, 67.60398889);
        checkDMStoDDD(63, 23, 29.48, 63.39152222);
    }

    private void checkDMStoDDD(int deg, int min, double sec, double degExpected) {
        dms = new DMSCoords(deg, min, sec);
        ddd = mConverter.convert(dms);
        assertEquals(degExpected, ddd.getDeg(), 1e-8);
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
        ddd = new Dcoords(deg);
        dms = mConverter.convert(ddd);
        assertEquals(degExpected, dms.getDeg());
        assertEquals(minExpected, dms.getMin());
        assertEquals(secExpected, dms.getSec(), 1e-10);
    }
}

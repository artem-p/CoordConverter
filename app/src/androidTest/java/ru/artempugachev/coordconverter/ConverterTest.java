package ru.artempugachev.coordconverter;

import android.app.Application;
import android.test.ApplicationTestCase;
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
        dms = new DMSCoords(0, 0, 0);
        ddd = mConverter.convert(dms);
        assertEquals(0.0, ddd.getDeg());

        dms = new DMSCoords(30, 30, 0);
        ddd = mConverter.convert(dms);
        assertEquals(30.5, ddd.getDeg());

        dms = new DMSCoords(30, 30, 30);
        ddd = mConverter.convert(dms);
        assertEquals(30.508333333333333, ddd.getDeg(), 0);

    }

}

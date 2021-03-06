package ru.artempugachev.concoord;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class MainActivityOld extends ActionBarActivity {
    //  todo вкладки dms-ddd, dms-dm, dm-ddd
    private Toolbar mToolbar;
    private Spinner mLatSpinner;
    private Spinner mLonSpinner;
//    private Converter mConverter;
    private TextView mLatDeg, mLatMin, mLatSec, mLonDeg, mLonMin, mLonSec, mDecLat, mDecLon;
    private ImageButton mDmsToDdBtn, mDdToDmsBtn;
    private NumberFormat mNf = NumberFormat.getNumberInstance(Locale.ENGLISH);
    private DecimalFormat mDmsSecFormat = (DecimalFormat) mNf;
    private DecimalFormat mDddformat = (DecimalFormat) mNf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_no_tabs);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        createViews();

        mDmsSecFormat.setMaximumFractionDigits(4);
        mDddformat.setMaximumFractionDigits(7);

//        mConverter = new Converter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                clearFields();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearFields() {
        //  Сбрасываем все поля
        mLatDeg.setText("");
        mLatMin.setText("");
        mLatSec.setText("");
        mLonDeg.setText("");
        mLonMin.setText("");
        mLonSec.setText("");
        mDecLat.setText("");
        mDecLon.setText("");
    }


    private void createViews() {
        mLatDeg = (TextView) findViewById(R.id.latDeg);
        mLatMin = (TextView) findViewById(R.id.latMin);
        mLatSec = (TextView) findViewById(R.id.latSec);
        mLonDeg = (TextView) findViewById(R.id.lonDeg);
        mLonMin = (TextView) findViewById(R.id.lonMin);
        mLonSec = (TextView) findViewById(R.id.lonSec);
        mDecLat = (TextView) findViewById(R.id.latDecimal);
        mDecLon = (TextView) findViewById(R.id.lonDecimal);
        mDmsToDdBtn = (ImageButton) findViewById(R.id.btnDmsToDeg);
        mDdToDmsBtn = (ImageButton) findViewById(R.id.btnDegToDms);
        mDmsToDdBtn.setOnClickListener(new dmsToDListener());
        mDdToDmsBtn.setOnClickListener(new dToDmsListener());

        String latLabels[] = {"N", "S"};
        String lonLabels[] = {"E", "W"};
        mLatSpinner = (Spinner) findViewById(R.id.latSpinner);
        mLonSpinner = (Spinner) findViewById(R.id.lonSpinner);
        populateCoordinateSpinner(latLabels, mLatSpinner);
        populateCoordinateSpinner(lonLabels, mLonSpinner);
    }

    private boolean checkDMSCoord(String sDeg, String sMin, String sSec, int minDeg, int maxDeg) {
        boolean isRightCoords = false;

        int deg = Integer.parseInt(sDeg);
        int min = Integer.parseInt(sMin);
        double sec = Double.parseDouble(sSec);

        if(minDeg <= deg && deg <= maxDeg) {
            if(0 <= min && min <= 59) {
                if(0 <= sec && sec <= 59) {
                    isRightCoords = true;
                }
            }
        }

        return isRightCoords;
    }

    private boolean checkDCoord(String sDeg, int minDeg, int maxDeg) {
        boolean isRightCoords = false;

        double deg = Double.parseDouble(sDeg);

        if(minDeg <= deg && deg <= maxDeg) {
                    isRightCoords = true;
        }

        return isRightCoords;
    }


    private class dmsToDListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            setMissingVals();

            String latDeg = String.valueOf(mLatDeg.getText());
            String latMin = String.valueOf(mLatMin.getText());
            String latSec = String.valueOf(mLatSec.getText());
            String latLabel = (String) mLatSpinner.getSelectedItem();
            String lonDeg = String.valueOf(mLonDeg.getText());
            String lonMin = String.valueOf(mLonMin.getText());
            String lonSec = String.valueOf(mLonSec.getText());
            String lonLabel = (String) mLonSpinner.getSelectedItem();



            if(checkDMSCoord(latDeg, latMin, latSec, -90, 90)) {
                if (checkDMSCoord(lonDeg, lonMin, lonSec, -180, 180)) {
                    Coordinate lat = new Coordinate(Integer.parseInt(latDeg),
                            Integer.parseInt(latMin), Double.parseDouble(latSec), latLabel);
                    Coordinate lon = new Coordinate(Integer.parseInt(lonDeg), Integer.parseInt(lonMin),
                            Double.parseDouble(lonSec), lonLabel);

                    //  Устанавливаем значения в ddd
                    String sLat = mDddformat.format(lat.getDecimalPres());
                    String sLon = mDddformat.format(lon.getDecimalPres());
                    mDecLat.setText(sLat);
                    mDecLon.setText(sLon);
                } else {
                    Toast.makeText(MainActivityOld.this, "Неверное значение долготы", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivityOld.this, "Неверное значение широты", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private class dToDmsListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            setMissingVals();

            String sLat = String.valueOf(mDecLat.getText());
            String sLon = String.valueOf(mDecLon.getText());

            if(checkDCoord(sLat, -90, 90)) {
                if(checkDCoord(sLon, -180, 180)) {
                    Coordinate dLat = new Coordinate(Double.parseDouble(sLat));
                    Coordinate dLon = new Coordinate(Double.parseDouble(sLon));
                    //  Устанавливаем значения в поля dms
                    mLatDeg.setText(String.valueOf(Math.abs(dLat.getIntD())));
                    mLatMin.setText(String.valueOf(dLat.getIntMin()));

                    String sLatSec = mDmsSecFormat.format(dLat.getSec());
                    mLatSec.setText(sLatSec);

                    mLonDeg.setText(String.valueOf(Math.abs(dLon.getIntD())));
                    mLonMin.setText(String.valueOf(dLon.getIntMin()));

                    String sLonSec = mDmsSecFormat.format(dLon.getSec());
                    mLonSec.setText(String.valueOf(sLonSec));

                    //  Устанавливаем спиннер с полушариями
                    if(dLat.getIntD() >= 0) {
                        mLatSpinner.setSelection(0);            //  N
                    } else {
                        mLatSpinner.setSelection(1);            //  S
                    }

                    if(dLon.getIntD() >= 0) {
                        mLonSpinner.setSelection(0);            //  E
                    } else {
                        mLonSpinner.setSelection(1);            //  W
                    }
                } else {
                    Toast.makeText(MainActivityOld.this, "Неверное значение долготы", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivityOld.this, "Неверное значение широты", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void setMissingVals() {
        //  Устанавливаем отсутствующие значения в 0
        setToNullIfMissing(mLatDeg);
        setToNullIfMissing(mLatMin);
        setToNullIfMissing(mLatSec);
        setToNullIfMissing(mLonDeg);
        setToNullIfMissing(mLonMin);
        setToNullIfMissing(mLonSec);
        setToNullIfMissing(mDecLat);
        setToNullIfMissing(mDecLon);
    }

    private void setToNullIfMissing(TextView textView) {
        String sVal = String.valueOf(textView.getText());
        if(sVal.equals("")) textView.setText("0");
    }


    private void populateCoordinateSpinner(String[] labels, Spinner spinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, labels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }


}

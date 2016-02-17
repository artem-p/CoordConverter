package ru.artempugachev.coordconverter;

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


public class MainActivity extends ActionBarActivity {
    //  todo сделать инпуты по гайдлайнам, со сдвигающимся лэйблами
    // todo десятичный вывод ограничить до 7 знаков
    //  todo вкладки dms-ddd, dms-dm, dm-ddd
    private Toolbar mToolbar;
    private Spinner mLatSpinner;
    private Spinner mLonSpinner;
    private Converter mConverter;
    private TextView mLatDeg, mLatMin, mLatSec, mLonDeg, mLonMin, mLonSec, mDecLat, mDecLon;
    private ImageButton mDmsToDdBtn, mDdToDmsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        createViews();

        mConverter = new Converter();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        mDmsToDdBtn.setOnClickListener(new dmsToDdListener());
        mDdToDmsBtn.setOnClickListener(new ddToDmsListener());

        String latLabels[] = {"N", "S"};
        String lonLabels[] = {"E", "W"};
        mLatSpinner = (Spinner) findViewById(R.id.latSpinner);
        mLonSpinner = (Spinner) findViewById(R.id.lonSpinner);
        populateCoordinateSpinner(latLabels, mLatSpinner);
        populateCoordinateSpinner(lonLabels, mLonSpinner);
    }

    private class dmsToDdListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            try {
                // todo lat и lon extends dmscoords
                mConverter.checkDmsCoordinates(new Lat(mLatDeg.getText(), mLatMin.getText(), mLatSec.getText()),
                        new Lon(mLonDeg.getText(), mLonMin.getText(), mLonSec.getText()));
            }
            catch (WrongCoordinatesException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class ddToDmsListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    private void populateCoordinateSpinner(String[] labels, Spinner spinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, labels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }


}

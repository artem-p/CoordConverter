package ru.artempugachev.concoord;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DmsToDmFragment extends Fragment {
    //  todo расширить поле для минут в dm
    private Spinner dmsLatSpinner, dmLatSpinner, dmsLonSpinner, dmLonSpinner;
    private TextView dmsLatDeg, dmsLatMin, dmsLatSec, dmsLonDeg, dmsLonMin, dmsLonSec;
    private TextView dmLatDeg, dmLatMin, dmLonDeg, dmLonMin;
    private ImageButton dmsToDmBtn, dmToDmsBtn;
    private NumberFormat mNf = NumberFormat.getNumberInstance(Locale.ENGLISH);
    private DecimalFormat mDmMinFormat = (DecimalFormat) mNf;
    private DecimalFormat mDmsSecFormat = (DecimalFormat) mNf;

    public DmsToDmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDmMinFormat.setMaximumFractionDigits(4);
        mDmsSecFormat.setMaximumFractionDigits(4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dms_to_dm_layout, container, false);
        dmsLatDeg = (TextView) v.findViewById(R.id.latDeg);
        dmsLatMin = (TextView) v.findViewById(R.id.latMin);
        dmsLatSec = (TextView) v.findViewById(R.id.latSec);
        dmsLonDeg = (TextView) v.findViewById(R.id.lonDeg);
        dmsLonMin = (TextView) v.findViewById(R.id.lonMin);
        dmsLonSec = (TextView) v.findViewById(R.id.lonSec);


        dmLatDeg = (TextView) v.findViewById(R.id.dmLatDeg);
        dmLatMin = (TextView) v.findViewById(R.id.dmLatMin);
        dmLonDeg = (TextView) v.findViewById(R.id.dmLonDeg);
        dmLonMin = (TextView) v.findViewById(R.id.dmLonMin);

        dmsToDmBtn = (ImageButton) v.findViewById(R.id.btnDmsToDm);
        dmToDmsBtn = (ImageButton) v.findViewById(R.id.btnDmToDms);
        dmsToDmBtn.setOnClickListener(new DmsToDmListener());
        dmToDmsBtn.setOnClickListener(new DmToDmsListener());

        String latLabels[] = {"N", "S"};
        String lonLabels[] = {"E", "W"};

        dmsLatSpinner = (Spinner) v.findViewById(R.id.dmsLatSpinner);
        dmsLonSpinner = (Spinner) v.findViewById(R.id.dmsLonSpinner);
        dmLatSpinner = (Spinner) v.findViewById(R.id.dmLatSpinner);
        dmLonSpinner = (Spinner) v.findViewById(R.id.dmLonSpinner);
        populateCoordinateSpinner(latLabels, dmsLatSpinner);
        populateCoordinateSpinner(lonLabels, dmsLonSpinner);
        populateCoordinateSpinner(latLabels, dmLatSpinner);
        populateCoordinateSpinner(lonLabels, dmLonSpinner);
        return v;
    }

    private void populateCoordinateSpinner(String[] labels, Spinner spinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, labels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }


    public void clearFields() {
        //  Сбрасываем все поля
        dmsLatDeg.setText("");
        dmsLatMin.setText("");
        dmsLatSec.setText("");
        dmsLonDeg.setText("");
        dmsLonMin.setText("");
        dmsLonSec.setText("");


        dmLatDeg.setText("");
        dmLatMin.setText("");
        dmLonDeg.setText("");
        dmLonMin.setText("");
    }

    private void setMissingVals() {
        //  Устанавливаем отсутствующие значения в 0
        MainActivity.setToNullIfMissing(dmsLatDeg);
        MainActivity.setToNullIfMissing(dmsLatMin);
        MainActivity.setToNullIfMissing(dmsLatSec);
        MainActivity.setToNullIfMissing(dmsLonDeg);
        MainActivity.setToNullIfMissing(dmsLonMin);
        MainActivity.setToNullIfMissing(dmsLonSec);

        MainActivity.setToNullIfMissing(dmLatDeg);
        MainActivity.setToNullIfMissing(dmLatMin);
        MainActivity.setToNullIfMissing(dmLonDeg);
        MainActivity.setToNullIfMissing(dmLonMin);
    }

    public void updateLocation(Coordinate lat, Coordinate lon) {
        updateDmsFields(lat, lon);
        updateDmFiels(lat, lon);
    }


    private class DmsToDmListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            setMissingVals();

            String latDeg = String.valueOf(dmsLatDeg.getText());
            String latMin = String.valueOf(dmsLatMin.getText());
            String latSec = String.valueOf(dmsLatSec.getText());
            String latLabel = (String) dmsLatSpinner.getSelectedItem();
            String lonDeg = String.valueOf(dmsLonDeg.getText());
            String lonMin = String.valueOf(dmsLonMin.getText());
            String lonSec = String.valueOf(dmsLonSec.getText());
            String lonLabel = (String) dmsLonSpinner.getSelectedItem();



            if(MainActivity.checkDMSCoord(latDeg, latMin, latSec, -90, 90)) {
                if (MainActivity.checkDMSCoord(lonDeg, lonMin, lonSec, -180, 180)) {
                    Coordinate lat = new Coordinate(Integer.parseInt(latDeg),
                            Integer.parseInt(latMin), Double.parseDouble(latSec), latLabel);
                    Coordinate lon = new Coordinate(Integer.parseInt(lonDeg), Integer.parseInt(lonMin),
                            Double.parseDouble(lonSec), lonLabel);

                    updateDmFiels(lat, lon);

                } else {
                    Toast.makeText(getActivity(), R.string.wrong_long, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.wrong_lat, Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void updateDmFiels(Coordinate lat, Coordinate lon) {

        //  Устанавливаем значения в поля dm
        dmLatDeg.setText(String.valueOf(Math.abs(lat.getIntD())));

        String sLatMin = mDmMinFormat.format(lat.getDoubleMin());
        dmLatMin.setText(sLatMin);

        dmLonDeg.setText(String.valueOf(Math.abs(lon.getIntD())));

        String sLonMin = mDmMinFormat.format(lon.getDoubleMin());
        dmLonMin.setText(String.valueOf(sLonMin));

        //  Устанавливаем спиннер с полушариями
        if(lat.getIntD() >= 0) {
            dmLatSpinner.setSelection(0);            //  N
        } else {
            dmLatSpinner.setSelection(1);            //  S
        }

        if(lon.getIntD() >= 0) {
            dmLonSpinner.setSelection(0);            //  E
        } else {
            dmLonSpinner.setSelection(1);            //  W
        }

    }

    private class DmToDmsListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            setMissingVals();

            String latDeg = String.valueOf(dmLatDeg.getText());
            String latMin = String.valueOf(dmLatMin.getText());
            String latLabel = (String) dmLatSpinner.getSelectedItem();
            String lonDeg = String.valueOf(dmLonDeg.getText());
            String lonMin = String.valueOf(dmLonMin.getText());
            String lonLabel = (String) dmLonSpinner.getSelectedItem();

            if(MainActivity.checkDMCoord(latDeg, latMin, -90, 90)) {
                if (MainActivity.checkDMCoord(lonDeg, lonMin, -180, 180)) {
                    Coordinate lat = new Coordinate(Integer.parseInt(latDeg),
                            Double.parseDouble(latMin), latLabel);
                    Coordinate lon = new Coordinate(Integer.parseInt(lonDeg),
                            Double.parseDouble(lonMin), lonLabel);
                    updateDmsFields(lat, lon);

                } else {
                    Toast.makeText(getActivity(), R.string.wrong_long, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.wrong_lat, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void updateDmsFields(Coordinate lat, Coordinate lon) {

        //  Устанавливаем значения в поля dms
        dmsLatDeg.setText(String.valueOf(Math.abs(lat.getIntD())));
        dmsLatMin.setText(String.valueOf(lat.getIntMin()));

        String sLatSec = mDmsSecFormat.format(lat.getSec());
        dmsLatSec.setText(sLatSec);

        dmsLonDeg.setText(String.valueOf(Math.abs(lon.getIntD())));
        dmsLonMin.setText(String.valueOf(lon.getIntMin()));

        String sLonSec = mDmsSecFormat.format(lon.getSec());
        dmsLonSec.setText(String.valueOf(sLonSec));

        //  Устанавливаем спиннер с полушариями
        if(lat.getIntD() >= 0) {
            dmsLatSpinner.setSelection(0);            //  N
        } else {
            dmsLatSpinner.setSelection(1);            //  S
        }

        if(lon.getIntD() >= 0) {
            dmsLonSpinner.setSelection(0);            //  E
        } else {
            dmsLonSpinner.setSelection(1);            //  W
        }

    }
}

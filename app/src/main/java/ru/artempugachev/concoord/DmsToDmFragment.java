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

    public DmsToDmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


    private class DmsToDmListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            setMissingVals();

            String latDeg = String.valueOf(mLatDeg.getText());
            String latMin = String.valueOf(mLatMin.getText());
            String latLabel = (String) mLatSpinner.getSelectedItem();
            String lonDeg = String.valueOf(mLonDeg.getText());
            String lonMin = String.valueOf(mLonMin.getText());
            String lonLabel = (String) mLonSpinner.getSelectedItem();



            if(MainActivity.checkDMCoord(latDeg, latMin, -90, 90)) {
                if (MainActivity.checkDMCoord(lonDeg, lonMin, -180, 180)) {
                    Coordinate lat = new Coordinate(Integer.parseInt(latDeg),
                            Integer.parseInt(latMin), latLabel);
                    Coordinate lon = new Coordinate(Integer.parseInt(lonDeg),
                            Integer.parseInt(lonMin), lonLabel);

                    //  Устанавливаем значения в ddd
                    String sLat = mDddformat.format(lat.getDecimalPres());
                    String sLon = mDddformat.format(lon.getDecimalPres());
                    mDecLat.setText(sLat);
                    mDecLon.setText(sLon);
                } else {
                    Toast.makeText(getActivity(), "Неверное значение долготы", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Неверное значение широты", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private class DmToDmsListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            setMissingVals();

            String sLat = String.valueOf(mDecLat.getText());
            String sLon = String.valueOf(mDecLon.getText());

            if(MainActivity.checkDCoord(sLat, -90, 90)) {
                if(MainActivity.checkDCoord(sLon, -180, 180)) {
                    Coordinate dLat = new Coordinate(Double.parseDouble(sLat));
                    Coordinate dLon = new Coordinate(Double.parseDouble(sLon));
                    //  Устанавливаем значения в поля dms
                    mLatDeg.setText(String.valueOf(Math.abs(dLat.getIntD())));

                    String sLatMin = mDmMinFormat.format(dLat.getDoubleMin());
                    mLatMin.setText(sLatMin);

                    mLonDeg.setText(String.valueOf(Math.abs(dLon.getIntD())));

                    String sLonMin = mDmMinFormat.format(dLon.getDoubleMin());
                    mLonMin.setText(String.valueOf(sLonMin));

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
                    Toast.makeText(getActivity(), "Неверное значение долготы", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Неверное значение широты", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

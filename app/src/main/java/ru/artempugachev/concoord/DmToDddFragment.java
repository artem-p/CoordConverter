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

public class DmToDddFragment extends Fragment {
    //  todo расширить поле для минут в dm
    private Spinner mLatSpinner;
    private Spinner mLonSpinner;
    private TextView mLatDeg, mLatMin, mLonDeg, mLonMin, mDecLat, mDecLon;
    private ImageButton mDmToDdBtn, mDdToDmBtn;
    private NumberFormat mNf = NumberFormat.getNumberInstance(Locale.ENGLISH);
    private DecimalFormat mDmMinFormat = (DecimalFormat) mNf;
    private DecimalFormat mDddformat = (DecimalFormat) mNf;

    public DmToDddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDmMinFormat.setMaximumFractionDigits(4);
        mDddformat.setMaximumFractionDigits(7);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dm_to_ddd_layout, container, false);

        mLatDeg = (TextView) v.findViewById(R.id.latDeg);
        mLatMin = (TextView) v.findViewById(R.id.latMin);
        mLonDeg = (TextView) v.findViewById(R.id.lonDeg);
        mLonMin = (TextView) v.findViewById(R.id.lonMin);
        mDecLat = (TextView) v.findViewById(R.id.latDecimal);
        mDecLon = (TextView) v.findViewById(R.id.lonDecimal);
        mDmToDdBtn = (ImageButton) v.findViewById(R.id.btnDmToDeg);
        mDdToDmBtn = (ImageButton) v.findViewById(R.id.btnDegToDm);
        mDmToDdBtn.setOnClickListener(new DmToDListener());
        mDdToDmBtn.setOnClickListener(new DToDmListener());

        String latLabels[] = {"N", "S"};
        String lonLabels[] = {"E", "W"};
        mLatSpinner = (Spinner) v.findViewById(R.id.latSpinner);
        mLonSpinner = (Spinner) v.findViewById(R.id.lonSpinner);
        populateCoordinateSpinner(latLabels, mLatSpinner);
        populateCoordinateSpinner(lonLabels, mLonSpinner);

        return v;
    }

    public void clearFields() {
        //  Сбрасываем все поля
        mLatDeg.setText("");
        mLatMin.setText("");
        mLonDeg.setText("");
        mLonMin.setText("");
        mDecLat.setText("");
        mDecLon.setText("");
    }

    public void updateLocation(Coordinate lat, Coordinate lon) {
        mDecLat.setText(String.valueOf(lat.asDDD()));
        mDecLon.setText(String.valueOf(lon.asDDD()));
        updateDmFields(lat, lon);
    }

    private class DmToDListener implements View.OnClickListener{

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
                    Toast.makeText(getActivity(), R.string.wrong_long, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.wrong_lat, Toast.LENGTH_SHORT).show();
            }


        }
    }

    private class DToDmListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            setMissingVals();

            String sLat = String.valueOf(mDecLat.getText());
            String sLon = String.valueOf(mDecLon.getText());

            if(MainActivity.checkDCoord(sLat, -90, 90)) {
                if(MainActivity.checkDCoord(sLon, -180, 180)) {
                    Coordinate dLat = new Coordinate(Double.parseDouble(sLat));
                    Coordinate dLon = new Coordinate(Double.parseDouble(sLon));
                    updateDmFields(dLat, dLon);
                } else {
                    Toast.makeText(getActivity(), R.string.wrong_long, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.wrong_lat, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void updateDmFields(Coordinate dLat, Coordinate dLon) {
        //  Устанавливаем значения в поля dm
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

    }

    private void setMissingVals() {
        //  Устанавливаем отсутствующие значения в 0
        MainActivity.setToNullIfMissing(mLatDeg);
        MainActivity.setToNullIfMissing(mLatMin);
        MainActivity.setToNullIfMissing(mLonDeg);
        MainActivity.setToNullIfMissing(mLonMin);
        MainActivity.setToNullIfMissing(mDecLat);
        MainActivity.setToNullIfMissing(mDecLon);
    }

    private void populateCoordinateSpinner(String[] labels, Spinner spinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, labels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }
}

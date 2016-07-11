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

public class DmsToDddFragment extends Fragment {
    private Spinner mLatSpinner;
    private Spinner mLonSpinner;
    private TextView mLatDeg, mLatMin, mLatSec, mLonDeg, mLonMin, mLonSec, mDecLat, mDecLon;
    private ImageButton mDmsToDdBtn, mDdToDmsBtn;
    private NumberFormat mNf = NumberFormat.getNumberInstance(Locale.ENGLISH);
    private DecimalFormat mDmsSecFormat = (DecimalFormat) mNf;
    private DecimalFormat mDddformat = (DecimalFormat) mNf;

    public DmsToDddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDmsSecFormat.setMaximumFractionDigits(4);
        mDddformat.setMaximumFractionDigits(7);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dms_to_ddd_layout, container, false);
        mLatDeg = (TextView) v.findViewById(R.id.latDeg);
        mLatMin = (TextView) v.findViewById(R.id.latMin);
        mLatSec = (TextView) v.findViewById(R.id.latSec);
        mLonDeg = (TextView) v.findViewById(R.id.lonDeg);
        mLonMin = (TextView) v.findViewById(R.id.lonMin);
        mLonSec = (TextView) v.findViewById(R.id.lonSec);
        mDecLat = (TextView) v.findViewById(R.id.latDecimal);
        mDecLon = (TextView) v.findViewById(R.id.lonDecimal);
        mDmsToDdBtn = (ImageButton) v.findViewById(R.id.btnDmsToDeg);
        mDdToDmsBtn = (ImageButton) v.findViewById(R.id.btnDegToDms);
        mDmsToDdBtn.setOnClickListener(new DmsToDListener());
        mDdToDmsBtn.setOnClickListener(new DToDmsListener());

        String latLabels[] = {"N", "S"};
        String lonLabels[] = {"E", "W"};
        mLatSpinner = (Spinner) v.findViewById(R.id.latSpinner);
        mLonSpinner = (Spinner) v.findViewById(R.id.lonSpinner);
        populateCoordinateSpinner(latLabels, mLatSpinner);
        populateCoordinateSpinner(lonLabels, mLonSpinner);

        return v;
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


    private class DmsToDListener implements View.OnClickListener{

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
                    Toast.makeText(getActivity(), "Неверное значение долготы", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Неверное значение широты", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private class DToDmsListener implements View.OnClickListener {

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
                    Toast.makeText(getActivity(), "Неверное значение долготы", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Неверное значение широты", Toast.LENGTH_SHORT).show();
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
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, labels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }
}

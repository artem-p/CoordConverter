package ru.artempugachev.concoord;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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

}

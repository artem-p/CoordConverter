package ru.artempugachev.concoord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.System.in;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    //  todo вкладка свободных преобразований как в https://play.google.com/store/apps/details?id=com.in_con.coordinateconverter
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView mLatDeg, mLatMin, mLatSec, mLonDeg, mLonMin, mLonSec, mDecLat, mDecLon;
    private int DMS_TO_DDD_TAB_POS = 0;
    private int DM_TO_DDD_TAB_POS = 1;
    private int DMS_TO_DM_TAB_POS = 2;
    private GoogleApiClient mGoogleApiClient;
    private final static int REQUEST_LOCATION = 1;
    private Location mLocation;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //  Проверяем доступность play services
        if(checkPlayServices()) {
            //  Получаем google api client
            buildGoogleApiClient();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                FragmentPagerAdapter fa = (FragmentPagerAdapter) viewPager.getAdapter();
                if (viewPager.getCurrentItem() == DMS_TO_DDD_TAB_POS) {
                    DmsToDddFragment fragment = (DmsToDddFragment) fa.getItem(viewPager.getCurrentItem());
                    fragment.clearFields();
                }

                if (viewPager.getCurrentItem() == DM_TO_DDD_TAB_POS) {
                    DmToDddFragment fragment = (DmToDddFragment) fa.getItem(viewPager.getCurrentItem());
                    fragment.clearFields();
                }

                if (viewPager.getCurrentItem() == DMS_TO_DM_TAB_POS) {
                    DmsToDmFragment fragment = (DmsToDmFragment) fa.getItem(viewPager.getCurrentItem());
                    fragment.clearFields();
                }

                return true;
            case R.id.action_location_search:
                Toast.makeText(MainActivity.this, R.string.getting_location, Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //  Нет разрешения на использование местоположения, запрашиваем и лови колбэк потом
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_LOCATION);
                } else {
                    //  Есть разрешения на использование местоположения
                    outputLocation();
                }


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted. Do the
                    // location-related task you need to do.
                    outputLocation();
                } else {
                    // permission denied. Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //    private void clearFields(TextView[] fields) {
//        //  Сбрасываем все поля
//        for (TextView field : fields) {
//            field.setText("");
//        }
//    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(result)) {
                googleApiAvailability.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.device_is_not_supported, Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void outputLocation() {
        //  Получаем местоположение и выводим его в виде координат
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation != null) {
            String sLat = String.valueOf(mLocation.getLatitude());
            String sLon = String.valueOf(mLocation.getLongitude());

            FragmentPagerAdapter fa = (FragmentPagerAdapter) viewPager.getAdapter();
            if (viewPager.getCurrentItem() == DMS_TO_DDD_TAB_POS) {
                DmsToDddFragment fragment = (DmsToDddFragment) fa.getItem(viewPager.getCurrentItem());
                fragment.updateLocation(sLat, sLon);
            }

//            if (viewPager.getCurrentItem() == DM_TO_DDD_TAB_POS) {
//                DmToDddFragment fragment = (DmToDddFragment) fa.getItem(viewPager.getCurrentItem());
//                fragment.clearFields();
//            }
//
//            if (viewPager.getCurrentItem() == DMS_TO_DM_TAB_POS) {
//                DmsToDmFragment fragment = (DmsToDmFragment) fa.getItem(viewPager.getCurrentItem());
//                fragment.clearFields();
//            }
        } else {
            Toast.makeText(MainActivity.this, R.string.cannot_get_location, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DmsToDddFragment(), "dms<->ddd");
        adapter.addFragment(new DmToDddFragment(), "dm<->ddd");
        adapter.addFragment(new DmsToDmFragment(), "dms<->dm");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static void setToNullIfMissing(TextView textView) {
        String sVal = String.valueOf(textView.getText());
        if(sVal.equals("")) textView.setText("0");
    }

    public static boolean checkDCoord(String sDeg, int minDeg, int maxDeg) {
        boolean isRightCoords = false;

        double deg = Double.parseDouble(sDeg);

        if(minDeg <= deg && deg <= maxDeg) {
            isRightCoords = true;
        }

        return isRightCoords;
    }

    public static boolean checkDMSCoord(String sDeg, String sMin, String sSec, int minDeg, int maxDeg) {
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

    public static boolean checkDMCoord(String sDeg, String sMin, int minDeg, int maxDeg) {
        boolean isRightCoords = false;

        int deg = Integer.parseInt(sDeg);
        int min = Integer.parseInt(sMin);

        if(minDeg <= deg && deg <= maxDeg) {
            if(0 <= min && min <= 59) {
                isRightCoords = true;
            }
        }

        return isRightCoords;
    }
}

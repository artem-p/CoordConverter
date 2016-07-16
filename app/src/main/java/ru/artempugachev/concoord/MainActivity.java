package ru.artempugachev.concoord;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.System.in;

public class MainActivity extends AppCompatActivity {
    //  todo вкладка свободных преобразований как в https://play.google.com/store/apps/details?id=com.in_con.coordinateconverter
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView mLatDeg, mLatMin, mLatSec, mLonDeg, mLonMin, mLonSec, mDecLat, mDecLon;
    private int DMS_TO_DDD_TAB_POS = 0;
    private int DM_TO_DDD_TAB_POS = 1;
    private int DMS_TO_DM_TAB_POS = 2;

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
                if(viewPager.getCurrentItem() == DMS_TO_DDD_TAB_POS) {
                    DmsToDddFragment fragment = (DmsToDddFragment) fa.getItem(viewPager.getCurrentItem());
                    fragment.clearFields();
                }

                if(viewPager.getCurrentItem() == DM_TO_DDD_TAB_POS) {
                    DmToDddFragment fragment = (DmToDddFragment) fa.getItem(viewPager.getCurrentItem());
                    fragment.clearFields();
                }

                if(viewPager.getCurrentItem() == DMS_TO_DM_TAB_POS) {
                    DmsToDmFragment fragment = (DmsToDmFragment) fa.getItem(viewPager.getCurrentItem());
                    fragment.clearFields();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void clearFields(TextView[] fields) {
//        //  Сбрасываем все поля
//        for (TextView field : fields) {
//            field.setText("");
//        }
//    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DmsToDddFragment(), "dms<->ddd");
        adapter.addFragment(new DmToDddFragment(), "dm<->ddd");
        adapter.addFragment(new DmsToDmFragment(), "dms<->dm");
        viewPager.setAdapter(adapter);
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

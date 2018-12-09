package enib.gala;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;

public class Admin_BarMode extends AppCompatActivity {

    private TextView mTextMessage;
    final int request_code_scan_bracelet=24; //qrcode scanner
    Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_product_selection:
                    toolbar.setTitle(getString(R.string.title_product_selection));
                    mTextMessage.setText(getString(R.string.title_product_selection));
                    return true;
                case R.id.navigation_list_product:
                    toolbar.setTitle(getString(R.string.title_list_product));
                    mTextMessage.setText(getString(R.string.title_list_product));
                    return true;
                case R.id.navigation_scan_and_pay:
                    toolbar.setTitle(getString(R.string.title_scan_and_pay));
                    Intent i =  new Intent();
                    i.setClass(getApplicationContext(), ScanBraceletActivity.class);
                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(),0,0);
                    startActivityForResult(i,request_code_scan_bracelet, activityOptions.toBundle());
                    mTextMessage.setText(getString(R.string.title_scan_and_pay));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__bar_mode);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Slidr.attach(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_code_scan_bracelet) {
            if (resultCode == RESULT_OK) {
                try
                {
                    String sData = data.getData().toString();
                    afterBraceletCodeReturn(sData);
                }
                catch (NullPointerException e)
                {
                    Log.e("braceletResult : ",e.getMessage());
                }
            }
        }
    }

    public void afterBraceletCodeReturn(String code)
    {
        //TODO : get info about the user and if the balance is good
    }

}

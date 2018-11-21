package enib.gala;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;


public class Admin_EntranceMode extends AppCompatActivity {

    private TextView mTextMessage;
    int request_Code=12;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_scan_qrcode:
                    mTextMessage.setText(R.string.title_scan_qrcode);
                    Intent intent = new Intent(getApplicationContext(), ScannedBarcodeActivity.class);
                    startActivityForResult(intent,request_Code);

                    return true;
                case R.id.navigation_scan_bracelet:
                    mTextMessage.setText(R.string.title_scan_bracelet);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__entrance_mode);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_Code) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();
                mTextMessage.setText(mTextMessage.getText().toString()+returnedResult);
            }
        }
    }

}

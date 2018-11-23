package enib.gala;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class Admin_EntranceMode extends AppCompatActivity {

    private TextView mTextQRCodeShow;
    private ViewFlipper mView;
    private ConstraintLayout mLayoutScanQRCode;
    int request_Code=12;
//    QRCodeValue

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mView.setDisplayedChild(0);
                    return true;
                case R.id.navigation_scan_qrcode:
                    mView.setDisplayedChild(1);
                    initScanQRCodeView();
                    Intent intent = new Intent(getApplicationContext(), ScannedBarcodeActivity.class);
                    startActivityForResult(intent,request_Code);

                    return true;
                case R.id.navigation_scan_bracelet:
                    mView.setDisplayedChild(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__entrance_mode);

        mView=findViewById(R.id.vf);

        initScanQRCodeView();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private void initScanQRCodeView()
    {
        mTextQRCodeShow=findViewById(R.id.QRCodeValue);
        mLayoutScanQRCode= findViewById(R.id.layoutScanQRCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_Code) {
            if (resultCode == RESULT_OK) {
                afterQRCodeReturn(data.getData().toString());
            }
        }
    }

    public void afterQRCodeReturn(String result)
    {
        initScanQRCodeView();
        if(result.isEmpty())
        {



            if(mLayoutScanQRCode!=null)
            {
                mLayoutScanQRCode.setBackgroundColor(Color.RED);
            }
            else
            {
                mTextQRCodeShow.setText("empty layout");
            }

        }
        else
        {
            mTextQRCodeShow.setText(result);
            boolean isCorrect=true;
            if(isCorrect)
            {
//                mLayoutScanQRCode.setBackgroundColor(getResources().getColor(R.color.red));
            }
            else
            {
//                mLayoutScanQRCode.setBackgroundColor(getResources().getColor(R.color.green));
            }

        }

    }

}

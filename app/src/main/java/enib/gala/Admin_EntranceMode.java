package enib.gala;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class Admin_EntranceMode extends AppCompatActivity {

    private TextView mTextViewQRCodeShow;
    private TextView mTextViewLastName;
    private TextView mTextViewFistName;
    private TextView mTextViewEmail;
    private TextView mTextViewAccountValue;
    private CardView mCardViewPlace;

    private ViewFlipper mView;
    private ConstraintLayout mLayoutScanQRCode;
    //define a requestcode to know witch activity return smth
    int request_Code=12; //qrcode scanner

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mView.setDisplayedChild(0);
                    return true;
                case R.id.navigation_scan_qrcode:
                    initScanQRCodeView();
                    Intent intent = new Intent(getApplicationContext(), ScannedBarcodeActivity.class);
                    startActivityForResult(intent,request_Code);
                    mView.setDisplayedChild(1);

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
        mTextViewQRCodeShow=findViewById(R.id.textView_qr_code_value);
        mLayoutScanQRCode= findViewById(R.id.layoutScanQRCode);
        mCardViewPlace=findViewById(R.id.card_view_place);

        mTextViewLastName=findViewById(R.id.textView_last_name_value);
        mTextViewFistName=findViewById(R.id.textView_first_name_value);
        mTextViewEmail=findViewById(R.id.textView_email_value);
        mTextViewAccountValue=findViewById(R.id.textView_account_value);
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

            mCardViewPlace.setCardBackgroundColor(Color.RED);

        }
        else
        {
            //ask to server


            String email="email";
            String last_name="last_name";
            String first_name="first_name";
            Double account=0.0;
            boolean achiveRequest=true;

            //update view
            if(achiveRequest)
            {
                mCardViewPlace.setCardBackgroundColor(Color.GREEN);
                setPlaceCardInfo(result,email,last_name,first_name,account);
            }
            else
            {
                mCardViewPlace.setCardBackgroundColor(Color.RED);
            }



        }

    }

    private void setPlaceCardInfo(String code, String email, String last_name, String first_name, Double account)
    {
        mTextViewQRCodeShow.setText(code);
        mTextViewLastName.setText(last_name);
        mTextViewFistName.setText(first_name);
        mTextViewEmail.setText(email);
        mTextViewAccountValue.setText(account.toString()+"€");
    }

}

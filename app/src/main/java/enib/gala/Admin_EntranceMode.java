package enib.gala;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.r0adkll.slidr.Slidr;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class Admin_EntranceMode extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ProgressBar mProgressBarHomeUserChecked;
    private TextView mTextViewHomeUserChecked;

    private TextView mTextViewQRCodeShow;
    private TextView mTextViewLastName;
    private TextView mTextViewFistName;
    private TextView mTextViewEmail;
    private TextView mTextViewAccountValue;
    private CardView mCardViewPlace;
    private CardView mCardViewInfo;
    private Boolean mPlaceWorks=false;
    private Boolean mBraceletWorks=false;

    private TextView mTextViewBraceletCode;
    private TextView mTextViewBraceletInfo;
    private TextView mTextViewPlaceInfo;

    private Button mButtonBind;
    private String mQRCodeValue = null;
    private String mBraceletValue = null;

    private ProgressDialog pDialog;

    private ViewFlipper mView;

    private SwipeRefreshLayout mSwipeRefresh;

    //define a request code to know witch activity return smth
    int request_code_scan_qrcode=12; //qrcode scanner
    int request_code_scan_bracelet=24; //qrcode scanner

    private UserAuth mAuth;
    private User mUser;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setupHome();
                    mView.setDisplayedChild(0);
                    return true;
                case R.id.navigation_scan_qrcode:
                    resetPlaceCard();
                    Intent intent = new Intent(getApplicationContext(), ScannedBarcodeActivity.class);
                    startActivityForResult(intent,request_code_scan_qrcode);
                    mView.setDisplayedChild(1);

                    return true;
                case R.id.navigation_scan_bracelet:
                    if(mPlaceWorks)
                    {
                        //lunch scan bracelet activity
                        Intent i =  new Intent();
                        i.setClass(getApplicationContext(), ScanBraceletActivity.class);
                        ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(),0,0);
                        startActivityForResult(i,request_code_scan_bracelet, activityOptions.toBundle());

                        //init display
                        mButtonBind.setEnabled(false);
                        mTextViewBraceletCode.setText("");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Place Problem", Toast.LENGTH_LONG).show();
                    }

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__entrance_mode);

        mAuth = new UserAuth(getApplicationContext());

        mView=findViewById(R.id.vf);

        initView();
        setupHome();

        Slidr.attach(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mSwipeRefresh=findViewById(R.id.swipeRefresh);
        mSwipeRefresh.setOnRefreshListener(this);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        if(mUser!=null)
        {

            mAuth.getAllInfo(mAuth.getCurrentUser()).getAllInfoListener(new UserAuth.GetAllInfoListener() {
                @Override
                public void GetAllInfoComplete(User u) {
                    if (u!=null)
                    {
                        Log.i("getAllInfo getAllInfoListener return", u.toString());
                        mUser=u;
                        if (!u.isAdmin())
                        {
                            finish();
                        }
                    }
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"no user found", Toast.LENGTH_LONG).show();
            Log.i("getCurrentUser : ", "no user found");
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        setupHome();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(false);
            }
        }, 2000);
    }

    private void setupHome()
    {
        //TODO
        Integer userChecked=366;
        Integer userTotal=666;
        Float percent= (userChecked.floatValue()/userTotal.floatValue())*100;
        mProgressBarHomeUserChecked.setProgress(percent.intValue());
        String text=userChecked.toString()+"/"+userTotal.toString();
        mTextViewHomeUserChecked.setText(text);
    }


    private void initView()
    {
        //home
        mProgressBarHomeUserChecked=findViewById(R.id.progressBarHomeUserChecked);
        mTextViewHomeUserChecked=findViewById(R.id.textViewHomeUserChecked);

        //qrcode
        mTextViewQRCodeShow=findViewById(R.id.textView_qr_code_value);
        mCardViewPlace=findViewById(R.id.card_view_place);
        mTextViewLastName=findViewById(R.id.textView_last_name_value);
        mTextViewFistName=findViewById(R.id.textView_first_name_value);
        mTextViewEmail=findViewById(R.id.textView_email_value);
        mTextViewAccountValue=findViewById(R.id.textView_account_value);
        mTextViewPlaceInfo=findViewById(R.id.textViewPlaceInfo);

        //bracelet
        mTextViewBraceletCode=findViewById(R.id.textViewBraceletCode);
        mButtonBind=findViewById(R.id.buttonBind);
        mCardViewInfo=findViewById(R.id.cardViewInfo);
        mTextViewBraceletInfo=findViewById(R.id.textViewBraceletInfo);

        mButtonBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new BraceletCheck().execute(mBraceletValue,mQRCodeValue);
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_code_scan_qrcode) {
            if (resultCode == RESULT_OK) {
                try {
                    afterQRCodeReturn(data.getData().toString());
                } catch (Exception e) {
                    Log.e("onActivityResult", e.getMessage(), e);
                }

            }
        }
        else if (requestCode == request_code_scan_bracelet) {
            if (resultCode == RESULT_OK) {
                try {
                    afterBraceletCodeReturn(data.getData().toString());
                } catch (Exception e) {
                    Log.e("onActivityResult", e.getMessage(), e);
                }

            }
        }
    }

    public void afterBraceletCodeReturn(String result)
    {
        mTextViewBraceletCode.setText(result);

        if(result.isEmpty())
        {
            Toast.makeText(this, "Nothing detected", Toast.LENGTH_SHORT).show();
            mBraceletValue=null;
            mBraceletWorks=false;

        }
        else
        {
            mBraceletValue=result;
            mView.setDisplayedChild(2);
            mButtonBind.setEnabled(true);
        }


    }

    public void afterQRCodeReturn(String result)
    {

        if(result.isEmpty())
        {
            mQRCodeValue=null;
            placeValid(false);
            Toast.makeText(this, "Nothing detected", Toast.LENGTH_SHORT).show();
            mPlaceWorks=false;
        }
        else
        {
            mQRCodeValue=result;
            new QRCodeCheck().execute(result);
        }

    }

    private void placeValid(boolean isValid)
    {
        if(isValid)
        {
            mCardViewPlace.setCardBackgroundColor(Color.GREEN);
        }
        else
        {
            Toast.makeText(this, "This ticket got problem", Toast.LENGTH_SHORT).show();
            mCardViewPlace.setCardBackgroundColor(Color.RED);
        }
    }

    private void braceletValid(boolean isValid)
    {
        if(isValid)
        {
            mCardViewInfo.setCardBackgroundColor(Color.GREEN);
        }
        else
        {
            Toast.makeText(this, "This ticket got problem", Toast.LENGTH_SHORT).show();
            mCardViewInfo.setCardBackgroundColor(Color.RED);
        }
    }

    private void setPlaceCardInfo(String code, String email, String last_name, String first_name, Double account)
    {
        mTextViewQRCodeShow.setText(code);
        mTextViewLastName.setText(last_name);
        mTextViewFistName.setText(first_name);
        mTextViewEmail.setText(email);
        String value=account.toString()+"€";
        mTextViewAccountValue.setText(value);
    }

    private void resetPlaceCard()
    {
        mTextViewQRCodeShow.setText("");
        mTextViewLastName.setText("");
        mTextViewFistName.setText("");
        mTextViewEmail.setText("");
        mTextViewAccountValue.setText("");
        mCardViewPlace.setCardBackgroundColor(Color.WHITE);
    }

    public class QRCodeCheck extends AsyncTask<String, Void, String> {

        static final String API_URL = "https://api.leeap.cash/";

        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            try {
                URL url = new URL(API_URL); // here is your URL path
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setReadTimeout(7000);
                con.setConnectTimeout(7000);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setInstanceFollowRedirects(false);
                con.setRequestMethod("POST");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("QRCode", args[0]);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("ticket",jsonObject);
                String param =jsonObject2.toString();


                PrintWriter out = new PrintWriter(con.getOutputStream());
                out.print("stringified=");
                out.print(param);
                out.close();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    con.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            mTextViewPlaceInfo.setText(result);
            try {

                JSONObject obj = new JSONObject(result);
                if((boolean) obj.get("success"))
                {
                    mPlaceWorks = true;
                    String sUsed = null;
                    Integer used = null;
                    String sBalance = null;
                    Double balance = null;

                    try {
                        sUsed = (String) obj.get("Used");
                        used = Integer.parseInt(sUsed);
                        sBalance = (String) obj.get("Balance");
                        balance = Double.parseDouble(sBalance) / 100;
//                        Log.i("used",used.toString());
                    } catch (Exception e) {
                        Log.e("ERROR", e.getMessage(), e);
                    }

                    try {
                        setPlaceCardInfo((String) obj.get("QRCode"), (String) obj.get("Email"), (String) obj.get("Name"), (String) obj.get("Firstname"), balance);
                    } catch (Exception e) {
                        Log.e("ERROR", e.getMessage(), e);
                    }


                    boolean valid = false;

                    try {
                        valid = (used == 0);
                    } catch (Exception e) {
                        Log.e("ERROR", e.getMessage(), e);
                    }
                    Log.i("valid", valid ? "true" : "false");

                    Toast.makeText(getApplicationContext(), valid ? "unused ticket" : "this ticket is already used", Toast.LENGTH_LONG).show();

                    placeValid(valid);
                    mPlaceWorks = valid;
                    if (valid)
                    {
                        //unused ticket
                    }
                    else
                    {
                        //already used ticket
                        mPlaceWorks = false;
                        //TODO : implement force set
                    }
                }
                else
                {
                    mPlaceWorks=false;
                    placeValid(false);
                    setPlaceCardInfo(mQRCodeValue, null, null, null, null);
                    Toast.makeText(getApplicationContext(),"problem", Toast.LENGTH_LONG).show();
                }

                Log.d("My App", obj.toString());

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
            }
        }
    }

    public class BraceletCheck extends AsyncTask<String, Void, String> {

        static final String API_URL = "https://api.leeap.cash/";

        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(Admin_EntranceMode.this);
            pDialog.setMessage("execute");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            try {
                URL url = new URL(API_URL); // here is your URL path
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setReadTimeout(7000);
                con.setConnectTimeout(7000);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setInstanceFollowRedirects(false);
                con.setRequestMethod("POST");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("QRCode", args[1]);
                jsonObject.accumulate("bracelet", args[0]);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("bind",jsonObject);
                JSONObject jsonObject3 = new JSONObject();
                jsonObject3.accumulate("ticket",jsonObject2);
                String param =jsonObject3.toString();


                PrintWriter out = new PrintWriter(con.getOutputStream());
                out.print("stringified=");
                out.print(param);
                out.close();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    con.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {

            mTextViewBraceletInfo.setText(result);
            try {

                JSONObject obj = new JSONObject(result);
//                JSONArray array = new JSONArray(obj);
                if((boolean) obj.get("success"))
                {
                    //TODO check if already used
                    braceletValid(true);
                    mBraceletWorks=true;

                    Toast.makeText(getApplicationContext(),"success ", Toast.LENGTH_LONG).show();


                }
                else
                {
                    mBraceletWorks=false;
                    braceletValid(false);
                    Toast.makeText(getApplicationContext(),"problem", Toast.LENGTH_LONG).show();

                }

                Log.d("My App", obj.toString());

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
            }
            pDialog.dismiss();
        }
    }

}

package enib.gala;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.r0adkll.slidr.Slidr;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Admin_BarMode extends AppCompatActivity {

    final int request_code_scan_bracelet=24; //qrcode scanner
    Toolbar toolbar;

    private UserAuth mAuth;
    private User mUser;

    private ViewFlipper mView;

    private LinearLayout mLinearLayoutProducts;
    private ListView mListViewSelectedConso;

    private List<Product> productsList = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_product_selection:
                    toolbar.setTitle(getString(R.string.title_product_selection));
                    mView.setDisplayedChild(0);
                    return true;
                case R.id.navigation_list_product:
                    toolbar.setTitle(getString(R.string.title_list_product));
                    mView.setDisplayedChild(1);

                    mListViewSelectedConso.setAdapter(new CustomProductListAdapter(getApplicationContext(),productsList));
                    mListViewSelectedConso.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                            Object o = mListViewSelectedConso.getItemAtPosition(position);
                            Product c = (Product) o;
                            AlertDialog alertDialog = new AlertDialog.Builder(Admin_BarMode.this).create();
                            alertDialog.setTitle("Info");
                            alertDialog.setMessage(c.toString());
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });
                    return true;
                case R.id.navigation_scan_and_pay:
                    toolbar.setTitle(getString(R.string.title_scan_and_pay));
                    mView.setDisplayedChild(2);
                    Intent i =  new Intent();
                    i.setClass(getApplicationContext(), ScanBraceletActivity.class);
                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(),0,0);
                    startActivityForResult(i,request_code_scan_bracelet, activityOptions.toBundle());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__bar_mode);

        mAuth = new UserAuth(getApplicationContext());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Slidr.attach(this);

        mView=findViewById(R.id.vf);
        mLinearLayoutProducts=findViewById(R.id.linearLayoutProducts);
        mListViewSelectedConso=findViewById(R.id.listViewSelectedConso);



        new Consommation().execute();
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

    public class Consommation extends AsyncTask<String, Void, String> {

        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            try {
                URL url = new URL(Data.getApiUrl()); // here is your URL path
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setReadTimeout(7000);
                con.setConnectTimeout(7000);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setInstanceFollowRedirects(false);
                con.setRequestMethod("POST");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("get", true);

                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("consommations",jsonObject);
                String param =jsonObject2.toString();

                PrintWriter out = new PrintWriter(con.getOutputStream());
                out.print("stringified=");
                out.print(param);
                out.close();

                Log.i("param", param);

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
            Log.i("Consommation result",result);
            try {
                JSONObject obj = new JSONObject(result);
//                Log.d("Consommation", obj.toString());
                if((boolean) obj.get("success"))
                {
                    try
                    {
                        JSONArray data =(JSONArray)  obj.get("data");
                        Log.d("Consommation", data.toString());
                        if (data.length()>0)
                        {
                            mLinearLayoutProducts.removeAllViewsInLayout();
                            for(int i=0; i<data.length(); i++){
                                try
                                {
                                    JSONObject productJSON= data.getJSONObject(i);
                                    Integer id =Integer.parseInt((String) productJSON.get("Id"));
                                    Integer sPrice = Integer.parseInt((String) productJSON.get("Price"));
                                    Double price = sPrice.doubleValue()/100;
                                    Integer size =Integer.parseInt((String) productJSON.get("Size"));
                                    Integer sAvailable =Integer.parseInt((String) productJSON.get("Available"));
                                    final Product p = new Product(id,price,(String) productJSON.get("Name"),size,(String) productJSON.get("SizeUnit"),(sAvailable==1));
                                    Log.i("Consommation product",p.toString());
                                    Button b= new Button(getApplicationContext());
                                    b.setText(p.getName());
                                    b.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //TODO add to list
                                            productsList.add(p);
                                        }
                                    });
                                    b.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            Toast.makeText(getApplicationContext(),p.toString(), Toast.LENGTH_LONG).show();
                                            return false;
                                        }
                                    });
                                    mLinearLayoutProducts.addView(b);
                                }
                                catch (Exception e)
                                {
                                    Log.e("ERROR", e.getMessage(), e);
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"no conso found", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("ERROR", e.getMessage(), e);
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"failed", Toast.LENGTH_LONG).show();
                    //TODO
                }
            } catch (Throwable t) {
                Log.e("Consommation", "Could not parse malformed JSON: \"" + result + "\"");
            }
        }
    }

}

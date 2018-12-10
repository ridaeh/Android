package enib.gala;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private TextView mTextViewTest;
    private TextView mTextViewParam;

    static final String API_URL = "https://api.leeap.cash/";

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello "+ mAuth.getCurrentUser().getEmail(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        mAuth.getUid();
        mAuth.getCurrentUser().getUid();


        mTextViewTest=findViewById(R.id.textViewTest);
        mTextViewParam = findViewById(R.id.textViewParam);

        findViewById(R.id.buttonTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new ConnectionPost().execute("marc.marronnier@yopmail.com","test");
            }
        });

        new ConnectionPost().execute("salut","bijour");

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_disconnect)
        {
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if (id == R.id.action_admin)
        {
            Intent intent = new Intent(getApplicationContext(), Admin_Selection.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ticket) {
            Intent intent = new Intent(getApplicationContext(), ShowTicket.class);
            startActivity(intent);

        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(getApplicationContext(), ShowHistory.class);
            startActivity(intent);

        } else if (id == R.id.nav_store) {
            Intent intent = new Intent(getApplicationContext(), Store.class);
            startActivity(intent); //TODO : on activity return update

        } else if (id == R.id.nav_reload) { //disabled

        } else if (id == R.id.nav_share) { //disabled

        } else if (id == R.id.nav_send_feedback) { //disabled

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class ConnectionPost extends AsyncTask<String, Void, String> {

        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(Main.this);
            pDialog.setMessage("execute");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            mTextViewTest.setText("");
            mTextViewParam.setText("");
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
                jsonObject.accumulate("identifier", args[0]);
                jsonObject.accumulate("password", args[1]);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("login",jsonObject);
                String param =jsonObject2.toString();
                mTextViewParam.setText(param);


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
            mTextViewTest.setText("result : "+result);

            try {

                JSONObject obj = new JSONObject(result);
//                JSONArray array = new JSONArray(obj);
                if((boolean) obj.get("success"))
                {
                    String token = (String) obj.get("token");
                    new ConnectionInfoPost().execute(token);
                    Toast.makeText(getApplicationContext(),"connected "+token, Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"problem", Toast.LENGTH_LONG).show();
                }

                Log.d("My App", obj.toString());

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
            }
            pDialog.dismiss();
        }
    }

    public class ConnectionInfoPost extends AsyncTask<String, Void, String> {

        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(Main.this);
            pDialog.setMessage("execute");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            mTextViewTest.setText("");
            mTextViewParam.setText("");
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
                jsonObject.accumulate("getLogedUserProfile", true);
                jsonObject.accumulate("token", args[0]);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("profile",jsonObject);
                String param =jsonObject2.toString();
                mTextViewParam.setText(param);


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
            mTextViewTest.setText("result : "+result);

            try {

                JSONObject obj = new JSONObject(result);
//                JSONArray array = new JSONArray(obj);
                if((boolean) obj.get("success"))
                {
                    String token = (String) obj.get("token");
                    Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_LONG).show();

                }
                else
                {
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


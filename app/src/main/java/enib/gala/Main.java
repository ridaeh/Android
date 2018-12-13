package enib.gala;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private UserAuth mAuth;
    private User mUser;
    private TextView mTextViewUserEmail;

    private EditText mEditTextAccountBalanceValue;
    private CardView mCardViewNoTicket;
    private CardView mCardViewTicket;
    private CardView mCardViewAccountBalance;
    private MenuItem mMenuViewActionAdmin;

    static private int request_code_store=12;

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
//                Snackbar.make(view, "Hello "+ mAuth.getCurrentUser().getEmail(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mTextViewUserEmail = headerView.findViewById(R.id.textViewUserEmail);
        mTextViewUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hello "+mUser.getFirstName(), Toast.LENGTH_LONG).show();
            }
        });

        mAuth = new UserAuth(getApplicationContext());


        //content
        ImageButton mImageButtonGoToStore=findViewById(R.id.imageButtonGoToStore);
        mImageButtonGoToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent();
                i.setClass(getApplicationContext(), Store.class);
                ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(),0,0);
                startActivityForResult(i,request_code_store, activityOptions.toBundle());
            }
        });

        ImageButton mImageButtonGetBalanceDetail=findViewById(R.id.imageButtonGetBalanceDetail);
        mImageButtonGetBalanceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowHistory.class);
                startActivity(intent);

            }
        });
        mEditTextAccountBalanceValue=findViewById(R.id.editTextAccountBalanceValue);
        mEditTextAccountBalanceValue.setKeyListener(null);
        mEditTextAccountBalanceValue.clearFocus();
//        mEditTextAccountBalanceValue.setText("0.0€");


        mCardViewNoTicket=findViewById(R.id.cardViewNoTicket);
        mCardViewTicket=findViewById(R.id.cardViewTicket);
        mCardViewNoTicket.setVisibility(View.GONE);
        mCardViewTicket.setVisibility(View.VISIBLE);

        mCardViewAccountBalance=findViewById(R.id.cardViewCurrentAccountSolde);

    }

    @Override
    public void onStart() {
//        Toast.makeText(getApplicationContext(),"onStart", Toast.LENGTH_LONG).show();
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
                        updateView();
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
//        Toast.makeText(getApplicationContext(),"onActivityResult", Toast.LENGTH_LONG).show();
        updateView();
        if (requestCode == request_code_store) {
            if (resultCode == RESULT_OK) {
                //TODO usefull ?
            }
        }
    }

    public void updateView()
    {
//        Toast.makeText(getApplicationContext(),"updateView", Toast.LENGTH_LONG).show();
        mTextViewUserEmail.setText(mUser.getEmail());
        Double balance = mUser.getBalance();
        if(balance!=null)
        {
            String text = balance.toString()+"€";
            mEditTextAccountBalanceValue.setText(text);
        }
        else
        {
            mEditTextAccountBalanceValue.setText("0.0€");
        }
        boolean isTicket=false;
        mCardViewNoTicket.setVisibility(isTicket ? View.GONE : View.VISIBLE);
        mCardViewTicket.setVisibility(!isTicket ? View.GONE : View.VISIBLE);

        try
        {
            mMenuViewActionAdmin.setVisible(mUser.isAdmin());
        }
        catch (Exception e)
        {
            Log.e("updateView set visible? admin button", e.toString());
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
        try
        {
            mMenuViewActionAdmin = menu.findItem(R.id.action_admin);
        }
        catch (Exception e)
        {
            Log.e("onCreateOptionsMenu set action admin to menu", e.toString());
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
            if (mUser.isAdmin())
            {
                Intent intent = new Intent(getApplicationContext(), Admin_Selection.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"not allowed", Toast.LENGTH_LONG).show();
            }

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
}


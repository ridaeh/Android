package enib.gala;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

public class ShowHistory extends AppCompatActivity {
    private ListView mListViewConso;
    private UserAuth mAuth;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        mAuth = new UserAuth(getApplicationContext());
        Slidr.attach(this);
        mListViewConso=findViewById(R.id.listViewConso);

        List<Consumption> consumptionList = new ArrayList<>();


        mListViewConso.setAdapter(new CustomConsumptionListAdapter(this, consumptionList));

        mListViewConso.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = mListViewConso.getItemAtPosition(position);
                Consumption c = (Consumption) o;
//                Toast.makeText(getApplicationContext(), "Selected :" + " " + c, Toast.LENGTH_LONG).show();

                AlertDialog alertDialog = new AlertDialog.Builder(ShowHistory.this).create();
                alertDialog.setTitle("Info");
                alertDialog.setMessage(c.toStringProper());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            mAuth.getAllInfo(mAuth.getCurrentUser()).getAllInfoListener(new UserAuth.GetAllInfoListener() {
                @Override
                public void GetAllInfoComplete(User u) {
                    if (u != null) {
                        Log.i("getAllInfo getAllInfoListener return", u.toString());
                        mUser = u;
                        if (!u.isAdmin()) {
                            finish();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "no user found", Toast.LENGTH_LONG).show();
            Log.i("getCurrentUser : ", "no user found");
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        Historical h = new Historical();
        h.getHistorical(mUser.getId()).setGetHistoricalCompleteListener(new Historical.GetHistoricalCompleteListener() {
            @Override
            public void GetHistoricalComplete(boolean success, String text, List<Consumption> consumptionList) {
                if (success) {
                    mListViewConso.setAdapter(new CustomConsumptionListAdapter(getApplicationContext(), consumptionList));
                } else {
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}

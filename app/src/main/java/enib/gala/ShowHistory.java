package enib.gala;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

public class ShowHistory extends AppCompatActivity {
    private ListView mListViewConso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        Slidr.attach(this);
        mListViewConso=findViewById(R.id.listViewConso);

        List<Consumption> consumptionList = new ArrayList<>();
        consumptionList.add(new Consumption("ecocup", -1.0, 1));
        consumptionList.add(new Consumption("bouteille champagne", -22.0, 2));
        consumptionList.add(new Consumption("metre biere", -12.0, 2013,1,null,null,5.5));
        consumptionList.add(new Consumption("cb", 20.0, 2));
        consumptionList.add(new Consumption("vestiaire", -1.0, 2));
        consumptionList.add(new Consumption("preload", 20.0, 2));

        mListViewConso.setAdapter(new CustomListAdapter(this, consumptionList));

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
}

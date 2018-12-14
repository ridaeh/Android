package enib.gala;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Bracelet {
    private BraceletScanCompleteListener listenerBraceletScan;

    public Bracelet() {
        this.listenerBraceletScan = null;
    }

    public interface BraceletScanCompleteListener {
        void BraceletScanComplete(boolean success,String text,Double solde,Integer id);
    }

    public void setSignInCompleteListener(BraceletScanCompleteListener listener) {
        this.listenerBraceletScan = listener;
    }

    public Bracelet scanBracelet(String code)
    {
        new BraceletPost().execute(code);
        return this;
    }

    public class BraceletPost extends AsyncTask<String, Void, String> {

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
                jsonObject.accumulate("getBalance", args[0]);

                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("bracelet",jsonObject);
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
            Log.i("ConnectionPost result",result);
            try {
                JSONObject obj = new JSONObject(result);
                Double balance=null;
                Integer id =null;
                if((boolean) obj.get("success"))
                {
                    String sBalance = (String) obj.get("balance");
                    String sId =(String) obj.get("userId");
                    try
                    {
                        balance =Double.parseDouble(sBalance)/100;
                        id= Integer.parseInt(sId);
                    }
                    catch (Exception e)
                    {
                        Log.e("ERROR", e.getMessage(), e);
                    }
                    listenerBraceletScan.BraceletScanComplete(true,(String) obj.get("text"),balance,id);
                }
                else
                {
                    listenerBraceletScan.BraceletScanComplete(false,(String) obj.get("text"),balance,id);
                }

                Log.d("My App", obj.toString());

            } catch (Throwable t) {
                listenerBraceletScan.BraceletScanComplete(false,"result error",null,null);
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");

            }
        }
    }
}

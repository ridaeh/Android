package cash.leeap;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static cash.leeap.Data.API_URL;

public class Bracelet {
    private BraceletScanCompleteListener listenerBraceletScan;
    private BraceletScanManualRechargeCompleteListener listenerBraceletScanManualRecharge;

    public Bracelet() {
        this.listenerBraceletScan = null;
        this.listenerBraceletScanManualRecharge = null;
    }

    public interface BraceletScanCompleteListener {
        void BraceletScanComplete(boolean success, String text, Double solde, Integer id);
    }

    public void setSignInCompleteListener(BraceletScanCompleteListener listener) {
        this.listenerBraceletScan = listener;
    }

    public Bracelet scanBracelet(String code)
    {
        new BraceletPost().execute(code);
        return this;
    }

    public void setManualRechargeCompleteListener(BraceletScanManualRechargeCompleteListener listener) {
        this.listenerBraceletScanManualRecharge = listener;
    }

    public Bracelet manualRechargeBracelet(String code, Double amout, String adminToken) {
        RechargeBraceletPost r = new RechargeBraceletPost();
        r.setAdminToken(adminToken);
        r.setAmout(amout);
        r.setCode(code);
        r.execute();
        return this;
    }

    public interface BraceletScanManualRechargeCompleteListener {
        void BraceletScanManualRechargeComplete(boolean success, String text);
    }

    public class RechargeBraceletPost extends AsyncTask<String, Void, String> {
        private Double amout;
        private String code;
        private String adminToken;

        public void setAmout(Double amout) {
            this.amout = amout;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setAdminToken(String adminToken) {
            this.adminToken = adminToken;
        }

        protected void onPreExecute() {
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
                jsonObject.accumulate("amount", amout * 100);

                jsonObject.accumulate("token", adminToken);
                jsonObject.accumulate("bracelet", code);

                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("manualRecharge", jsonObject);

                JSONObject jsonObject3 = new JSONObject();
                jsonObject3.accumulate("bracelet", jsonObject2);
                String param = jsonObject3.toString();
                Log.i("RechargeBraceletPost param", param);

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
                } finally {
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("ConnectionPost result", result);
            try {
                JSONObject obj = new JSONObject(result);

                if ((boolean) obj.get("success")) {
                    listenerBraceletScanManualRecharge.BraceletScanManualRechargeComplete(true, "success");
                } else {
                    listenerBraceletScanManualRecharge.BraceletScanManualRechargeComplete(false, (String) obj.get("text"));
                }

                Log.d("My App", obj.toString());

            } catch (Throwable t) {
                listenerBraceletScanManualRecharge.BraceletScanManualRechargeComplete(false, "result error");
                Log.e("RechargeBraceletPost", "Could not parse malformed JSON: \"" + result + "\"", t);

            }
        }
    }

    public class BraceletPost extends AsyncTask<String, Void, String> {

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

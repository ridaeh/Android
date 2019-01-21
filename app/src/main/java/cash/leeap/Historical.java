package cash.leeap;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static cash.leeap.Data.API_URL;

public class Historical {
    private GetHistoricalCompleteListener listenerGetHistoricalComplete;

    public Historical() {
        this.listenerGetHistoricalComplete = null;
    }

    public void setGetHistoricalCompleteListener(GetHistoricalCompleteListener listener) {
        this.listenerGetHistoricalComplete = listener;
    }

    public Historical getHistorical(Integer userId) {
        HisoricalPost h = new HisoricalPost();
        h.setUserId(userId);
        h.execute();
        return this;
    }

    public interface GetHistoricalCompleteListener {
        void GetHistoricalComplete(boolean success, String text, List<Consumption> consumptionList);
    }

    public class HisoricalPost extends AsyncTask<String, Void, String> {
        private Integer userId;


        protected void setUserId(Integer userId) {
            this.userId = userId;
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
                jsonObject.accumulate("user", userId);

                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.accumulate("history", jsonObject);
                JSONObject jsonObject3 = new JSONObject();
                jsonObject3.accumulate("consommations", jsonObject2);
                String param = jsonObject3.toString();

                Log.i("param", param);

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
            Log.i("PayPost result", result);
            try {
                JSONObject obj = new JSONObject(result);
                try {
                    boolean success = (boolean) obj.get("success");
                    if (success) {

                        try {
                            List<Consumption> consumptionList = new ArrayList<>();
                            JSONArray historical = (JSONArray) obj.get("history");
                            Log.i("PayPost historical", historical.toString());
                            for (int i = 0; i < historical.length(); i++) {
                                JSONObject j = historical.getJSONObject(i);
                                Log.i("PayPost historical object", j.toString());
                                String sCount = (String) j.get("Qt");
                                Integer count = Integer.parseInt(sCount);
                                String sPrice = (String) j.get("Total");
                                Double price = Double.parseDouble(sPrice) / (100 * count);
                                consumptionList.add(new Consumption((String) j.get("Name"), -price, null, count, null, null, null, (String) j.get("Date")));
                            }
                            listenerGetHistoricalComplete.GetHistoricalComplete(true, null, consumptionList);
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage(), e);
                            listenerGetHistoricalComplete.GetHistoricalComplete(false, "internal error", null);
                        }


                    } else {
                        listenerGetHistoricalComplete.GetHistoricalComplete(false, "error", null); //TODO wait api upd and return witch error
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    listenerGetHistoricalComplete.GetHistoricalComplete(false, "return error", null);
                }


                Log.d("PayPost", obj.toString());

            } catch (Throwable t) {
                listenerGetHistoricalComplete.GetHistoricalComplete(false, "internal error", null);
                Log.e("PayPost", "Could not parse malformed JSON: \"" + result + "\"");

            }
        }
    }
}
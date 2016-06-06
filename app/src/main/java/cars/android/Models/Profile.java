package cars.android.Models;

import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Analystik on 2016-06-02.
 */
public class Profile  extends AsyncTask<Profile, Void, FinancialEvaluation> {

    public int KMPerYear;
    public int ProvinceId;
    public int CarId;

    @Override
    protected FinancialEvaluation doInBackground(Profile... params) {
        try {
            URL url = new URL("http://cars101.azurewebsites.net/api/calculate");
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            hc.addRequestProperty("Content-Type", "application/json");
            hc.addRequestProperty("Accept", "application/json");
            hc.setRequestMethod("PUT");

            //Prepare and send JSON object
            JSONObject js = new JSONObject();
            js.put("CarId", Integer.toString(params[0].CarId));
            js.put("KMPerYear", Integer.toString(params[0].KMPerYear));
            js.put("ProvinceId", Integer.toString(params[0].ProvinceId));
            OutputStream outputStream = new BufferedOutputStream(hc.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            writer.write(js.toString());
            writer.flush();
            writer.close();
            outputStream.close();

            int responseCode = hc.getResponseCode();
            if (responseCode == 200) {
                FinancialEvaluation f= new FinancialEvaluation();
                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String line;
                StringBuilder responseOutput = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    JSONObject jsonObject =  new JSONObject(line);
                    f = new FinancialEvaluation();
                    f.GasConsumptionIn8Years = Math.round(jsonObject.getDouble("GasConsumptionIn8Years"));
                    f.ElectricityConsumptionIn8Years = Math.round(jsonObject.getDouble("ElectricityConsumptionIn8Years"));
                    f.DeltaPrice = Math.round(jsonObject.getDouble("DeltaPrice"));
                    f.BatteryExpenses = Math.round(jsonObject.getDouble("BatteryExpenses"));
                    f.MileageIn8Years = Math.round(jsonObject.getDouble("MileageIn8Years"));
                    f.GasTotalExpensesPer100km = Math.round(jsonObject.getDouble("GasTotalExpensesPer100km"));
                    f.GasTotalExpensesIn8Years = Math.round(jsonObject.getDouble("GasTotalExpensesIn8Years"));
                    f.ElectricityTotalExpensesPer100km =  Math.round(jsonObject.getDouble("ElectricityTotalExpensesPer100km"));
                    f.ElectricityTotalExpensesIn8Years = Math.round(jsonObject.getDouble("ElectricityTotalExpensesIn8Years"));
                }
                br.close();
                return f;
            }
        } catch (Exception ex) {
            System.out.println("Erreur GET METHOD: " + ex);
        }
        return null;
    }
}
package cars.android.Models;

import android.os.AsyncTask;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Analystik on 2016-06-02.
 */
public class Province extends AsyncTask<Integer, List<Car>, List<Province>> {
    public int Id;
    public String Name;
    public double ElectricityPrice;
    public double ElectricityPriceGrowthRate;
    public double GasPrice;
    public double GasPriceGrowthRate;

    // What to display in the Spinner list.
    @Override
    public String toString() {
        return this.Name;
    }

    @Override
    protected List<Province> doInBackground(Integer... params) {
        try {
            List<Province> provinces = new ArrayList<>();
            URL url = new URL("http://cars101.azurewebsites.net/api/provinces");
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            hc.setRequestMethod("GET");
            int responseCode = hc.getResponseCode();

            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    JSONArray jsonArray = new JSONArray(line);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Province p = new Province();
                        p.Id= jsonArray.getJSONObject(i).getInt("Id");
                        p.Name = jsonArray.getJSONObject(i).getString("Name");
                        p.ElectricityPrice = jsonArray.getJSONObject(i).getDouble("ElectricityPrice");
                        p.ElectricityPriceGrowthRate = jsonArray.getJSONObject(i).getDouble("ElectricityPriceGrowthRate");
                        p.GasPrice = jsonArray.getJSONObject(i).getDouble("GasPrice");
                        p.GasPriceGrowthRate = jsonArray.getJSONObject(i).getDouble("GasPriceGrowthRate");
                        provinces.add(p);
                    }
                }
                br.close();
                return provinces;
            }
        } catch (Exception ex) {
            System.out.println("Erreur GET METHOD: " + ex);
        }
        return null;
    }

}
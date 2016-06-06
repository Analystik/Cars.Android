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
public class Car extends AsyncTask<Integer, Void, List<Car>> {

    public int Id;
    public int Year4Digits;
    public double Consumption;
    public int Price;

    // What to display in the Spinner list.
    @Override
    public String toString() {
        return Integer.toString(this.Year4Digits);
    }

    @Override
    protected List<Car> doInBackground(Integer... params) {
        try {
            List<Car> cars = new ArrayList<>();
            URL url = new URL("http://cars101.azurewebsites.net/api/makes/"+params[0]+"/models/"+params[1]+"/Cars");
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
                        Car c = new Car();
                        c.Id = jsonArray.getJSONObject(i).getInt("Id");
                        c.Year4Digits = jsonArray.getJSONObject(i).getInt("Year4Digits");
                        c.Consumption = jsonArray.getJSONObject(i).getInt("Consumption");
                        c.Price = jsonArray.getJSONObject(i).getInt("Price");
                        cars.add(c);
                    }
                }
                br.close();
                return cars;
            }
        } catch (Exception ex) {
            System.out.println("Erreur GET METHOD: " + ex);
        }
        return null;
    }
}